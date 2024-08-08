package users;

import static json.CustomJson.addJsonToJsonArray;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

import common.Location;
import eventManagementSystem.UserOperation;
import events.Event;
import events.EventId;
import events.EventRegistration;
import exceptions.ConflictingParticipationException;

public class Volunteer extends User implements EventRegistration, UserOperation<Volunteer>{

    public static final String VOLUNTEER_FILE = "app/src/main/files/users/volunteers.json";
    public static final Scanner INPUT = new Scanner(System.in);
    private String contactNumber;

    public Volunteer() {
    }

    public Volunteer(UserId id, String username, int age, Location address, String contactNumber,
            LinkedHashSet<EventId> organizedEvents) {
        super(id, username, age, address, organizedEvents);
        this.contactNumber = contactNumber;
    }

    @Override
    public void cancelRegistration(EventId event) {
        events.remove(event);
    }

    @Override
    public void registerForEvent(EventId eventId, LinkedHashMap<EventId, Event> eventMap) {
        if (isParticipationConflict(eventId, eventMap))
            throw new ConflictingParticipationException();
        events.add(eventId);
    }

    @Override
    public void displayDetails() {
        System.out.println("Id: " + id);
        System.out.println("Volunteer: " + username);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println();
    }

    @Override
    public void writeToJSON() {
        addJsonToJsonArray(VOLUNTEER_FILE, this, this.getClass());
    }

    public static String getVolunteerFile() {
        return VOLUNTEER_FILE;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    @Override
    public Volunteer signUp(Volunteer volunteer){
        System.out.println("Enter name: ");
        String name = INPUT.nextLine();
        System.out.println("Enter age: ");
        int age = Integer.parseInt(INPUT.nextLine());
        Location address = Location.getLocation("your");
        System.out.println("Give your number: ");
        String number = INPUT.nextLine();
        volunteer = new Volunteer(UserId.getUniqueUserId(VOLUNTEER_FILE),
                name, age, address, number, new LinkedHashSet<EventId>());
        volunteer.writeToJSON();
        return volunteer;
    }

}
