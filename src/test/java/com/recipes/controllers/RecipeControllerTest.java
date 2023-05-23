package com.recipes.controllers;

import com.recipes.model.Recipe;
import com.recipes.model.User;
import com.recipes.services.RecipeService;
import com.recipes.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecipeControllerTest {
    @Mock
    private RecipeService recipeService;
    @Mock
    private UserService userService;

    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(recipeService, userService);
    }

    @Test
    void testAddRecipe() {
        // Arrange
        Long userId = 1L;
        Recipe recipe = new Recipe();
        User user = new User();
        user.setUser_id(userId);
        when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));
        when(recipeService.addRecipe(userId, recipe, recipe.getIngredients())).thenReturn(recipe);

        // Act
        Recipe result = recipeController.addRecipe(recipe, userId);

        // Assert
        assertEquals(recipe, result);
        verify(userService, times(1)).getUserByUserId(userId);
        verify(recipeService, times(1)).addRecipe(userId, recipe, recipe.getIngredients());
    }

    @Test
    void testAddRecipe_InvalidUser() {
        // Arrange
        Long userId = 1L;
        Recipe recipe = new Recipe();
        when(userService.getUserByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeController.addRecipe(recipe, userId));
        verify(userService, times(1)).getUserByUserId(userId);
        verify(recipeService, never()).addRecipe(anyLong(), any(Recipe.class), anyList());
    }

    @Test
    void testUpdateRecipe() {
        // Arrange
        Long recipeId = 1L;
        Recipe updatedRecipe = new Recipe();
        when(recipeService.updateRecipe(recipeId, updatedRecipe)).thenReturn(updatedRecipe);

        // Act
        Recipe result = recipeController.updateRecipe(recipeId, updatedRecipe);

        // Assert
        assertEquals(updatedRecipe, result);
        verify(recipeService, times(1)).updateRecipe(recipeId, updatedRecipe);
    }

    @Test
    void testDeleteRecipe() {
        // Arrange
        Long userId = 1L;
        Long recipeId = 1L;

        // Act
        recipeController.deleteRecipe(userId, recipeId);

        // Assert
        verify(recipeService, times(1)).removeRecipe(userId, recipeId);
    }

    @Test
    void testSearchRecipes() {
        // Arrange
        Long userId = 1L;
        boolean vegetarian = true;
        int servings = 2;
        List<String> includeIngredients = new ArrayList<>();
        List<String> excludeIngredients = new ArrayList<>();
        String searchText = "pizza";

        User user = new User();
        user.setUser_id(userId);
        when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));
        List<Recipe> recipes = new ArrayList<>();
        when(recipeService.searchRecipes(userId, vegetarian, servings, includeIngredients, excludeIngredients, searchText))
                .thenReturn(recipes);

        // Act
        List<Recipe> result = recipeController.searchRecipes(userId, vegetarian, servings, includeIngredients, excludeIngredients, searchText);

        // Assert
        assertEquals(recipes, result);
//        verify(userService, times(1)).getUserByUserId(userId);
        verify(recipeService, times(1))
                .searchRecipes(userId, vegetarian, servings, includeIngredients, excludeIngredients, searchText);
    }


    @Test
    void testGetAllRecipes() {
        // Arrange
        List<Recipe> recipes = new ArrayList<>();
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        // Act
        List<Recipe> result = recipeController.getAllRecipes();

        // Assert
        assertEquals(recipes, result);
        verify(recipeService, times(1)).getAllRecipes();
    }
}
