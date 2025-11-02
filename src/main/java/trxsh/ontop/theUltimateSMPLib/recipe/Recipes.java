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

/**
 * Recipe helper.
 */
public class Recipes {
    private static final List<NamespacedKey> recipeKeys = new ArrayList<>();

    /**
     * Create a recipe. does not add it to bukkit recipes.
     * Recipe object must be Material, ItemStack, RecipeChoice.ExactCohice, or RecipeChoice.MaterialChoice
     * {@snippet :
     * createRecipe(new NamespacedKey("a_key"), resultStack, List.of(
     *                 Pair.of('A', recipeObject1),
     *                 Pair.of('B', recipeObject2),
     *                 Pair.of('C', recipeObject3)
     *         ), "ABA", "BCB", "CAC");
     * }
     * @param key
     * @param result
     * @param shapeMap
     * @param shape
     * @return
     */
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

    /**
     * Create a recipe. adds it to bukkit recipes.
     * Recipe object must be Material, ItemStack, RecipeChoice.ExactChoice, or RecipeChoice.MaterialChoice
     * {@snippet :
     * createThenAddRecipe(new NamespacedKey("a_key"), resultStack, List.of(
     *                 Pair.of('A', recipeObject1),
     *                 Pair.of('B', recipeObject2),
     *                 Pair.of('C', recipeObject3)
     *         ), "ABA", "BCB", "CAC");
     * }
     * @param key
     * @param result
     * @param shapeMap
     * @param shape
     * @return
     */
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
