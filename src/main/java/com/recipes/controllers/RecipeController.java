package com.recipes.controllers;

import com.recipes.model.Recipe;
import com.recipes.model.User;
import com.recipes.services.RecipeService;
import com.recipes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@Validated
public class RecipeController {
    private final RecipeService recipeService;
    private final UserService userService;


    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe addRecipe(@RequestBody Recipe recipe, @RequestParam Long id) {
        User user = userService.getUserByUserId(id)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + id));
        recipe.setUser(user);
        return recipeService.addRecipe(id, recipe, recipe.getIngredients());
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable Long id,  @RequestBody @Valid Recipe updatedRecipe) {
        return recipeService.updateRecipe(id,  updatedRecipe);
    }

    @DeleteMapping("/{userId}/{recipeId}")
    public void deleteRecipe(@PathVariable Long userId,@PathVariable Long recipeId) {
        recipeService.removeRecipe(userId, recipeId);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    // Additional endpoints and methods
}
