package trxsh.ontop.theUltimateSMPLib.other;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Raycast helper.
 */
public class Raycasting {

    /**
     * raycasts all living entities based on start and direction and max distance. excludes shooter
     * @param start
     * @param direction
     * @param maxDistance
     * @param shooter
     * @return
     */
    public static List<LivingEntity> raycastEntities(Location start, Vector direction, double maxDistance, LivingEntity shooter) {
        World world = start.getWorld();
        Location currentLocation = start.clone();

        List<LivingEntity> total = new ArrayList<>();

        for (double distance = 0; distance < maxDistance; distance += 0.1) {
            currentLocation.add(direction);

            Collection<Entity> nearbyEntities = world.getNearbyEntities(currentLocation, 2, 2, 2);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity && entity != shooter) {
                    if(!total.contains(entity))
                        total.add((LivingEntity) entity);
                }
            }

            if(currentLocation.getBlock().getType() != Material.AIR)
                break;
        }

        return total;
    }

    /**
     * raycasts to a solid block, returns ray hit location.
     * @param start
     * @param direction
     * @param maxDistance
     * @return
     */
    public static Location raycast(Location start, Vector direction, double maxDistance) {
        Location currentLocation = start.clone();

        for (double distance = 0; distance < maxDistance; distance += 0.1) {
            currentLocation.add(direction);

            if(currentLocation.getBlock().getType() != Material.AIR)
                break;
        }

        return currentLocation;
    }
}
