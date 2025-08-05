package trxsh.ontop.theUltimateSMPLib.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import trxsh.ontop.theUltimateSMPLib.other.Messenger;
import trxsh.ontop.theUltimateSMPLib.other.Text;

import java.net.URL;

public class Credits implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(commandSender instanceof Player player) {
            try {
                Messenger.sendColorMessage(player, NamedTextColor.GRAY, Component.text(Text.toSmallText("this smp plugin is brought to you by...")));
                Messenger.sendColorMessage(player, NamedTextColor.GRAY, Component.text("============================"));
                Messenger.space(player);
                Messenger.sendMessage(player, Text.gradientText(Text.toSmallText("!!!!! The Ultimate SMP Plugin Library !!!!!"), Text.textColorToBukkitColor(NamedTextColor.AQUA), Text.textColorToBukkitColor(NamedTextColor.GREEN)));
                Messenger.space(player);
                Messenger.sendColorMessage(player, NamedTextColor.YELLOW, Component.text("Developed by trxsh 2.0").hoverEvent(HoverEvent.showText(Component.text("Click to view my github!").color(NamedTextColor.AQUA))).clickEvent(ClickEvent.openUrl(new URL("https://github.com/trxshonzaza"))));
                Messenger.sendColorMessage(player, NamedTextColor.YELLOW, Component.text("Made to help developers and newbies create SMP plugins easier and faster."));
                Messenger.space(player);
                Messenger.sendColorMessage(player, NamedTextColor.AQUA, Component.text(Text.toSmallText("Click here to view repository!! (may be private.)")).decorate(TextDecoration.UNDERLINED).clickEvent(ClickEvent.openUrl(new URL("https://github.com/trxshonzaza/TheUltimateSmpPluginLibrary"))));
                Messenger.space(player);
                Messenger.space(player);
                Messenger.sendColorMessage(player, NamedTextColor.GRAY, Component.text(Text.toSmallText("also... this message called 5 of the 100+ unique methods in the many classes and utilities in this library... cool right? ;)")).decorate(TextDecoration.BOLD));
            }catch (Exception ignored) { }
        }

        return true;
    }
}
