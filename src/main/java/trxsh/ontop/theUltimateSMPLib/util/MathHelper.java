package trxsh.ontop.theUltimateSMPLib.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Math helper.
 * typically for finding specific points or vectors that may require a bit of math.
 */
public class MathHelper {
    public static Location getPointInCircle(Location initial, int smoothness, int i, double r, TrigFunction function, ApplyTo... apply) {
        double angle = (2 * Math.PI * i) / smoothness;
        double offset = 0;

        Location end = initial.clone();

        switch (function) {
            case Cos -> offset = r * Math.cos(angle);
            case Sin -> offset = r * Math.sin(angle);
        }

        for(ApplyTo a : apply) {
            switch (a) {
                case X -> end.add(offset, 0, 0);
                case Y -> end.add(0, offset, 0);
                case Z -> end.add(0, 0, offset);
            }
        }

        return end;
    }

    public static List<Location> blocksInRadius(Location initial, int r, int yRadius) {
        List<Location> locationList = new ArrayList<>();

        for (int x = -r; x <= r; x++) {
            for (int y = -yRadius; y <= yRadius; y++) {
                for (int z = -r; z <= r; z++) {
                    locationList.add(initial.clone().add(x, y, z));
                }
            }
        }

        return locationList;
    }

    public static List<Location> generateRandomPointsOutsideSphere(Location center, int radius, int count) {
        List<Location> points = new ArrayList<>();

        for(int i = 0; i < count; i++) {
            double phi = Math.acos(1 - 2 * Math.random());
            double theta = 2 * Math.PI * Math.random();

            double x = radius * Math.sin(phi) * Math.cos(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(phi);

            Location loc = center.clone().add(x, y, z);
            points.add(loc);
        }

        return points;
    }

    public static List<Location> generateRandomPointsOutsideCircle(Location center, double radius, int count) {
        List<Location> points = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI * i) / count;

            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            Location point = new Location(center.getWorld(), center.getX() + xOffset, center.getY() + 1, center.getZ() + zOffset);
            points.add(point);
        }

        return points;
    }

    public static List<Location> generateRandomPointsInSphere(Location center, int radius, int count) {
        List<Location> points = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double theta = new Random().nextDouble() * 2 * Math.PI;
            double phi = new Random().nextDouble() * Math.PI;
            double r = radius * new Random().nextDouble();

            double x = center.getX() + r * Math.sin(phi) * Math.cos(theta);
            double y = center.getY() + r * Math.sin(phi) * Math.sin(theta);
            double z = center.getZ() + r * Math.cos(phi);

            points.add(new Location(center.getWorld(), x, y, z));
        }

        return points;
    }

    public static List<Location> generateRandomPointsInCircle(Location center, int radius, int count) {
        List<Location> points = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double randomRadius = radius * Math.sqrt(Math.random());
            double angle = radius * Math.PI * Math.random();

            double x = center.getX() + randomRadius * Math.cos(angle);
            double z = center.getZ() + randomRadius * Math.sin(angle);

            Location point = new Location(center.getWorld(), x, center.getY(), z);

            points.add(point);
        }

        return points;
    }

    public static Vector getDirectionBetween(Location e1, Location e2) {
        return e1.clone().toVector().subtract(e2.clone().toVector()).normalize();
    }

    public enum ApplyTo {
        X,
        Y,
        Z
    }

    public enum TrigFunction {
        Sin,
        Cos
    }
}
