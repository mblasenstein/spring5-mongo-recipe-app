package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private static Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    public IngredientServiceImpl(
            IngredientCommandToIngredient ingredientCommandToIngredient,
            IngredientToIngredientCommand ingredientToIngredientCommand,
            RecipeReactiveRepository recipeReactiveRepository,
            UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository
    ) {
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Mono<Recipe> recipe = recipeReactiveRepository.findById(command.getRecipeId());

        // todo throw error if not found
        if (recipe == null) {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        }

        Mono<Ingredient> existingIngredient = recipe
                .flatMapIterable(Recipe::getIngredients)
                .filter(i -> i.getId().equals(command.getId()))
                .single();


        if (existingIngredient != null) {
            recipe
                    .map(r -> {
                        r.removeIngredientById(command.getId());
                        r.addIngredient(ingredientCommandToIngredient.convert(command));
                        return r;
                    })
                    .flatMap(recipeReactiveRepository::save)
                    .subscribe();
        }

        return Mono.just(command);
    }

    @Override
    public Mono<Void> deleteIngredientFromRecipe(String recipeId, String ingredientId) {
        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {
            log.debug("Found recipe id " + recipeId);

            Optional<Ingredient> ingredientToDelete = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (ingredientToDelete.isPresent()) {
                log.debug("Found ingredient id: " + ingredientId);
                Ingredient ingredient = ingredientToDelete.get();
                ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);
                recipeReactiveRepository.save(recipe).block();
                log.debug(String.format("Deleted ingredient id %s from recipe id %s", ingredientId, recipeId));
            }
        } else {
            log.debug("Recipe id " + recipeId + " not found");
        }
        return Mono.empty();
    }
}
