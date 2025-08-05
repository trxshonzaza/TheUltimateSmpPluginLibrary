package trxsh.ontop.theUltimateSMPLib.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {
    public Location getPointInCircle(Location initial, int smoothness, int i, double r, TrigFunction function, ApplyTo apply) {
        double angle = (2 * Math.PI * i) / smoothness;
        double offset = 0;

        Location end = initial.clone();

        switch (function) {
            case Cos -> offset = r * Math.cos(angle);
            case Sin -> offset = r * Math.sin(angle);
        }

        return switch (apply) {
            case X -> end.add(offset, 0, 0);
            case Y -> end.add(0, offset, 0);
            case Z -> end.add(0, 0, offset);
        };
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
