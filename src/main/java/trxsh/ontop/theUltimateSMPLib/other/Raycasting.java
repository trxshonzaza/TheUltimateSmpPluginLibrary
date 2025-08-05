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

public class Raycasting {
    public static List<LivingEntity> raycastEntities(Location start, Vector direction, double maxDistance, Player shooter) {
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
