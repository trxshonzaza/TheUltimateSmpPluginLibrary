package trxsh.ontop.theUltimateSMPLib.event.simple;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventAction<T extends Event> {
    void run(T event);
}
