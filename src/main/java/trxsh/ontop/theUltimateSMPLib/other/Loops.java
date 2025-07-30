package trxsh.ontop.theUltimateSMPLib.other;

import org.bukkit.scheduler.BukkitRunnable;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void endLoop(String key) {
        loops.get(key).cancel();
    }
}
