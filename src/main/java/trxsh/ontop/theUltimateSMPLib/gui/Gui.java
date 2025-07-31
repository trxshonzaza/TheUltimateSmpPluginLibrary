package trxsh.ontop.theUltimateSMPLib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import trxsh.ontop.theUltimateSMPLib.event.GuiChecker;
import trxsh.ontop.theUltimateSMPLib.util.ItemUtil;

public abstract class Gui {
    protected final String name;
    protected final Inventory inventory;
    protected final int slots;

    private boolean autoRemove = false;

    public Gui(String guiName, int slots) {
        this.name = guiName;
        this.slots = slots;
        this.inventory = Bukkit.createInventory(null, slots, guiName);

        GuiChecker.listenForClicks(this);
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
                addItemAt(ItemUtil.createQuickItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" "), null), i);
            } else if(item.getType() == Material.AIR) {
                addItemAt(ItemUtil.createQuickItem(Material.GRAY_STAINED_GLASS_PANE, Component.text(" "), null), i);
            }
        }
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

    public Inventory getInventory() {
        return inventory;
    }

    public void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
    }

    public boolean shouldAutoRemove() {
        return autoRemove;
    }

    public void unregister() {
        GuiChecker.remove(this);
    }
}
