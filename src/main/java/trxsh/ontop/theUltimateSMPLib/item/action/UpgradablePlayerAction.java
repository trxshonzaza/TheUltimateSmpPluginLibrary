package trxsh.ontop.theUltimateSMPLib.item.action;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface UpgradablePlayerAction {
    void run(Player player, int i);
}
