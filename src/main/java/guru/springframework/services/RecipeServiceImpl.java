package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private static Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    public Flux<Recipe> getRecipes() {
        
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {

        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {

        Mono<Recipe> savedRecipe = recipeReactiveRepository.save(recipeCommandToRecipe.convert(command));
        Recipe recipe = savedRecipe.block();
        if (recipe != null) {
            log.debug("Saved RecipeId:" + recipe.getId());
            return Mono.just(Objects.requireNonNull(recipeToRecipeCommand.convert(recipe)));
        }
        throw new RuntimeException("RecipeCommand failed to save");
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        RecipeCommand command = recipeToRecipeCommand.convert(findById(id).block());
        return Mono.just(command);
    }

    @Override
    public void deleteById(String id) {
        recipeReactiveRepository.deleteById(id);
    }
}
