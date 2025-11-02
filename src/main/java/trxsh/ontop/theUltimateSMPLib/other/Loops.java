package trxsh.ontop.theUltimateSMPLib.other;

import org.bukkit.scheduler.BukkitRunnable;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Loops helper.
 */
public class Loops {
    private static final Map<String, BukkitRunnable> loops = new HashMap<>();

    public static void addLoop(String key, BukkitRunnable task, int delay, boolean async) {
        if (async) {
            task.runTaskTimerAsynchronously(Main.getInstance(), 10, delay);
        } else {
            task.runTaskTimer(Main.getInstance(), 10, delay);
        }

        loops.put(key, task);
    }

    public static void loopUntil(Runnable task, int delay, Supplier<Boolean> condition) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(condition.get()) {
                    cancel();
                    return;
                }

                task.run();
            }
        }.runTaskTimer(Main.getInstance(), 0, delay);
    }

    public static void loopUntil(Runnable task, Runnable onComplete, int delay, Supplier<Boolean> condition) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(condition.get()) {
                    cancel();
                    onComplete.run();
                    return;
                }

                task.run();
            }
        }.runTaskTimer(Main.getInstance(), 0, delay);
    }

    public static void loopForTicks(Runnable task, int delay, int maxTicks) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if(ticks >= maxTicks) {
                    cancel();
                    return;
                }

                task.run();
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0, delay);
    }

    public static void loopForTicks(Runnable task, Runnable onComplete, int delay, int maxTicks) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if(ticks >= maxTicks) {
                    cancel();
                    onComplete.run();
                    return;
                }

                task.run();
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0, delay);
    }

    public static void endLoop(String key) {
        loops.get(key).cancel();
        loops.remove(key);
    }
}
