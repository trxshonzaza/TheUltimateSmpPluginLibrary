package trxsh.ontop.theUltimateSMPLib.enchant;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @deprecated this is only a generic example enchantment class. it should not be used. This may be removed in future versions
 */
@Deprecated
public class ExampleEnchantment extends CustomEnchantment {
    public ExampleEnchantment(String displayName, String enchantKey, int maxLevel, int defaultLevel) {
        super(displayName, enchantKey, maxLevel, defaultLevel);
    }

    @Override
    public boolean canApplyTo(ItemStack s) {
        return true;
    }

    @Override
    public boolean conflictsWithCustom(List<Class<? extends CustomEnchantment>> s) {
        return false;
    }

    @Override
    public boolean conflictsWith(List<Enchantment> s) {
        return false;
    }
}
