package users;

import static json.CustomJson.addJsonToJsonArray;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

import common.Location;
import eventManagementSystem.UserOperation;
import events.*;

public class Organizer extends User implements EventCreation, UserOperation<Organizer>{

    public static final String ORGANIZER_FILE = "app/src/main/files/users/organizers.json";

    private String contactNumber;
    private String contactMail;

    public Organizer() {
    }

    public Organizer(UserId id, String username, int age, Location address, String contactNumber, String contactMail,
            LinkedHashSet<EventId> organizedEvents) {
        super(id, username, age, address, organizedEvents);
        this.contactNumber = contactNumber;
        this.contactMail = contactMail;
    }

    @Override
    public void writeToJSON() {
        addJsonToJsonArray(ORGANIZER_FILE, this, this.getClass());
    }

    private static final Scanner INPUT = new Scanner(System.in);

    @Override
    public Event createEvent(LinkedHashMap<EventId, Event> eventMap) {
        System.out.print("Will this be an online or offline event (O/F): ");
        final char ch = INPUT.nextLine().toLowerCase().charAt(0);
        Event newEvent = null;
        do {
            switch (ch) {
                case 'o':
                    newEvent = OnlineEvent.inputOnlineEvent(this);
                    break;
                case 'f':
                    newEvent = OfflineEvent.inputOfflineEvent(this);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (newEvent == null);
        newEvent.writeToJSON();
        this.events.add(newEvent.getId());
        return newEvent;
    }

    @Override
    public Organizer signUp(Organizer organizer) {
        System.out.println("Enter name: ");
        String name = INPUT.nextLine();
        System.out.println("Enter age: ");
        int age = Integer.parseInt(INPUT.nextLine());
        System.out.println("Enter contact mail : ");
        String mail = INPUT.nextLine();
        System.out.println("Enter contact phone number : ");
        String number = INPUT.nextLine();
        Location address = Location.getLocation("your");
        organizer = new Organizer(UserId.getUniqueUserId(ORGANIZER_FILE),
                name, age, address, number, mail, new LinkedHashSet<EventId>());
        organizer.writeToJSON();
        return organizer;
    }

    @Override
    public void cancelEvent(EventId eventId) {
        events.remove(eventId);
    }

    @Override
    public void displayDetails() {
        System.out.println("Id: " + id);
        System.out.println("Organizer: " + username);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println("Contact Mail: " + contactMail);
        System.out.println("Contact Number: " + contactNumber);
    }

    public static String getOrganizerFile() {
        return ORGANIZER_FILE;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactMail() {
        return contactMail;
    }

    public static Scanner getInput() {
        return INPUT;
    }

}
