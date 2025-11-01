package trxsh.ontop.theUltimateSMPLib.event.custom;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CustomEvent {
    public String eventId;
    @Nullable
    public List<?> arguments;
    @Nullable
    public Object source;

    public CustomEvent(String eventId) {
        this.eventId = eventId;
    }
}
