package trxsh.ontop.theUltimateSMPLib.event.simple;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void init(Plugin plugin) {
        if(Main.initalizedSimpleEvent) throw new RuntimeException("SimpleEvent can only be initalized once. do not call it in your plugin as the main library initalizes it already.");

        Set<Class<? extends Event>> allEvents = new HashSet<>();

        Reflections bukkitReflections = new Reflections("org.bukkit");
        allEvents.addAll(getHandlerListEvents(bukkitReflections));

        Reflections paperReflections = new Reflections("io.papermc.paper");
        allEvents.addAll(getHandlerListEvents(paperReflections));

        plugin.getLogger().info("Found " + allEvents.size() + " unique events");

        EventExecutor eventExecutor = (listener, event) -> {
            onEvent(event);
        };

        allEvents.forEach(clazz -> {
            plugin.getServer().getPluginManager()
                    .registerEvent(clazz, this, EventPriority.NORMAL, eventExecutor, Main.getInstance());

            System.out.println("registering simple event: " + clazz.getName());
        });

        Main.initalizedSimpleEvent = true;
    }

    private Set<Class<? extends Event>> getHandlerListEvents(Reflections reflections) {
        return reflections.getSubTypesOf(Event.class).stream()
                .filter(clazz -> Arrays.stream(clazz.getDeclaredFields())
                        .anyMatch(field -> field.getType().getName().endsWith("HandlerList")))
                .collect(Collectors.toSet());
    }
}
