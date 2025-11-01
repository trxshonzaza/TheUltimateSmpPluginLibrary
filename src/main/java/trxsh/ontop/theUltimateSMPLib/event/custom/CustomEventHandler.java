package trxsh.ontop.theUltimateSMPLib.event.custom;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import trxsh.ontop.theUltimateSMPLib.event.custom.runnable.EventRunnable;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CustomEventHandler {
    private static final List<EventRunnable> registeredListeners = Collections.synchronizedList(new ArrayList<>());

    public static void fire(String eventId, @Nullable Object source, @Nullable List<?> arguments) {
        CustomEvent event = new CustomEvent(eventId);

        event.source = source;
        event.arguments = arguments != null ? arguments : List.of();

        for (EventRunnable runnable : registeredListeners) {
            if (Objects.equals(runnable.getListeningId(), eventId)) {
                Bukkit.getLogger().info("Event " + eventId + " fired!");
                runnable.run(event);
            }
        }
    }

    public static void registerEvent(EventRunnable runnable) {
        if (registeredListeners.stream().anyMatch(r -> r.getListeningId().equals(runnable.getListeningId())))
            throw new IllegalStateException("event listener for " + runnable.getListeningId() + " already registered");

        registeredListeners.add(runnable);
    }

    public static void unregisterEvents(String eventId) {
        registeredListeners.removeIf(r -> Objects.equals(r.getListeningId(), eventId));
    }

    public static void clearAll() {
        registeredListeners.clear();
    }
}
