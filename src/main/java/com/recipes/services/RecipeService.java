package com.recipes.services;


import com.recipes.model.Ingredient;
import com.recipes.model.Recipe;
import com.recipes.model.User;
import com.recipes.repositories.IngredientRepository;
import com.recipes.repositories.RecipeRepository;
import com.recipes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Recipe addRecipe(Long id, Recipe recipe, List<Ingredient> ingredients) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            recipe.setUser(user);
            for (Ingredient ingredient : ingredients) {
                recipe.getIngredients().add(ingredient);
                ingredientRepository.save(ingredient);
            }
            user.getRecipes().add(recipe); // Add recipe to user's list of recipes
            userRepository.save(user); // Save the user to update the relationship
            return recipe;
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }



    public Recipe updateRecipe(Long userId, Recipe updatedRecipe) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Recipe> recipeOptional = recipeRepository.findById(updatedRecipe.getId());
            if (recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();
                if (recipe.getUser().equals(user)) {
                    recipe.setName(updatedRecipe.getName());

                    // Update other recipe attributes as needed
                    return recipeRepository.save(recipe);
                } else {
                    throw new RuntimeException("Recipe not found with id: " + updatedRecipe.getId());
                }
            } else {
                throw new RuntimeException("Recipe not found with id: " + updatedRecipe.getId());
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    public void removeRecipe(Long userId, Long recipeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
            if (recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();
                if (recipe.getUser().equals(user)) {
                    recipeRepository.deleteById(recipeId);
                } else {
                    throw new RuntimeException("Recipe not found with id: " + recipeId);
                }
            } else {
                throw new RuntimeException("Recipe not found with id: " + recipeId);
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
    public List<Recipe> searchRecipes(Long userId, boolean vegetarian, int servings, List<String> includeIngredients,
                                      List<String> excludeIngredients, String searchText) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getRecipes().stream()
                    .filter(recipe -> recipe.isVegetarian() == vegetarian)
                    .filter(recipe -> recipe.getServings() == servings)
                    .filter(recipe -> includeIngredients.stream().allMatch(recipe.getIngredients()::contains))
                    .filter(recipe -> excludeIngredients.stream().noneMatch(recipe.getIngredients()::contains))
                    .filter(recipe -> recipe.getInstructions().contains(searchText))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

}
