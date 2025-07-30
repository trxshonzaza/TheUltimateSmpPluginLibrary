package trxsh.ontop.theUltimateSMPLib.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomItemRegistry {
    private static final Map<String, CustomItemStack> items = new HashMap<>();

    public static void register(CustomItemStack item) {
        items.put(item.getItemKey(), item);
    }

    public static CustomItemStack get(String itemKey) {
        return items.get(itemKey);
    }

    public static Map<String, CustomItemStack> getItems() {
        return items;
    }

    public static CustomItemStack identifyItem(ItemStack s) {
        if (s == null || !s.hasItemMeta()) return null;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "custom_item_key");
        String itemKey = s.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (itemKey == null) return null;

        return getItems().get(itemKey);
    }

    public static boolean isCustomItem(ItemStack s) {
        return getItems().values().stream().anyMatch(cs -> cs.getItemInstance().isSimilar(s));
    }
}
