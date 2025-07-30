package trxsh.ontop.theUltimateSMPLib.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import trxsh.ontop.theUltimateSMPLib.event.GuiChecker;

public abstract class Gui {
    protected final String name;
    protected final Inventory inventory;
    protected final int slots;
    protected boolean autoRemove = false;

    public Gui(String guiName, int slots) {
        this.name = guiName;
        this.slots = slots;
        this.inventory = Bukkit.createInventory(null, slots, guiName);

        GuiChecker.listenForClicks(this);
    }


    public void addItem(ItemStack... items) {
        inventory.addItem(items);
    }

    public void addItemAt(ItemStack item, int slot) {
        inventory.setItem(slot, item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract void onClick(InventoryClickEvent event);
    public abstract void onClose(InventoryCloseEvent event);

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
