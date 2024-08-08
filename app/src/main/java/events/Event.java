package events;
import java.util.*;
import users.*;
import common.Displayable;
import exceptions.AlreadyParticipatedException;
import json.JSONConvertable;
import java.time.*;
import java.time.temporal.ChronoUnit;


abstract public class Event implements JSONConvertable, Displayable{
    protected EventId id;
    protected String eventName;
    protected Organizer organizer;
    protected int maxParticipants;
    protected int maxVolunteers;
    protected String contactNumber;
    protected String contactEmail;
    protected String description;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected LinkedHashSet<UserId> registeredAttendees;
    protected LinkedHashSet<UserId> registeredVolunteers;


    protected Event(){}
    // Constructor
    public Event(EventId id, String name, Organizer organizer, int maxParticipants, int maxVolunteers,
                 String contactNumber, String contactEmail, String description,
                 LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.eventName = name;
        this.organizer = organizer;
        this.maxParticipants = maxParticipants;
        this.maxVolunteers = maxVolunteers;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.description = description;
        this.start = start;
        this.end = end;
        this.registeredAttendees = new LinkedHashSet<>();
        this.registeredVolunteers = new LinkedHashSet<>();
    }

    // Getter methods
    public EventId getId() {
        return id;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getMaxVolunteers() {
        return maxVolunteers;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
    public boolean isRegisteredAttendee(UserId attendeeID){
        for(UserId AID: registeredAttendees){
            if(AID.equals(attendeeID)){
                return true;
            }
        }
        return false;
    }
    public boolean isRegisteredVolunteer(UserId volunteerID){
        for(UserId VID: registeredVolunteers){
            if(VID.equals(volunteerID)){
                return true;
            }
        }
        return false;
    }

    public LinkedHashSet<UserId> getRegisteredAttendees() {
        return registeredAttendees;
    }

    public LinkedHashSet<UserId> getRegisteredVolunteers() {
        return registeredVolunteers;
    }

    public void registerVolunteer(UserId id){
        if(isRegisteredVolunteer(id))
        throw new AlreadyParticipatedException();
        if(registeredVolunteers.size() >= maxVolunteers)
        System.out.println("No more volunteers are needed for this event!");
        else{
            registeredAttendees.add(id);
        }
    }

    public void registerAttendee(UserId id){
        if(isRegisteredAttendee(id))
        throw new AlreadyParticipatedException();
        if(registeredAttendees.size() >= maxParticipants)
        System.out.println("No more attendees can attend this event!");
        else{
            registeredAttendees.add(id);
        }
    }

    public void unregisterVolunteer(UserId id){
        if(!registeredVolunteers.contains(id)) System.out.println("Not participating in this event!");
        registeredVolunteers.remove(id);
    }

    public void unregisterAttendee(UserId id){
        if(!registeredAttendees.contains(id)) System.out.println("Not participating in this event!");
        registeredAttendees.remove(id);
    }

    public void notification(UserId id) {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilEvent = ChronoUnit.HOURS.between(now,getStart());
        if (hoursUntilEvent <= 24) {
            System.out.println("Event is within a day!");
            displayDetails();

        }
    }

    public String getEventName(){
        return eventName;
    }

    public void displayDetails(){
        System.out.println("Name: " + getEventName());
        System.out.println("Event ID: " + getId());
        System.out.println("Organizer: " + getOrganizer().getUsername());
        System.out.println("Start Time: " + getStart().toString());
        System.out.println("End Time: " + getEnd().toString());
        System.out.println("Contact Number: " + getContactNumber());
        System.out.println("Contact Email: " + getContactEmail());
        System.out.println("Max Participants: " + getMaxParticipants());
        System.out.println("Max Volunteers: " + getMaxVolunteers());
    }


    @Override
    public boolean equals(Object other){
        if(!(other instanceof Event)) return false;
        Event otherEvent = (Event) other;
        return this.getId().equals(otherEvent.getId());
    }

    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }


    // Abstract methods
}

