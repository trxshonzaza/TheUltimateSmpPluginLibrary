package trxsh.ontop.theUltimateSMPLib.util;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {
    public ItemStack createQuickItem(Material material, TextComponent name, List<TextComponent> lore) {
        ItemStack stack = new ItemStack(material);

        stack.editMeta(meta -> {
            meta.customName(name);
            meta.lore(lore);
        });

        return stack;
    }
}
