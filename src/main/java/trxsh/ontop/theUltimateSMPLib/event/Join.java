package trxsh.ontop.theUltimateSMPLib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import trxsh.ontop.theUltimateSMPLib.manager.PlayerDataManager;
import trxsh.ontop.theUltimateSMPLib.data.PlayerData;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(PlayerDataManager.get(player.getUniqueId()) == null) {
            PlayerDataManager.add(PlayerData.Create(player));
        }
    }
}
