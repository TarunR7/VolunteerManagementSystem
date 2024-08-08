package events;

import java.util.LinkedHashMap;

public interface EventRegistration {
    void registerForEvent(EventId event, LinkedHashMap<EventId, Event> eventMap);
    void cancelRegistration(EventId event);
}
