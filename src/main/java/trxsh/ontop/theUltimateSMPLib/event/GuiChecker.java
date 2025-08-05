package trxsh.ontop.theUltimateSMPLib.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import trxsh.ontop.theUltimateSMPLib.gui.Gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiChecker implements Listener {
    private static final List<Gui> guis = new ArrayList<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        List<Gui> snapshot;
        synchronized (guis) {
            snapshot = new ArrayList<>(guis);
        }

        for (Gui gui : snapshot) {
            if (event.getInventory().equals(gui.getInventory())) {
                gui.onClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        List<Gui> snapshot;
        synchronized (guis) {
            snapshot = new ArrayList<>(guis);
        }

        for(Gui gui : snapshot) {
            if(event.getInventory().equals(gui.getInventory())) {
                gui.onClose(event);

                if(gui.shouldAutoRemove()) {
                    remove(gui);
                }
            }
        }
    }

    public static void listenForClicks(Gui gui) {
        guis.add(gui);
    }

    public static void remove(Gui gui) {
        guis.remove(gui);
    }
}
