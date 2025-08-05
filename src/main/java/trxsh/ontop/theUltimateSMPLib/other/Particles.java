package trxsh.ontop.theUltimateSMPLib.other;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particles {
    public static void drawParticle(Particle p, Location l) {
        l.getWorld().spawnParticle(p, l.getX(),
                l.getY(), l.getZ(), 1);
    }

    public static void drawParticleBlock(Location l, BlockData data) {
        l.getWorld().spawnParticle(Particle.BLOCK, l.getX(),
                l.getY(), l.getZ(), 1, data);
    }

    public static void drawParticleColor(Location l, Color c) {
        l.getWorld().spawnParticle(Particle.DUST, l.getX(),
                l.getY(), l.getZ(), 1, new Particle.DustOptions(c, 1));
    }

    public static void drawParticleCircle(Particle p, Location l, double r, int co) {
        for (int i = 0; i < co; i++) {
            double angle = (2 * Math.PI * i) / co;

            double xOffset = r * Math.cos(angle);
            double zOffset = r * Math.sin(angle);

            l.getWorld().spawnParticle(p, l.getX() + xOffset,
                    l.getY() + 1, l.getZ() + zOffset, 1);
        }
    }

    public static void drawParticleCircleColor(Location l, Color c, double r, int co) {
        for (int i = 0; i < co; i++) {
            double angle = (2 * Math.PI * i) / co;

            double xOffset = r * Math.cos(angle);
            double zOffset = r * Math.sin(angle);

            l.getWorld().spawnParticle(Particle.DUST, l.getX() + xOffset,
                    l.getY() + 1, l.getZ() + zOffset, 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void drawParticleFilledCircle(Particle p, Location l, double r, int co) {
        for (int i = 0; i < co; i++) {
            double randomRadius = r * Math.sqrt(Math.random());
            double angle = r * Math.PI * Math.random();

            double x = l.getX() + randomRadius * Math.cos(angle);
            double z = l.getZ() + randomRadius * Math.sin(angle);

            Location point = new Location(l.getWorld(), x, l.getY(), z);

            l.getWorld().spawnParticle(p, point, 1);
        }
    }

    public static void drawParticleFilledCircleColor(Location l, Color c, double r, int co) {
        for (int i = 0; i < co; i++) {
            double randomRadius = r * Math.sqrt(Math.random());
            double angle = r * Math.PI * Math.random();

            double x = l.getX() + randomRadius * Math.cos(angle);
            double z = l.getZ() + randomRadius * Math.sin(angle);

            Location point = new Location(l.getWorld(), x, l.getY(), z);

            l.getWorld().spawnParticle(Particle.DUST, point, 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void drawParticleLine(Particle p, Location start, Vector dir, double length, double rateOfChange) {
        Vector startPosition = start.toVector();

        for (double i = 0; i < length; i += rateOfChange) {
            Vector currentPosition = startPosition.clone().add(dir.clone().multiply(i));

            start.getWorld().spawnParticle(p, currentPosition.toLocation(start.getWorld()), 1);
        }
    }

    public static void drawParticleLineColor(Color c, Location start, Vector dir, double length, double rateOfChange) {
        Vector startPosition = start.toVector();

        for (double i = 0; i < length; i += rateOfChange) {
            Vector currentPosition = startPosition.clone().add(dir.clone().multiply(i));

            start.getWorld().spawnParticle(Particle.DUST, currentPosition.toLocation(start.getWorld()), 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void drawParticleLineColorSelf(Player p, Color c, Location start, Vector dir, double length, double rateOfChange) {
        Vector startPosition = start.toVector();

        for (double i = 0; i < length; i += rateOfChange) {
            Vector currentPosition = startPosition.clone().add(dir.clone().multiply(i));

            p.spawnParticle(Particle.DUST, currentPosition.toLocation(start.getWorld()), 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void drawParticleSphere(Particle p, Location center, int points, double radius) {
        for (int i = 0; i < points; i++) {
            double phi = Math.acos(1 - 2 * Math.random());
            double theta = 2 * Math.PI * Math.random();

            double x = radius * Math.sin(phi) * Math.cos(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(phi);

            Location particleLocation = center.clone().add(new Vector(x, y, z));
            center.getWorld().spawnParticle(p, particleLocation, 1);
        }
    }

    public static void drawParticleSphereColor(Location center, Color c, int points, double radius) {
        for (int i = 0; i < points; i++) {
            double phi = Math.acos(1 - 2 * Math.random());
            double theta = 2 * Math.PI * Math.random();

            double x = radius * Math.sin(phi) * Math.cos(theta);
            double y = radius * Math.sin(phi) * Math.sin(theta);
            double z = radius * Math.cos(phi);

            Location particleLocation = center.clone().add(new Vector(x, y, z));
            center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, new Particle.DustOptions(c, 1));
        }
    }

    public static void cone(Location center, Color color, double height, double radius, int points) {
        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;

            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            Location particleLocation = new Location(center.getWorld(), center.getX() + xOffset, center.getY(), center.getZ() + zOffset);
            center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, new Particle.DustOptions(color, 1));

            Vector direction = center.clone().add(0, height, 0).toVector().subtract(particleLocation.toVector()).normalize();
            drawParticleLineColor(color, particleLocation, direction, height, 0.1);
        }
    }

    public static void drawSpecificCircleAngle(Location l, int co, double r, int angle1) {
        for (int i = 0; i < co; i++) {
            double angle = (2 * Math.PI * angle1) / co;

            double xOffset = r * Math.cos(angle);
            double zOffset = r * Math.sin(angle);

            l.getWorld().spawnParticle(Particle.DUST, l.getX() + xOffset,
                    l.getY() + 1, l.getZ() + zOffset, 1);
        }
    }
}

