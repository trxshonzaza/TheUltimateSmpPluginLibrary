package trxsh.ontop.theUltimateSMPLib.item.action;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerAction {
    void run(Player player);
}
