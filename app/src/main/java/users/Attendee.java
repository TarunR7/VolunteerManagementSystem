package users;

import static json.CustomJson.addJsonToJsonArray;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

import common.Location;
import eventManagementSystem.UserOperation;
import events.*;
import exceptions.ConflictingParticipationException;

public class Attendee extends User implements EventRegistration, UserOperation<Attendee>{

    public Attendee() {
    }

    private static final Scanner INPUT = new Scanner(System.in);

    public static final String ATTENDEE_FILE = "app/src/main/files/users/attendees.json";

    public Attendee(UserId id, String username, int age, Location address,
            LinkedHashSet<EventId> attendedEvents) {
        super(id, username, age, address, attendedEvents);
    }

    @Override
    public void cancelRegistration(EventId eventId) {
        events.remove(eventId);
    }

    @Override
    public void registerForEvent(EventId eventId, LinkedHashMap<EventId, Event> eventMap){
        if (isParticipationConflict(eventId, eventMap))
            throw new ConflictingParticipationException();
        events.add(eventId);
    }

    @Override
    public void displayDetails() {
        System.out.println("Id: " + id);
        System.out.println("Attendee: " + username);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println();
    }

    @Override
    public Attendee signUp(Attendee attendee) {
        System.out.println("Enter name: ");
        String name = INPUT.nextLine();
        System.out.println("Enter age: ");
        int age = Integer.parseInt(INPUT.nextLine());
        Location address = Location.getLocation("your");
        attendee = new Attendee(UserId.getUniqueUserId(ATTENDEE_FILE),
                name, age, address, new LinkedHashSet<EventId>());
        attendee.writeToJSON();
        return attendee;
    }

    @Override
    public void writeToJSON() {
        addJsonToJsonArray(ATTENDEE_FILE, this, this.getClass());
    }

}
