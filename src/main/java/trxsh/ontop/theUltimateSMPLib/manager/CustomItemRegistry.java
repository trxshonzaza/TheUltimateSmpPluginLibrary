package trxsh.ontop.theUltimateSMPLib.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom itemstack registry
 */
public class CustomItemRegistry {
    private static final Map<String, CustomItemStack> items = new HashMap<>();

    /**
     * registers a custom item stack to the registry. You should always do this.
     * @param item
     */
    public static void register(CustomItemStack item) {
        if(items.containsKey(item.getItemKey())) throw new IllegalArgumentException("attempted to register an item with a similar key to " + get(item.getItemKey()).getClass().getName() + "!!! This is not allowed.");
        else {
            items.put(item.getItemKey(), item);
        }
    }

    public static CustomItemStack get(String itemKey) {
        return items.get(itemKey);
    }

    public static Map<String, CustomItemStack> getItems() {
        return items;
    }

    /**
     * returns the custom item stack class the item is assigned to
     * @param s
     * @return
     */
    public static CustomItemStack identifyItem(ItemStack s) {
        if (s == null || !s.hasItemMeta()) return null;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "custom_item_key");
        String itemKey = s.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (itemKey == null) return null;

        return getItems().get(itemKey);
    }

    /**
     * returns the custom item stack type (as a class) that the item has
     * @param s
     * @return
     */
    public static Class<? extends CustomItemStack> identifyItemType(ItemStack s) {
        if (s == null || !s.hasItemMeta()) return null;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "custom_item_key");
        String itemKey = s.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (itemKey == null) return null;

        return getItems().get(itemKey).getClass();
    }

    /**
     * returns true if the itemstack is a custom item
     * See: identifyItem(ItemStack s)
     * @param s
     * @return
     */
    public static boolean isCustomItem(ItemStack s) {
        return identifyItem(s) != null;
    }
}
