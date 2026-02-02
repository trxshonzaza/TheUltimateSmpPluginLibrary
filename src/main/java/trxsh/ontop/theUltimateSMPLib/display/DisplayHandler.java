package trxsh.ontop.theUltimateSMPLib.display;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Handler for all display entity types. See: https://docs.papermc.io/paper/dev/display-entities/
 */
public class DisplayHandler {
    /**
     * creates a display and returns the object of specified type
     * @param world
     * @param location
     * @param operator
     * @param clazz
     * @return the display entity
     * @param <T> the type of display
     */
    public static <T extends Display> T createDisplay( Class<T> clazz, World world, Location location, @Nullable Consumer<T> operator) {
        T display = (T) world.createEntity(location, clazz);

        if (operator != null)
            operator.accept(display);

        return display;
    }

    /**
     * removes a block display entity.
     * @param display
     * @return the location the display despawned at
     */
    public static Location removeDisplay(Display display) {
        Location loc = display.getLocation().clone();
        display.remove();

        return loc;
    }

    /**
     * removes a block display entity based on entity uuid.
     * @param world
     * @param uuid
     * @return the location the display despawned at
     */
    public static Location removeDisplay(World world, UUID uuid) {
        Entity e = world.getEntity(uuid);

        if(e == null) throw new NullPointerException("uuid does not belong to an entity in specified world");
        if(e instanceof Display display) {
            Location loc = display.getLocation().clone();
            display.remove();

            return loc;
        } else throw new IllegalArgumentException("uuid does not belong to a display entity");
    }

    /**
     * transforms the specified display using a transformation matrix.
     * @param display
     * @param matrix
     */
    public static void transformDisplay(Display display, Matrix4f matrix) {
        display.setTransformationMatrix(matrix);
    }

    /**
     * transforms the specified display using a transformation object.
     * @param display
     * @param transform
     */
    public static void transformDisplay(Display display, Transformation transform) {
        display.setTransformation(transform);
    }

    /**
     * teleports the display
     * @param display
     * @param newLocation
     * @param teleportTickDuration the amount of seconds to bring the display to the new location (interpolation)
     */
    public static void teleportDisplay(Display display, Location newLocation, int teleportTickDuration) {
        display.setTeleportDuration(teleportTickDuration);
        display.teleport(newLocation);
    }
}
