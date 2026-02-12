package trxsh.ontop.theUltimateSMPLib.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Item helper.
 */
public class ItemHelper {
    /**
     * creates an item.
     * @param material
     * @param name
     * @param lore
     * @return
     */
    public static ItemStack createQuickItem(Material material, TextComponent name, List<TextComponent> lore) {
        ItemStack stack = new ItemStack(material);

        stack.editMeta(meta -> {
            meta.customName(name);
            meta.lore(lore);
        });

        return stack;
    }

    /**
     * recovers a ban entry from a suitable item stack.
     * See: createItemStackFromBanEntry(BanEntry entry)
     * @param stack the stack to get the suitable ban entry from
     * @return
     */
    public static BanEntry<?> getBanEntryFromStack(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        NamespacedKey banKey = new NamespacedKey(Main.getInstance(), "ban_uuid");

        if(meta == null) return null;
        if(!meta.getPersistentDataContainer().has(banKey)) return null;

        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(meta.getPersistentDataContainer().get(banKey, PersistentDataType.STRING))));
        return Bukkit.getBanList(BanListType.PROFILE).getBanEntry(player.getPlayerProfile());
    }

    /**
     * creates a custom item from a ban entry. useful for unban GUI's
     * @param entry the ban entry to use
     * @return
     */
    public static ItemStack createItemStackFromBanEntry(BanEntry<PlayerProfile> entry) {
        if(entry == null)
            return null;
        else if(entry.getBanTarget().getId() == null)
            return null;

        NamespacedKey banKey = new NamespacedKey(Main.getInstance(), "ban_uuid");

        OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getBanTarget().getId());

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        assert meta != null;
        meta.setOwningPlayer(player);
        meta.displayName(Component.text(player.getName()));
        meta.lore(Collections.singletonList(Component.text("Was banned on " + entry.getCreated())));

        meta.getPersistentDataContainer().set(banKey, PersistentDataType.STRING, player.getUniqueId().toString());

        skull.setItemMeta(meta);

        return skull;
    }
}
