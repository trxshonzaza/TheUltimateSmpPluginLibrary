package trxsh.ontop.theUltimateSMPLib.event.simple;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple event wrapper. allows for bukkit events to be wrapped in a EventAction interface for easy event execution.
 */
public class SimpleEvent {
    private static final Map<Class<? extends Event>, EventAction<? extends Event>> events = new HashMap<>();


    /**
     * registers an event based on bukkit class that runs a runnable interface.
     * {@snippet :
     * registerEvent(PlayerJoinEvent.class, (event -> {
     *             // run code here
     *         }));
     * }
     * @param eventClass
     * @param action
     * @param <T>
     */
    public static <T extends Event> void registerEvent(Class<T> eventClass, EventAction<T> action) {
        events.put(eventClass, action);
    }

    public static Collection<Map.Entry<Class<? extends Event>, EventAction<? extends Event>>> eventSet() {
        return events.entrySet();
    }
}
