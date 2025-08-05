package trxsh.ontop.theUltimateSMPLib.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class SimpleEventHandler implements Listener {
    @EventHandler
    public void onEvent(Event event) {
        for(Map.Entry<Class<? extends Event>, EventAction<? extends Event>> events : SimpleEvent.eventSet()) {
            EventAction<? extends Event> action = events.getValue();
            Class<? extends Event> clazz = events.getKey();

            if(clazz.isInstance(event)) {
                callEventAction(action, event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void callEventAction(EventAction<? extends Event> action, Event event) {
        ((EventAction<T>) action).run((T) event);
    }
}
