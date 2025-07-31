package trxsh.ontop.theUltimateSMPLib.util;

import org.bukkit.Location;

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
