package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Category;
import guru.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RecipeToRecipeCommand implements Converter<Mono<Recipe>, Mono<RecipeCommand>> {

    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    public RecipeToRecipeCommand(CategoryToCategoryCommand categoryConveter, IngredientToIngredientCommand ingredientConverter,
                                 NotesToNotesCommand notesConverter) {
        this.categoryConverter = categoryConveter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Mono<RecipeCommand> convert(Mono<Recipe> recipeMono) {
        if (recipeMono == null) {
            return null;
        }


        return recipeMono
                .map(recipe -> {
                    final RecipeCommand command = new RecipeCommand();

                    command.setId(recipe.getId());
                    command.setCookTime(recipe.getCookTime());
                    command.setPrepTime(recipe.getPrepTime());
                    command.setDescription(recipe.getDescription());
                    command.setDifficulty(recipe.getDifficulty());
                    command.setDirections(recipe.getDirections());
                    command.setServings(recipe.getServings());
                    command.setSource(recipe.getSource());
                    command.setUrl(recipe.getUrl());
                    command.setImage(recipe.getImage());
                    command.setNotes(notesConverter.convert(recipe.getNotes()));

                    if (recipe.getCategories() != null && recipe.getCategories().size() > 0) {
                        recipe.getCategories()
                                .forEach((Category category) -> command.getCategories().add(categoryConverter.convert(category)));
                    }

                    if (recipe.getIngredients() != null && recipe.getIngredients().size() > 0) {
                        recipe.getIngredients()
                                .forEach(ingredient -> command.getIngredients().add(ingredientConverter.convert(ingredient)));
                    }

                    return command;
                });
    }
}
