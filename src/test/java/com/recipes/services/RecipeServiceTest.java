package com.recipes.services;

import com.recipes.model.Ingredient;
import com.recipes.model.Recipe;
import com.recipes.model.User;
import com.recipes.repositories.IngredientRepository;
import com.recipes.repositories.RecipeRepository;
import com.recipes.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IngredientRepository ingredientRepository;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeService(recipeRepository, userRepository, ingredientRepository);
    }



    @Test
    void testAddRecipe_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Recipe recipe = new Recipe();
        List<Ingredient> ingredients = new ArrayList<>();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.addRecipe(userId, recipe, ingredients));
        verify(userRepository, times(1)).findById(userId);
        verify(ingredientRepository, never()).save(any(Ingredient.class));
        verify(userRepository, never()).save(any(User.class));
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void testUpdateRecipe() {
        // Arrange
        Long userId = 1L;
        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        User user = new User();
        user.setUser_id(userId);
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1L);
        existingRecipe.setUser(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(updatedRecipe.getId())).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Recipe result = recipeService.updateRecipe(userId, updatedRecipe);

        // Assert
        assertEquals(existingRecipe, result);
        assertEquals(updatedRecipe.getName(), existingRecipe.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findById(updatedRecipe.getId());
        verify(recipeRepository, times(1)).save(existingRecipe);
    }

    @Test
    void testUpdateRecipe_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(userId, updatedRecipe));
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, never()).findById(anyLong());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void testUpdateRecipe_RecipeNotFound() {
        // Arrange
        Long userId = 1L;
        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        User user = new User();
        user.setUser_id(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(updatedRecipe.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(userId, updatedRecipe));
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findById(updatedRecipe.getId());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void testRemoveRecipe() {
        // Arrange
        Long userId = 1L;
        Long recipeId = 1L;
        User user = new User();
        user.setUser_id(userId);
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setUser(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Act
        recipeService.removeRecipe(userId, recipeId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).deleteById(recipeId);
    }

    @Test
    void testRemoveRecipe_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Long recipeId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.removeRecipe(userId, recipeId));
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, never()).findById(anyLong());
        verify(recipeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testRemoveRecipe_RecipeNotFound() {
        // Arrange
        Long userId = 1L;
        Long recipeId = 1L;
        User user = new User();
        user.setUser_id(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.removeRecipe(userId, recipeId));
        verify(userRepository, times(1)).findById(userId);
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetAllRecipes() {
        // Arrange
        List<Recipe> recipes = new ArrayList<>();
        when(recipeRepository.findAll()).thenReturn(recipes);

        // Act
        List<Recipe> result = recipeService.getAllRecipes();

        // Assert
        assertEquals(recipes, result);
        verify(recipeRepository, times(1)).findAll();
    }



    @Test
    void testSearchRecipes_UserNotFound() {
        // Arrange
        Long userId = 1L;
        boolean vegetarian = true;
        int servings = 2;
        List<String> includeIngredients = new ArrayList<>();
        List<String> excludeIngredients = new ArrayList<>();
        String searchText = "pizza";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                recipeService.searchRecipes(userId, vegetarian, servings, includeIngredients, excludeIngredients, searchText));
        verify(userRepository, times(1)).findById(userId);
    }
}
