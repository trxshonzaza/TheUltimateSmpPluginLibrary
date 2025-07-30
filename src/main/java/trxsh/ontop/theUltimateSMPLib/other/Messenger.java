package trxsh.ontop.theUltimateSMPLib.other;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class Messenger {
    public static void sendMessage(HumanEntity entity, TextComponent text) {
        entity.sendMessage(text);
    }

    public static void sendErrorMessage(HumanEntity entity, TextComponent text) {
        entity.sendMessage(text.color(NamedTextColor.RED));
    }

    public static void sendSuccessMessage(HumanEntity entity, TextComponent text) {
        entity.sendMessage(text.color(NamedTextColor.GREEN));
    }

    public static void sendColorMessage(TextColor color, HumanEntity entity, TextComponent text) {
        entity.sendMessage(text.color(color));
    }
}
