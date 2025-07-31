package trxsh.ontop.theUltimateSMPLib.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityUtil {
    public static Player getClosestPlayer(Entity entity, double minDistance) {
        Player closest = null;
        double closestDistanceSquared = Double.MAX_VALUE;

        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            if(entity.getUniqueId() != target.getUniqueId()) {
                double distanceSquared = entity.getLocation().distanceSquared(target.getLocation());

                if (distanceSquared < closestDistanceSquared && distanceSquared <= minDistance) {
                    closestDistanceSquared = distanceSquared;
                    closest = target;
                }
            }
        }

        return closest;
    }
}
