package trxsh.ontop.theUltimateSMPLib.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.enchant.CustomEnchantment;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEnchantmentRegistry {
    private static final Map<String, CustomEnchantment> enchants = new HashMap<>();

    public static void register(CustomEnchantment enchant) {
        if(enchants.containsKey(enchant.getEnchantKey())) throw new IllegalArgumentException("attempted to register an enchantment with a similar key to " + get(enchant.getEnchantKey()).getClass().getName() + "!!! This is not allowed.");
        else {
            enchants.put(enchant.getEnchantKey(), enchant);
        }
    }

    public static CustomEnchantment get(String enchantKey) {
        return enchants.get(enchantKey);
    }

    public static Map<String, CustomEnchantment> getEnchants() {
        return enchants;
    }

    public static List<CustomEnchantment> getCustomEnchantments(ItemStack s) {
        List<CustomEnchantment> enchantments = new ArrayList<>();

        if (s == null || !s.hasItemMeta()) return enchantments;
        ItemMeta meta = s.getItemMeta();

        for(CustomEnchantment enchantment : getEnchants().values()) {
            if(meta.getPersistentDataContainer().has(enchantment.getEnchantNamespacedKey())) {
                enchantments.add(enchantment);
            }
        }

        return enchantments;
    }

    public static <T extends CustomEnchantment> CustomEnchantment getCustomEnchantment(ItemStack s, Class<T> type) {
        if (s == null || !s.hasItemMeta()) return null;
        ItemMeta meta = s.getItemMeta();

        for(CustomEnchantment enchantment : getEnchants().values()) {
            if(type.isInstance(enchantment)) {
                if(meta.getPersistentDataContainer().has(enchantment.getEnchantNamespacedKey())) {
                    return enchantment;
                }
            }
        }

        return null;
    }

    public static boolean hasCustomEnchantment(ItemStack s) {
        return getCustomEnchantment(s, CustomEnchantment.class) != null;
    }

    public static <T extends CustomEnchantment> boolean hasCustomEnchantment(ItemStack s, Class<T> type) {
        return getCustomEnchantment(s, type) != null;
    }
}
