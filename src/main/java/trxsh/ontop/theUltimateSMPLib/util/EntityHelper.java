package trxsh.ontop.theUltimateSMPLib.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Entity helper.
 */
public class EntityHelper {
    private static final List<UUID> hidden = new ArrayList<>();

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

    public static List<LivingEntity> getNearbyEntities(Entity entity, double x, double y, double z) {
        List<LivingEntity> entities = new ArrayList<>();

        for(Entity e : entity.getNearbyEntities(x, y, z)) {
            if(e instanceof LivingEntity le) {
                entities.add(le);
            }
        }

        return entities;
    }

    public static void showPlayer(Player player) {
        Bukkit.getOnlinePlayers().forEach(entity -> {
            if(entity != player) {
                entity.showPlayer(Main.getInstance(), player);
            }
        });

        hidden.remove(player.getUniqueId());
    }
    public static void hidePlayer(Player player) {
        Bukkit.getOnlinePlayers().forEach(entity -> {
            if(entity != player) {
                entity.hidePlayer(Main.getInstance(), player);
            }
        });

        hidden.add(player.getUniqueId());
    }

    public static boolean isHidden(Player player) {
        return hidden.contains(player.getUniqueId());
    }

    public static Vector getDirectionBetween(Entity e1, Entity e2) {
        return e1.getLocation().clone().toVector().subtract(e2.getLocation().clone().toVector()).normalize();
    }
}
