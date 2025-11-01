package trxsh.ontop.theUltimateSMPLib.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.enchant.CustomEnchantment;
import trxsh.ontop.theUltimateSMPLib.manager.CustomEnchantmentRegistry;

import java.util.List;
import java.util.Random;

import static trxsh.ontop.theUltimateSMPLib.enchant.CustomEnchantment.*;
import static trxsh.ontop.theUltimateSMPLib.manager.CustomEnchantmentRegistry.getCustomEnchantments;

public class Anvil implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();

        ItemStack first = inv.getFirstItem();
        ItemStack second = inv.getSecondItem();

        if(first == null || second == null)
            return;

        if(first.getType() == Material.AIR || second.getType() == Material.AIR)
            return;

        ItemStack result = first.clone();

        List<CustomEnchantment> firstEnchants = getCustomEnchantments(first);
        List<CustomEnchantment> secondEnchants = getCustomEnchantments(second);

        if (firstEnchants.isEmpty() && secondEnchants.isEmpty()) return;

        for(CustomEnchantment enchant : secondEnchants) {
            if(firstEnchants.stream().anyMatch(enchantment -> enchantment.getEnchantKey().equalsIgnoreCase(enchant.getEnchantKey()))) {
                int levelA = getLevel(enchant, first);
                int levelB = getLevel(enchant, second);

                if(enchant.conflictsWith(result.getEnchantments().keySet().stream().toList()) || enchant.conflictsWithCustom(firstEnchants))
                    continue;

                if (levelA == levelB && enchant.canApplyTo(result) ) {
                    setLevel(enchant, result, Math.min(levelA + 1, enchant.getMaxLevel()));
                } else if(enchant.canApplyTo(result)) {
                    setLevel(enchant, result, Math.max(levelA, levelB));
                }

                event.getView().setRepairCost(Math.max(levelA, levelB) * 2);
            } else if (enchant.canApplyTo(result)) {
                int levelA = getLevel(enchant, first);
                int levelB = getLevel(enchant, second);

                applyEnchantment(enchant, result);
                setLevel(enchant, result, Math.max(levelA, levelB));

                event.getView().setRepairCost(Math.max(levelA, levelB) * 2);
            }
        }

        event.setResult(result);
    }
}
