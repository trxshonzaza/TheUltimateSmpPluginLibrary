package trxsh.ontop.theUltimateSMPLib.item;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomItemStack {
    private  int model = -1;
    private String itemKey;
    private final ItemStack itemInstance;
    private Map<String, PlayerAction> actions = new HashMap<>();

    public CustomItemStack(String itemKey) {
        this.itemKey = itemKey;
        itemInstance = tagItem(createItem().clone());
    }

    public abstract ItemStack createItem();


    /*
    YOU MUST USE THIS METHOD AFTER YOU ARE DONE CREATING YOUR ITEM!!!!!!
    WITHOUT THIS, THE PLUGIN WILL NOT BE ABLE TO IDENTIFY UNIQUE ITEMS!
     */
    private ItemStack tagItem(ItemStack item) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "custom_item_key");
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, itemKey);
        });

        return item;
    }

    public void addAction(String key, PlayerAction task) {
        actions.put(key, task);
    }

    public void runAction(String key, Player player) {
        PlayerAction action = actions.get(key);

        if (action != null) {
            action.run(player);
        } else {
            throw new RuntimeException("Action defined is null");
        }
    }

    public Map<String, PlayerAction> getActions() {
        return actions;
    }

    public String getItemKey() {
        return itemKey;
    }

    public int getModel() {
        return model;
    }

    public ItemStack getItemInstance() {
        return itemInstance;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
