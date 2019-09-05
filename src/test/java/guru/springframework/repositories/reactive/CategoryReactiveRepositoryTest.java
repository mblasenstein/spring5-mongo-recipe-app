package guru.springframework.repositories.reactive;

import guru.springframework.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CategoryReactiveRepositoryTest {

    private static final String TEST = "Test";

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void testCategorySave() {

        Category category = new Category();
        category.setDescription(TEST);

        categoryReactiveRepository.save(category).then().block();

        Category fetchedCat = categoryReactiveRepository.findByDescription(TEST).block();

        assertNotNull(fetchedCat);
    }

    @Test
    public void testFindByDescription() {
        Category category = new Category();
        category.setDescription(TEST);

        categoryReactiveRepository.save(category).then().block();

        Category fetchedCat = categoryReactiveRepository.findByDescription(TEST).block();

        assertEquals(TEST, fetchedCat.getDescription());
    }
}