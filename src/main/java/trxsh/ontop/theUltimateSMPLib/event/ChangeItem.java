package trxsh.ontop.theUltimateSMPLib.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import trxsh.ontop.theUltimateSMPLib.manager.CustomItemRegistry;

public class ChangeItem implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSwitch(PlayerItemHeldEvent event) {
        int previousSlot = event.getPreviousSlot();
        ItemStack previousStack = event.getPlayer().getInventory().getItem(previousSlot);

        if(previousStack != null) {
            if(CustomItemRegistry.isCustomItem(previousStack)) {
                if (CustomItemRegistry.identifyItem(previousStack).onUnequip(event.getPlayer(), false)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack stack = event.getItemDrop().getItemStack();

        if(CustomItemRegistry.isCustomItem(stack)) {
            if(CustomItemRegistry.identifyItem(stack).onUnequip(event.getPlayer(), true)) {
                event.setCancelled(true);
            }
        }
    }
}
