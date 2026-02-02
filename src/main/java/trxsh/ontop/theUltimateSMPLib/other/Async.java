package trxsh.ontop.theUltimateSMPLib.other;

import org.bukkit.Bukkit;
import trxsh.ontop.theUltimateSMPLib.Main;

/**
 * Runs async code in runnable.
 * {@snippet :
 * Async.run(() -> {
 *     // your code here
 * })
 * }
 */
public class Async {
    /**
     * runs a task asynchronously
     * @param runnable
     */
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), runnable);
    }
}
