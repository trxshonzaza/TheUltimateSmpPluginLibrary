package trxsh.ontop.theUltimateSMPLib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import trxsh.ontop.theUltimateSMPLib.event.gui.GuiChecker;
import trxsh.ontop.theUltimateSMPLib.gui.callback.GuiCallback;
import trxsh.ontop.theUltimateSMPLib.util.ItemHelper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gui wrapper for inventories.
 */
public abstract class Gui {
    protected final Component name;
    protected final Inventory inventory;
    protected final int slots;

    private UUID id;

    private final Map<Integer, GuiCallback> callbacks = new ConcurrentHashMap<>();

    private boolean autoRemove = false;

    public Gui(Component guiName, int slots) {
        this.name = guiName;
        this.id = UUID.randomUUID();
        this.slots = slots;
        this.inventory = Bukkit.createInventory(null, slots, guiName);
    }

    /**
     * registers the GUI to listen for clicks. executes onClick and callbacks.
     */
    public void register() {
        GuiChecker.listenForClicks(this);
    }

    /**
     * adds a callback to run a block of code when a specific slot is clicked.
     * See: GuiCallback.java
     * {@snippet :
     * addCallback(10, (gui, event) -> {
     *             // run code here
     *         });
     * }
     * @param slot
     * @param callback
     */
    public void addCallback(int slot, GuiCallback callback) {
        if(callbacks.containsKey(slot)) Bukkit.getLogger().warning("replacing an existing callback on slot " + slot);
        callbacks.put(slot, callback);
    }

    public void removeCallback(int slot) {
        if(callbacks.containsKey(slot)) callbacks.remove(slot);
        else Bukkit.getLogger().warning("slot passed does not have a callback: " + slot);
    }

    public void clearCallbacks() {
        callbacks.clear();
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public abstract void onClick(InventoryClickEvent event);
    public abstract void onClose(InventoryCloseEvent event);

    public void removeItem(ItemStack item) {
        inventory.remove(item);
    }

    public void removeMaterial(Material mat) {
        inventory.remove(mat);
    }

    public void removeItems(ItemStack... items) {
        inventory.removeItem(items);
    }

    public void removeItemAt(int slot) {
        if(inventory.getItem(slot) != null) {
            inventory.getItem(slot).setType(Material.AIR);
        } else {
            Bukkit.getLogger().warning("gui tried to remove a null item. (slot " + slot + ")");
        }
    }

    public void fillWithGlassPane() {
        for(int i = 0; i < slots; i++) {
            ItemStack item = getItem(i);

            if(item == null) {
                addItemAt(ItemHelper.createQuickItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" "), null), i);
            } else if(item.getType() == Material.AIR) {
                addItemAt(ItemHelper.createQuickItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" "), null), i);
            }
        }
    }

    /**
     * finds the first slot that the specified itemstack is in
     * @param s
     * @return
     */
    public @Nullable Integer findItem(ItemStack s) {
        for(int i = 0; i < slots; i++) {
            ItemStack item = getItem(i);

            if(item != null)
                if(item.isSimilar(s))
                    return i;
        }

        return null;
    }

    /**
     * finds the first slot that the specified material is in
     * @param m
     * @return
     */
    public @Nullable Integer findMaterial(Material m) {
        for(int i = 0; i < slots; i++) {
            ItemStack item = getItem(i);

            if(item != null)
                if(item.getType() == m)
                    return i;
        }

        return null;
    }

    public void addItem(ItemStack... items) {
        inventory.addItem(items);
    }

    public void addItemAt(ItemStack item, int slot) {
        inventory.setItem(slot, item);
    }

    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    public UUID getId() {
        return id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * autoRemove removes the GUI from listening when exited.
     * @param autoRemove
     */
    public void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
    }

    public boolean shouldAutoRemove() {
        return autoRemove;
    }

    public void unregister() {
        GuiChecker.remove(this);
    }

    public Map<Integer, GuiCallback> getCallbacks() {
        return callbacks;
    }
}
