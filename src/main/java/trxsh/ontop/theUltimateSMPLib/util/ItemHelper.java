package trxsh.ontop.theUltimateSMPLib.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;

public class ItemHelper {
    public static ItemStack createQuickItem(Material material, TextComponent name, List<TextComponent> lore) {
        ItemStack stack = new ItemStack(material);

        stack.editMeta(meta -> {
            meta.customName(name);
            meta.lore(lore);
        });

        return stack;
    }

    public static BanEntry<?> getBanEntryFromStack(ItemStack stack) {
        if(stack.getItemMeta() == null)
            return null;

        if(!stack.getItemMeta().hasDisplayName())
            return null;

        String name = stack.getItemMeta().getDisplayName();
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        return Bukkit.getBanList(BanListType.PROFILE).getBanEntry(player.getPlayerProfile());
    }

    public static ItemStack createItemStackFromBanEntry(BanEntry<PlayerProfile> entry) {
        if(entry == null)
            return null;
        else if(entry.getBanTarget().getId() == null)
            return null;

        Player player = Bukkit.getPlayer(entry.getBanTarget().getId());

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        assert meta != null;
        meta.setOwningPlayer(player);
        meta.setDisplayName(player.getName());
        meta.setLore(Collections.singletonList("Was banned on " + entry.getCreated()));

        skull.setItemMeta(meta);

        return skull;
    }
}
