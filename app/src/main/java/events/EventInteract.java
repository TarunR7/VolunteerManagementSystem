package events;
import java.util.LinkedHashMap;

public interface EventInteract {
    void displayEvents(LinkedHashMap<EventId,Event> eventMap);
}
