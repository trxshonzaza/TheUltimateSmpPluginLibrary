package trxsh.ontop.theUltimateSMPLib.event.simple;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import trxsh.ontop.theUltimateSMPLib.Main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles simple events.
 */
public class SimpleEventHandler implements Listener {

    static Map<String, String> disallowedEvents = new HashMap<>();
    static {
        disallowedEvents.put("PlayerLoginEvent", "deprecated. use PlayerConnectionValidateLoginEvent or PlayerServerFullCheckEvent");
        disallowedEvents.put("PlayerPreLoginEvent", "deprecated");
        disallowedEvents.put("PlayerChatTabCompleteEvent", "deprecated");
        disallowedEvents.put("PlayerBucketFishEvent", "use PlayerBucketEntityEvent");
        disallowedEvents.put("PlayerChatEvent", "forces main thread blocking. use AsyncChatEvent");
        disallowedEvents.put("AsyncPlayerChatEvent", "deprecated. use AsyncChatEvent");
        disallowedEvents.put("PlayerPickupItemEvent", "deprecated. use EntityPickupItemEvent");
        disallowedEvents.put("PlayerRecipeBookClickEvent", "deprecated for removal");
        disallowedEvents.put("PlayerSpawnChangeEvent", "deprecated for removal, use PlayerSetSpawnEvent");
        disallowedEvents.put("PlayerSignOpenEvent", "deprecated for removal, use PlayerOpenSignEvent");
        disallowedEvents.put("EntityKnockbackByEntityEvent", "deprecated for removal, use paper equivalent");
        disallowedEvents.put("EntityKnockbackEvent", "deprecated for removal, use paper equivalent");
        disallowedEvents.put("PrepareInventoryResultEvent", "deprecated");
    }

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

    /**
     * initalizes the event wrapper. do not run this.
     * @param plugin
     */
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
            if(!disallowedEvents.containsKey(clazz.getSimpleName())) {
                plugin.getServer().getPluginManager()
                        .registerEvent(clazz, this, EventPriority.NORMAL, eventExecutor, Main.getInstance());

                System.out.println("registering simple event: " + clazz.getName());
            } else {
                System.err.println("skipping disallowed event " + clazz.getName() + ", " + disallowedEvents.get(clazz.getSimpleName()));
            }
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
