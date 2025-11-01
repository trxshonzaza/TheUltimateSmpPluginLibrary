package trxsh.ontop.theUltimateSMPLib.enchant;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExampleEnchantment extends CustomEnchantment {
    public ExampleEnchantment(String displayName, String enchantKey, int maxLevel, int defaultLevel) {
        super(displayName, enchantKey, maxLevel, defaultLevel);
    }

    @Override
    public boolean canApplyTo(ItemStack s) {
        return true;
    }

    @Override
    public boolean conflictsWithCustom(List<? extends CustomEnchantment> s) {
        return false;
    }

    @Override
    public boolean conflictsWith(List<? extends Enchantment> s) {
        return false;
    }
}
