package trxsh.ontop.theUltimateSMPLib.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import trxsh.ontop.theUltimateSMPLib.item.CustomItemStack;
import trxsh.ontop.theUltimateSMPLib.manager.CustomItemRegistry;

public class ItemRegistryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(commandSender.isOp() && commandSender instanceof Player player) {
            Inventory inv = Bukkit.createInventory(null, 54, "Items");

            for(CustomItemStack itemStack : CustomItemRegistry.getItems().values()) {
                inv.addItem(itemStack.createItem());
            }

            player.openInventory(inv);
            return true;
        }

        return false;
    }
}
