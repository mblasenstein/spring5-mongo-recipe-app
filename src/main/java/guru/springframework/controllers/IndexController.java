package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
public class IndexController {

    private final RecipeService recipeService;
    private static Logger log = LoggerFactory.getLogger(IndexController.class);

    public IndexController(RecipeService recipeService) {

        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        log.debug("Getting index page");

        Set<Recipe> recipes = recipeService.getRecipes();
        model.addAttribute("recipes", recipes);

        return "index";
    }
}
