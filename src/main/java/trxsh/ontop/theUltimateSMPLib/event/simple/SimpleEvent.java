package trxsh.ontop.theUltimateSMPLib.event.simple;

import org.bukkit.event.Event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleEvent {
    private static final Map<Class<? extends Event>, EventAction<? extends Event>> events = new HashMap<>();

    public static <T extends Event> void registerEvent(Class<T> eventClass, EventAction<T> action) {
        events.put(eventClass, action);
    }

    public static Collection<Map.Entry<Class<? extends Event>, EventAction<? extends Event>>> eventSet() {
        return events.entrySet();
    }
}
