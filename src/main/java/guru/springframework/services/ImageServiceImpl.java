package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private static Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
        public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    Byte[] byteObjects = new Byte[0];
                    try {
                        byteObjects = new Byte[file.getBytes().length];

                        int i = 0;

                        for (byte b : file.getBytes()) {
                            byteObjects[i++] = b;
                        }

                        recipe.setImage(byteObjects);

                    } catch (IOException e) {
                        log.error("Error occurred", e);
                        e.printStackTrace();
                    }

                    return recipe;
                });

        recipeReactiveRepository.save(recipeMono.block()).block();

        return Mono.empty();
    }
}
