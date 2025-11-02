package trxsh.ontop.theUltimateSMPLib.other;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Cooldown wrapper
 * Allows cooldowns to be placed on items.
 * See: CustomItemStack.java
 */
public class Cooldowns {
    private static final Map<String, Map<UUID, Cooldown>> cooldowns = new HashMap<>();

    /**
     * adds a cooldown based on key
     * @param key
     * @param playerId
     * @param durationMillis
     */
    public static void add(String key, UUID playerId, long durationMillis) {
        cooldowns
                .computeIfAbsent(key, k -> new HashMap<>())
                .put(playerId, new Cooldown(System.currentTimeMillis() + durationMillis));
    }

    /**
     * checks if a player has a cooldown based on key
     * @param key
     * @param playerId
     * @return
     */
    public static boolean hasCooldown(String key, UUID playerId) {
        Map<UUID, Cooldown> map = cooldowns.get(key);
        if (map == null) return false;

        Cooldown cd = map.get(playerId);
        return cd != null && !cd.isExpired();
    }

    /**
     * gets remaining duration of a players cooldown based on key
     * @param key
     * @param playerId
     * @return
     */
    public static long getRemainingDuration(String key, UUID playerId) {
        Map<UUID, Cooldown> map = cooldowns.get(key);
        if (map == null) return 0;

        Cooldown cd = map.get(playerId);
        return cd != null ? cd.getRemainingTimeMillis() : 0;
    }

    public static void remove(String key, UUID playerId) {
        Map<UUID, Cooldown> map = cooldowns.get(key);
        if (map != null) {
            map.remove(playerId);
        }
    }

    public static String formatMillis(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;

        return minutes + "m " + seconds + "s";
    }

    private static class Cooldown {
        private final long endTimeMillis;

        Cooldown(long endTimeMillis) {
            this.endTimeMillis = endTimeMillis;
        }

        boolean isExpired() {
            return System.currentTimeMillis() >= endTimeMillis;
        }

        long getRemainingTimeMillis() {
            return Math.max(0, endTimeMillis - System.currentTimeMillis());
        }
    }
}
