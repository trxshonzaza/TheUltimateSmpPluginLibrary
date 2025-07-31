package trxsh.ontop.theUltimateSMPLib.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.manager.CustomItemRegistry;

public abstract class Upgradable extends CustomItemStack {

    public Upgradable(String itemKey) {
        super(itemKey);
    }

    public abstract void onLevelChanged(ItemStack stack, int level);

    public static void setLevel(ItemStack item, int level) {
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), "custom_item_level");
        NamespacedKey itemKey = new NamespacedKey(Main.getInstance(), "custom_item_key");

        if(!item.getItemMeta().getPersistentDataContainer().has(itemKey)) return;

        String itemKeyString = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);

        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        });

        CustomItemStack customItemStack = CustomItemRegistry.get(itemKeyString);

        if (customItemStack instanceof Upgradable upgradable) {
            upgradable.onLevelChanged(item, level);
        }
    }

    public static int getLevel(ItemStack item) {
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), "custom_item_level");

        if (!item.hasItemMeta()) return 0;

        Integer level = item.getItemMeta().getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    public static void incrementLevel(ItemStack item) {
        setLevel(item, getLevel(item) + 1);
    }
}
