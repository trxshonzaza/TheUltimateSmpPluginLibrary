package trxsh.ontop.theUltimateSMPLib.item;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.manager.CustomItemRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class Upgradable extends CustomItemStack {

    private Map<String, UpgradablePlayerAction> actions = new HashMap<>();

    public Upgradable(String itemKey) {
        super(itemKey);

        addUpgradableAction("hheh", (player, i) -> {

        });
    }

    public abstract void onLevelChanged(ItemStack stack, int level, @Nullable Player player);

    public static void setLevel(ItemStack item, int level, @Nullable Player player) {
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), "custom_item_level");
        NamespacedKey itemKey = new NamespacedKey(Main.getInstance(), "custom_item_key");

        if(!item.getItemMeta().getPersistentDataContainer().has(itemKey)) return;

        String itemKeyString = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);

        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        });

        CustomItemStack customItemStack = CustomItemRegistry.get(itemKeyString);

        if (customItemStack instanceof Upgradable upgradable) {
            upgradable.onLevelChanged(item, level, player);
        }
    }

    public static void setLevelNoTag(ItemStack item, int level) {
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), "custom_item_level");

        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        });
    }

    public static int getLevel(ItemStack item) {
        NamespacedKey levelKey = new NamespacedKey(Main.getInstance(), "custom_item_level");

        if (!item.hasItemMeta()) return 0;

        Integer level = item.getItemMeta().getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }

    public void runUpgradableAction(String key, Player player, int i) {
        UpgradablePlayerAction action = actions.get(key);

        if (action != null) {
            action.run(player, i);
        } else {
            throw new RuntimeException("Action defined is null");
        }
    }

    public void addUpgradableAction(String key, UpgradablePlayerAction action) {
        actions.put(key, action);
    }

    public Map<String, UpgradablePlayerAction> getUpgradableActions() {
        return actions;
    }

    public static void incrementLevel(ItemStack item) {
        setLevel(item, getLevel(item) + 1, null);
    }
}
