package trxsh.ontop.theUltimateSMPLib.other;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import trxsh.ontop.theUltimateSMPLib.Main;

/**
 * Chat message helper.
 * See: ConfigManager.java
 */
public class Messenger {
    public static void sendMessage(Player entity, TextComponent text) {
        String prefix = Main.getInstance().getConfigManager().getMessengerPrefix();
        entity.sendMessage(Component.text(prefix).append(text));
    }

    public static void sendErrorMessage(Player entity, TextComponent in) {
        TextComponent text = in.color(NamedTextColor.RED);
        String prefix = Main.getInstance().getConfigManager().getMessengerPrefix();
        entity.sendMessage(Component.text(prefix).append(text));
    }

    public static void sendSuccessMessage(Player entity, TextComponent in) {
        TextComponent text = in.color(NamedTextColor.GREEN);
        String prefix = Main.getInstance().getConfigManager().getMessengerPrefix();
        entity.sendMessage(Component.text(prefix).append(text));
    }

    public static void sendColorMessage(Player entity, TextColor color, TextComponent in) {
        TextComponent text = in.color(color);
        String prefix = Main.getInstance().getConfigManager().getMessengerPrefix();
        entity.sendMessage(Component.text(prefix).append(text));
    }

    public static void space(Player entity) {
        entity.sendMessage(Component.text(" "));
    }
}
