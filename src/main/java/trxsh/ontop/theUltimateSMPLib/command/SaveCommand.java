package trxsh.ontop.theUltimateSMPLib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import trxsh.ontop.theUltimateSMPLib.Main;
import trxsh.ontop.theUltimateSMPLib.manager.PlayerDataManager;
import trxsh.ontop.theUltimateSMPLib.data.PlayerData;

public class SaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(commandSender instanceof Player player) {
            PlayerData data = PlayerDataManager.get(player.getUniqueId());

            if(data != null) {
                player.sendMessage("data is valid.");
            } else {
                player.sendMessage("data is null.");
            }
        } else {
            commandSender.sendMessage("you are not a valid entity");
        }

        return true;
    }
}
