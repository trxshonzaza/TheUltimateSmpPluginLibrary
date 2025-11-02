package trxsh.ontop.theUltimateSMPLib.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;
import trxsh.ontop.theUltimateSMPLib.manager.CustomItemRegistry;

import java.util.function.BiConsumer;

/**
 * Utility to run code if the specific item stack of type if being held in specified player's hand.
 * {@snippet :
 * runIfHolding(player, YourCustomItemStack.class, (equipSlot, customItem) -> {
 *             // your code here
 *         });
 * }
 */
public class CustomItemUtil {
    public static <T extends CustomItemStack> void runIfHolding(Player player, Class<T> itemType, BiConsumer<EquipmentSlot, T> action) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND) {
                ItemStack item = player.getInventory().getItem(slot);

                if (CustomItemRegistry.isCustomItem(item)) {
                    CustomItemStack stack = CustomItemRegistry.identifyItem(item);

                    if (itemType.isInstance(stack)) {
                        action.accept(slot, itemType.cast(stack));
                    }
                }
            }
        }
    }
}
