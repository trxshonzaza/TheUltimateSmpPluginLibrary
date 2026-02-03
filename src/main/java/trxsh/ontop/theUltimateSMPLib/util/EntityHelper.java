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

    /**
     * Gets all specific entity's based on class
     * @param entity the entity to start at (its location)
     * @param entitySearch the class of entity to find
     * @param x the distance to search in blocks in the x axis
     * @param y the distance to search in blocks in the y axis
     * @param z the distance to search in blocks in the z axis
     * @return a list of all entities that are of class type
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<T> getNearbyEntitiesSpecific(Entity entity, Class<? extends Entity> entitySearch, double x, double y, double z) {
        List<T> entities = new ArrayList<>();

        for (Entity e : entity.getNearbyEntities(x, y, z)) {
            if (entitySearch.isInstance(e)) {
                entities.add((T) entitySearch.cast(e));
            }
        }
         return entities;
    }

    /**
     * Gets all entities in an area
     * @param entity the entity to start at (its location)
     * @param x the distance to search in blocks in the x axis
     * @param y the distance to search in blocks in the y axis
     * @param z the distance to search in blocks in the z axis
     * @return a list of all entities that are of class type
     */
    public static List<LivingEntity> getNearbyEntities(Entity entity, double x, double y, double z) {
        List<LivingEntity> entities = new ArrayList<>();

        for(Entity e : entity.getNearbyEntities(x, y, z)) {
            if(e instanceof LivingEntity le) {
                entities.add(le);
            }
        }

        return entities;
    }

    /**
     * fully shows a player
     * @param player the player to show
     */
    public static void showPlayer(Player player) {
        Bukkit.getOnlinePlayers().forEach(entity -> {
            if(entity != player) {
                entity.showPlayer(Main.getInstance(), player);
            }
        });

        hidden.remove(player.getUniqueId());
    }

    /**
     * fully hides a player
     * @param player the player to show
     */
    public static void hidePlayer(Player player) {
        Bukkit.getOnlinePlayers().forEach(entity -> {
            if(entity != player) {
                entity.hidePlayer(Main.getInstance(), player);
            }
        });

        hidden.add(player.getUniqueId());
    }

    /**
     * checks if a player is hidden
     * @param player the player to check
     * @return wether the player is hidden or not
     */
    public static boolean isHidden(Player player) {
        return hidden.contains(player.getUniqueId());
    }

    /**
     * @deprecated this will be removed in order to prefer MathHelper as it uses locations instead of entities
     * @param e1
     * @param e2
     * @return the direction vector from the first entity to the second entity
     */
    @Deprecated(forRemoval = true)
    public static Vector getDirectionBetween(Entity e1, Entity e2) {
        return e1.getLocation().clone().toVector().subtract(e2.getLocation().clone().toVector()).normalize();
    }
}
