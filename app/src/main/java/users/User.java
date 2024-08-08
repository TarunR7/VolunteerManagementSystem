package users;
import events.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import common.Displayable;
import common.Location;


abstract public class User implements EventInteract, Displayable{

    protected UserId id;
    protected String username;
    protected int age;
    protected Location address;
    protected LinkedHashSet<EventId> events;

    protected User(){};

    public User(UserId id, String username, int age, Location address, LinkedHashSet<EventId> events){
        this.id = id;
        this.username = username;
        this.age = age;
        this.address = address;
        this.events = events;
    }

    @Override
    public void displayEvents(LinkedHashMap<EventId, Event> eventMap) {
        for(EventId id: events){
            eventMap.get(id).displayDetails();
        }
    }

    abstract public void displayDetails();

    public boolean isOverlapping(LocalDateTime startFirst, LocalDateTime endFirst, LocalDateTime startSecond, LocalDateTime endSecond){
        return (startFirst.isBefore(endSecond) && endFirst.isAfter(startSecond));
    }


    public boolean isParticipationConflict(EventId nextEventId, LinkedHashMap<EventId, Event> eventMap){
        LocalDateTime nextEventStart = eventMap.get(nextEventId).getStart();
        LocalDateTime nextEventEnd = eventMap.get(nextEventId).getEnd();

        for(EventId committedEventId : events){
            Event committedEvent =  eventMap.get(committedEventId);
            LocalDateTime committedStart = committedEvent.getStart();
            LocalDateTime committedEnd = committedEvent.getEnd();
            if(isOverlapping(committedStart, committedEnd, nextEventStart, nextEventEnd))
                return true;
        }
        return false;
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public Location getAddress() {
        return address;
    }

    public LinkedHashSet<EventId> getEvents() {
        return events;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof User)) return false;
        User otherUser = (User) other;
        return this.getId().equals(otherUser.getId());
    }

    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }

}