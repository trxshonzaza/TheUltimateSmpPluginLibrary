package trxsh.ontop.theUltimateSMPLib.other;

import org.bukkit.Bukkit;
import trxsh.ontop.theUltimateSMPLib.Main;

public class Async {
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), runnable);
    }
}
