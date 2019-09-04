package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.NotesRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Component
@Profile("default")
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private NotesRepository notesRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private Map<String, UnitOfMeasure> unitsOfMeasure = new HashMap<>();

    public DataLoader(RecipeRepository recipeRepository, CategoryRepository categoryRepository, NotesRepository notesRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.notesRepository = notesRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.save(loadGuacamoleRecipe());
        recipeRepository.save(loadTacoRecipe());
    }

    private UnitOfMeasure getUom(String description) {
        if (unitsOfMeasure.size() == 0) {
            Iterable<UnitOfMeasure> uoms = unitOfMeasureRepository.findAll();
            for (UnitOfMeasure uom : uoms) {
                unitsOfMeasure.put(uom.getDescription(), uom);
            }
        }
        return unitsOfMeasure.get(description);
    }

    private Recipe loadGuacamoleRecipe() {
        Recipe recipe = new Recipe();
        recipe.setDescription("Perfect Guacamole");

        Ingredient avocados = new Ingredient();
        avocados.setAmount(BigDecimal.valueOf(2));
        avocados.setUom(getUom("Each"));
        avocados.setDescription("avocados");
        avocados.setRecipe(recipe);
        recipe.getIngredients().add(avocados);
        
        Ingredient salt = new Ingredient();
        salt.setAmount(BigDecimal.valueOf(.5));
        salt.setUom(getUom("Teaspoon"));
        salt.setDescription("kosher salt");
        salt.setRecipe(recipe);
        recipe.getIngredients().add(salt);
        
        Ingredient juice = new Ingredient();
        juice.setAmount(BigDecimal.valueOf(1));
        juice.setUom(getUom("Tablespoon"));
        juice.setDescription("fresh lime or lemon juice");
        juice.setRecipe(recipe);
        recipe.getIngredients().add(juice);
        
        Ingredient onion = new Ingredient();
        onion.setAmount(BigDecimal.valueOf(2));
        onion.setUom(getUom("Tablespoon"));
        onion.setDescription("minced red onion or thinly sliced green onion");
        onion.setRecipe(recipe);
        recipe.getIngredients().add(onion);

        Ingredient chiles = new Ingredient();
        chiles.setAmount(BigDecimal.valueOf(1));
        chiles.setUom(getUom("Each"));
        chiles.setDescription("serrano chiles, stems and seeds removed, minced");
        chiles.setRecipe(recipe);
        recipe.getIngredients().add(chiles);
        
        Ingredient cilantro = new Ingredient();
        cilantro.setAmount(BigDecimal.valueOf(2));
        cilantro.setUom(getUom("Tablespoon"));
        cilantro.setDescription("cilantro (leaves and tender stems), finely chopped");
        cilantro.setRecipe(recipe);
        recipe.getIngredients().add(cilantro);
        
        Ingredient pepper = new Ingredient();
        pepper.setAmount(BigDecimal.valueOf(1));
        pepper.setUom(getUom("Dash"));
        pepper.setDescription("freshly ground pepper");
        pepper.setRecipe(recipe);
        recipe.getIngredients().add(pepper);
        
        Ingredient tomato = new Ingredient();
        tomato.setAmount(BigDecimal.valueOf(.5));
        tomato.setUom(getUom("Each"));
        tomato.setDescription("tomato, seeds and pulp removed, chopped");
        tomato.setRecipe(recipe);
        recipe.getIngredients().add(tomato);
        
        recipe.setDirections(
                "1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                        "\n" +
                        "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                        "\n" +
                        "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                        "\n" +
                        "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                        "\n" +
                        "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                        "\n" +
                        "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                        "\n" +
                        "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                        "\n" +
                        "Variations\n" +
                        "\n" +
                        "For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                        "\n" +
                        "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries (see our Strawberry Guacamole).\n" +
                        "\n" +
                        "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                        "\n" +
                        "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great."
        );

        Notes notes = new Notes();
        notes.setRecipeNotes(
                "Be careful handling chiles if using. Wash your hands thoroughly after handling and do not touch your "
                + "eyes or the area near your eyes with your hands for several hours");
        notes.setRecipe(recipe);
        recipe.setNotes(notes);

        Category mexican = categoryRepository.findByDescription("Mexican").get();

        recipe.getCategories().add(mexican);
        recipe.setPrepTime(10);
        recipe.setServings(4);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setSource("simplyrecipes.com");
        recipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");

        return recipe;
    }

    private Recipe loadTacoRecipe() {

        Recipe recipe = new Recipe();
        recipe.setDescription("Grilled Chicken Tacos");

        Ingredient chiliPowder = new Ingredient();
        chiliPowder.setAmount(BigDecimal.valueOf(2));
        chiliPowder.setUom(getUom("Tablespoon"));
        chiliPowder.setDescription("ancho chili powder");
        chiliPowder.setRecipe(recipe);
        recipe.getIngredients().add(chiliPowder);
        
        Ingredient oregano = new Ingredient();
        oregano.setAmount(BigDecimal.valueOf(1));
        oregano.setUom(getUom("Teaspoon"));
        oregano.setDescription("dried oregano");
        oregano.setRecipe(recipe);
        recipe.getIngredients().add(oregano);
        
        Ingredient cumin = new Ingredient();
        cumin.setAmount(BigDecimal.valueOf(1));
        cumin.setUom(getUom("Teaspoon"));
        cumin.setDescription("dried cumin");
        cumin.setRecipe(recipe);
        recipe.getIngredients().add(cumin);

        Ingredient sugar = new Ingredient();
        sugar.setAmount(BigDecimal.valueOf(1));
        sugar.setUom(getUom("Teaspoon"));
        sugar.setDescription("sugar");
        sugar.setRecipe(recipe);
        recipe.getIngredients().add(sugar);
        
        Ingredient salt = new Ingredient();
        salt.setAmount(BigDecimal.valueOf(.5));
        salt.setUom(getUom("Teaspoon"));
        salt.setDescription("salt");
        salt.setRecipe(recipe);
        recipe.getIngredients().add(salt);

        Ingredient garlic = new Ingredient();
        garlic.setAmount(BigDecimal.valueOf(1));
        garlic.setUom(getUom("Clove"));
        garlic.setDescription("garlic, finely chopped");
        garlic.setRecipe(recipe);
        recipe.getIngredients().add(garlic);

        Ingredient zest = new Ingredient();
        zest.setAmount(BigDecimal.valueOf(1));
        zest.setUom(getUom("Tablespoon"));
        zest.setDescription("finely grated orange zest");
        zest.setRecipe(recipe);
        recipe.getIngredients().add(zest);

        Ingredient juice = new Ingredient();
        juice.setAmount(BigDecimal.valueOf(3));
        juice.setUom(getUom("Tablespoon"));
        juice.setDescription("fresh-squeezed orange juice");
        juice.setRecipe(recipe);
        recipe.getIngredients().add(juice);

        Ingredient oliveOil = new Ingredient();
        oliveOil.setAmount(BigDecimal.valueOf(2));
        oliveOil.setUom(getUom("Tablespoon"));
        oliveOil.setDescription("olive oil");
        oliveOil.setRecipe(recipe);
        recipe.getIngredients().add(oliveOil);

        Ingredient chicken = new Ingredient();
        chicken.setAmount(BigDecimal.valueOf(6));
        chicken.setUom(getUom("Each"));
        chicken.setDescription("skinless, boneless chicken thighs");
        chicken.setRecipe(recipe);
        recipe.getIngredients().add(chicken);

        Ingredient tortillas = new Ingredient();
        tortillas.setAmount(BigDecimal.valueOf(8));
        tortillas.setUom(getUom("Each"));
        tortillas.setDescription("small corn tortillas");
        tortillas.setRecipe(recipe);
        recipe.getIngredients().add(tortillas);

        Ingredient arugula = new Ingredient();
        arugula.setAmount(BigDecimal.valueOf(3));
        arugula.setUom(getUom("Cup"));
        arugula.setDescription("packed baby arugula");
        arugula.setRecipe(recipe);
        recipe.getIngredients().add(arugula);

        Ingredient avocados = new Ingredient();
        avocados.setAmount(BigDecimal.valueOf(2));
        avocados.setUom(getUom("Each"));
        avocados.setDescription("medium ripe avocados, sliced");
        avocados.setRecipe(recipe);
        recipe.getIngredients().add(avocados);

        Ingredient radishes = new Ingredient();
        radishes.setAmount(BigDecimal.valueOf(4));
        radishes.setUom(getUom("Each"));
        radishes.setDescription("thinly sliced");
        radishes.setRecipe(recipe);
        recipe.getIngredients().add(radishes);

        Ingredient cherryTomatoes = new Ingredient();
        cherryTomatoes.setAmount(BigDecimal.valueOf(.5));
        cherryTomatoes.setUom(getUom("Pint"));
        cherryTomatoes.setDescription("cherry tomatoes, halved");
        cherryTomatoes.setRecipe(recipe);
        recipe.getIngredients().add(cherryTomatoes);

        Ingredient redOnion = new Ingredient();
        redOnion.setAmount(BigDecimal.valueOf(.25));
        redOnion.setUom(getUom("Each"));
        redOnion.setDescription("red onion, thinly sliced");
        redOnion.setRecipe(recipe);
        recipe.getIngredients().add(redOnion);

        Ingredient cilantro = new Ingredient();
        cilantro.setAmount(BigDecimal.valueOf(1));
        cilantro.setUom(getUom("Each"));
        cilantro.setDescription("Roughly chopped cilantro");
        cilantro.setRecipe(recipe);
        recipe.getIngredients().add(cilantro);
        
        Ingredient sourCream = new Ingredient();
        sourCream.setAmount(BigDecimal.valueOf(.5));
        sourCream.setUom(getUom("Cup"));
        sourCream.setDescription("sour cream");
        sourCream.setRecipe(recipe);
        recipe.getIngredients().add(sourCream);

        Ingredient lime = new Ingredient();
        lime.setAmount(BigDecimal.valueOf(1));
        lime.setUom(getUom("Each"));
        lime.setDescription("lime");
        lime.setRecipe(recipe);
        recipe.getIngredients().add(lime);

        recipe.setDirections(
            "1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                    "\n" +
                    "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                    "\n" +
                    "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                    "\n" +
                    "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                    "\n" +
                    "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                    "\n" +
                    "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                    "\n" +
                    "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges."
        );

        Category mexican = categoryRepository.findByDescription("Mexican").get();

        recipe.getCategories().add(mexican);
        recipe.setPrepTime(20);
        recipe.setCookTime(15);
        recipe.setServings(6);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setSource("simplyrecipes.com");
        recipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");

        return recipe;
    }
}
