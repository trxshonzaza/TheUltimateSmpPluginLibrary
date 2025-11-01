package trxsh.ontop.theUltimateSMPLib.recipe;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class Recipes {
    private static final List<NamespacedKey> recipeKeys = new ArrayList<>();

    public static ShapedRecipe createRecipe(NamespacedKey key, ItemStack result, List<Pair<Character, Object>> shapeMap, String... shape) {
        if(recipeKeys.contains(key)) throw new IllegalCallerException("Tried to make a recipe with the same key");

        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(shape);

        for(Pair<Character, Object> pair : shapeMap) {
            Character c = pair.getLeft();
            Object o = pair.getRight();

            switch(o) {
                case Material mat -> recipe.setIngredient(c, mat);
                case ItemStack s -> recipe.setIngredient(c, s);
                case RecipeChoice.ExactChoice ec -> recipe.setIngredient(c, ec);
                case RecipeChoice.MaterialChoice mc -> recipe.setIngredient(c, mc);
                default -> throw new IllegalStateException("Unexpected value: " + o);
            }
        }

        recipeKeys.add(key);
        return recipe;
    }

    public static ShapedRecipe createThenAddRecipe(NamespacedKey key, ItemStack result, List<Pair<Character, Object>> shapeMap, String... shape) {
        if(recipeKeys.contains(key)) throw new IllegalCallerException("Tried to make a recipe with the same key");

        ShapedRecipe recipe = createRecipe(key, result, shapeMap, shape);

        recipeKeys.add(key);
        Bukkit.addRecipe(recipe);

        return recipe;
    }

    public static void clearAllRecipes() {
        recipeKeys.forEach(Bukkit::removeRecipe);
        recipeKeys.clear();
    }
}
