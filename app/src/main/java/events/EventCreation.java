package events;

import java.util.LinkedHashMap;

public interface EventCreation {
    Event createEvent(LinkedHashMap<EventId,Event> eventMap);
    void cancelEvent(EventId eventId);
}
