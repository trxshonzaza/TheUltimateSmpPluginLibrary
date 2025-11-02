package trxsh.ontop.theUltimateSMPLib.item;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.item.action.PlayerAction;

import java.util.HashMap;
import java.util.Map;

/**
 * custom item stack wrapper.
 * See: CustomItemRegistry.java
 */
public abstract class CustomItemStack {
    private int model = -1;
    private String itemKey;
    private final ItemStack itemInstance;
    private Map<String, PlayerAction> actions = new HashMap<>();

    public CustomItemStack(String itemKey) {
        this.itemKey = itemKey;
        this.itemInstance = createItem();

        ItemMeta meta = createItem().getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "custom_item_key"))) {
            throw new IllegalStateException("You must tag your item or else it cannot be identified!!! (use tagItem() when returning your item in your createItem() method for the item " + this.itemKey + "!)");
        }
    }

    /**
     * creates your item. MUST RETURN ITEM WITH TAG.
     * See: tagItem(ItemStack item)
     * @return
     */
    public abstract ItemStack createItem();
    public abstract void onUnequip(Player player, boolean dropped);

    /**
     * YOU MUST USE THIS METHOD AFTER YOU ARE DONE CREATING YOUR ITEM!!!!!!
     * WITHOUT THIS, THE PLUGIN WILL NOT BE ABLE TO IDENTIFY UNIQUE ITEMS!
     * See: CustomItemRegistry.java
     * @param item
     * @return
     */
    public ItemStack tagItem(ItemStack item) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "custom_item_key");
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, itemKey);
        });

        return item;
    }

    /**
     * adds a action based on key to run when called.
     * {@snippet :
     * addAction("a_key", (playerThatRanIt -> {
     *             // code goes here
     *         }));
     * }
     * @param key
     * @param task
     */
    public void addAction(String key, PlayerAction task) {
        actions.put(key, task);
    }

    /**
     * calls action based on key
     * See: addAction(String key, PlayerAction task)
     * @param key
     * @param player
     */
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
