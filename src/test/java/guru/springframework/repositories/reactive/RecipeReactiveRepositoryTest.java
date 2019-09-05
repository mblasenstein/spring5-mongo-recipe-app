package guru.springframework.repositories.reactive;

import guru.springframework.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class RecipeReactiveRepositoryTest {

    private static Logger log = LoggerFactory.getLogger(RecipeReactiveRepositoryTest.class);

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void testRecipeSave() {

        Recipe recipe = new Recipe();
        recipe.setDescription("Test");

        recipeReactiveRepository.save(recipe).block();

        assertEquals(Long.valueOf(1L), recipeReactiveRepository.count().block());
    }
}