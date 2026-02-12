package trxsh.ontop.theUltimateSMPLib.event.custom;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Custom event wrapper
 * See: EventRunnable.java, CustomEventHandler.java
 */
public class CustomEvent {
    public String eventId;
    @Nullable
    public List<?> arguments;
    @Nullable
    public Object source;


    /**
     * creates a new CustomEvent assigned to eventId. 
     * you typically shouldnt instantiate this.
     * See: CustomEventHandler.java
     * @param eventId
     */
    public CustomEvent(String eventId) {
        this.eventId = eventId;
    }
}
