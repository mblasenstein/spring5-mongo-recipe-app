package guru.springframework.services;

import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Test
    public void getRecipeByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        String recipeId = "1";
        recipe.setId(recipeId);
        Mono<Recipe> recipeMono = Mono.just(recipe);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);

        Recipe recipeReturned = recipeService.findById(recipeId).block();

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeReactiveRepository, times(1)).findById(recipeId);
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipesTest() {

        when(recipeReactiveRepository.findAll()).thenReturn(Flux.just(new Recipe()));
        List<Recipe> recipes = recipeReactiveRepository.findAll().collectList().block();
        assertEquals(recipes.size(), 1);
        verify(recipeReactiveRepository, times(1)).findAll();
    }

    @Test
    public void deleteRecipeById() {

        // given
        String idToDelete = "2";

        // when
        recipeService.deleteById(idToDelete);

        // then
        verify(recipeReactiveRepository, times(1)).deleteById(idToDelete);
    }
}