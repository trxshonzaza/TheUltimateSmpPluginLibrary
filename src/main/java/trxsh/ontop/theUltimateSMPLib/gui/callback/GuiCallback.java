package trxsh.ontop.theUltimateSMPLib.gui.callback;

import org.bukkit.event.inventory.InventoryClickEvent;
import trxsh.ontop.theUltimateSMPLib.gui.Gui;

import java.util.function.Consumer;

public interface GuiCallback {
    void accept(Gui o, InventoryClickEvent e);
}
