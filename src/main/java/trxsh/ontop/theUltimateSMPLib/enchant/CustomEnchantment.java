package trxsh.ontop.theUltimateSMPLib.enchant;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;
import trxsh.ontop.theUltimateSMPLib.item.Upgradable;
import trxsh.ontop.theUltimateSMPLib.manager.CustomItemRegistry;
import trxsh.ontop.theUltimateSMPLib.util.RomanNumeral;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Custom Enchantment wrapper to allow custom enchantments.
 * See: ExampleEnchantment.java
 */
public abstract class CustomEnchantment {
    public String displayName;
    public String name;
    private String enchantKey;
    private List<Component> bookLore;
    public int maxLevel;
    public int defaultLevel;

    public CustomEnchantment(String displayName, String enchantKey, int maxLevel, int defaultLevel) {
        this.displayName = displayName;
        this.name = displayName.replace(" ", "_");
        this.enchantKey = enchantKey;
        this.maxLevel = maxLevel;
        this.defaultLevel = defaultLevel;
    }

    public CustomEnchantment(CustomEnchantment other) {
        this.displayName = other.displayName;
        this.name = displayName.replace(" ", "_");
        this.enchantKey = other.enchantKey;
        this.maxLevel = other.maxLevel;
        this.defaultLevel = other.defaultLevel;
    }

    public abstract boolean canApplyTo(ItemStack s);
    public abstract boolean conflictsWith(List<Enchantment> s); // static enchantment types
    public abstract boolean conflictsWithCustom(List<Class<? extends CustomEnchantment>> s); // custom enchantment class types

    public void setBookLore(List<Component> bookLore) {
        this.bookLore = bookLore;
    }

    public ItemStack getBook() {
        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        stack.editMeta((itemMeta -> {
            itemMeta.lore(bookLore);
        }));
        return CustomEnchantment.applyEnchantment(this, stack);
    }

    public String getEnchantKey() {
        return enchantKey;
    }

    public NamespacedKey getLevelNamespacedKey() {
        return new NamespacedKey(Main.getInstance(), name + "_level");
    }

    public NamespacedKey getEnchantNamespacedKey() {
        return new NamespacedKey(Main.getInstance(), name + "_key");
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static int getLevel(CustomEnchantment enchantment, ItemStack item) {
        NamespacedKey enchantKey = new NamespacedKey(Main.getInstance(), enchantment.name + "_key");
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), enchantment.name + "_level");

        ItemMeta meta = item.getItemMeta();

        if(!meta.getPersistentDataContainer().has(enchantKey)) {
            Bukkit.getLogger().warning("cannot get enchant level from an item that does not have it (ignore if using a blank item on anvil)");
            return -1;
        }

        return Objects.requireNonNull(meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER),
                "malformed enchant data (missing level)");
    }

    /**
    Sets specifed level to the specified enchantment in the item stack
     */
    public static void setLevel(CustomEnchantment enchantment, ItemStack item, int level) {
        NamespacedKey enchantKey = new NamespacedKey(Main.getInstance(), enchantment.name + "_key");
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), enchantment.name + "_level");

        int finalLevel = Math.min(level, enchantment.maxLevel);

        item.editMeta((itemMeta -> {
            List<Component> itemLore = itemMeta.hasLore() ? new ArrayList<>(itemMeta.lore()) : new ArrayList<>();

            if(!itemMeta.getPersistentDataContainer().has(enchantKey)) throw new IllegalStateException("cannot set enchant level to an item that does not have it");

            int componentIndex = 0;

            for(int i = 0; i < itemLore.size(); i++) {
                String text = PlainTextComponentSerializer.plainText().serialize(itemLore.get(i));

                if(text.contains(enchantment.displayName)) {
                    Bukkit.getLogger().info("enchant index found");

                    componentIndex = i;
                    break;
                }
            }

            itemLore.set(componentIndex,
                    Component.text(enchantment.displayName + " " + (finalLevel == 0 ? "" : RomanNumeral.toRoman(finalLevel))).color(NamedTextColor.GOLD));

            itemMeta.lore(itemLore);
        }));

        item.editMeta((meta -> {
            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        }));
    }

    /*
    Applies specified enchantment to an item stack
     */
    public static ItemStack applyEnchantment(CustomEnchantment enchantment, ItemStack item) {
        NamespacedKey enchantKey = new NamespacedKey(Main.getInstance(), enchantment.name + "_key");
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), enchantment.name + "_level");

        item.editMeta((itemMeta -> {
            List<Component> itemLore = itemMeta.hasLore() ? new ArrayList<>(itemMeta.lore()) : new ArrayList<>();

            if(itemMeta.getPersistentDataContainer().has(enchantKey)) throw new IllegalStateException("cannot apply enchant to an item that already has it");

            itemLore.addFirst(Component.text(enchantment.displayName + " " + (enchantment.defaultLevel == 0 ? "" : RomanNumeral.toRoman(enchantment.defaultLevel))).color(NamedTextColor.GOLD));
            itemMeta.lore(itemLore);
        }));

        item.editMeta(meta -> {
            meta.setEnchantmentGlintOverride(true);

            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, enchantment.defaultLevel);
            meta.getPersistentDataContainer().set(enchantKey, PersistentDataType.STRING, enchantment.enchantKey);
        });

        return item;
    }
}
