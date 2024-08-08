package events;

import common.Id;

public class EventId extends Id{
    private static int ID_LENGTH = 11;

    public EventId(){
        super(ID_LENGTH);
    }

    public static EventId getUniqueEventId(String filePath){
        return getUniqueId(filePath, EventId.class);
    }

    public EventId(String id){
        super(id);
    }
}