package events;

import common.Location;
import users.Organizer;
import json.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class OfflineEvent extends Event {
    private Location location;
    public static String OFFLINE_FILE = "app/src/main/files/events/offline_events.json";
    private static Scanner INPUT = new Scanner(System.in);
    // Constructors

    public OfflineEvent() {
    }

    public OfflineEvent(EventId id, String name, Organizer organizer, int maxParticipants, int maxVolunteers,
            String contactNumber, String contactEmail, String description,
            LocalDateTime start, LocalDateTime end, Location location) {
        super(id, name, organizer, maxParticipants, maxVolunteers, contactNumber, contactEmail,
                description, start, end);
        this.location = location;
    }

    // Getter method for location
    public Location getLocation() {
        return location;
    }

    // Implementation of abstract methods
    @Override
    public void displayDetails() {
        System.out.println("Offline Event Details:");
        super.displayDetails();
        System.out.println("Location: " + getLocation());
    }

    public static OfflineEvent inputOfflineEvent(Organizer organizer) {
        System.out.print("What is the name of your new event: ");
        String name = INPUT.nextLine();
        System.out.print("Enter max participants: ");
        int maxParticipants = INPUT.nextInt();
        System.out.print("Enter max volunteers: ");
        int maxVolunteers = INPUT.nextInt();
        INPUT.nextLine(); // Consume the newline character
        System.out.print("Enter contact number: ");
        String contactNumber = INPUT.nextLine();
        System.out.print("Enter contact email: ");
        String contactEmail = INPUT.nextLine();
        System.out.print("Enter description: ");
        String description = INPUT.nextLine();
        System.out.print("Enter the starting date(YYYY-mm-dd): ");
        String date = INPUT.nextLine();
        System.out.print("Enter the starting time(HH:mm): ");
        String time = INPUT.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(date + " " + time, formatter);
        System.out.print("Enter the ending date(YYYY-mm-dd): ");
        date = INPUT.nextLine();
        System.out.print("Enter the ending time(HH:mm): ");
        time = INPUT.nextLine();
        LocalDateTime endDateTime = LocalDateTime.parse(date + " " + time, formatter);
        Location eventLocation;
        eventLocation = Location.getLocation("the event's");
        //eventLocation = new Location("The North Pole", 0, 0);
        return new OfflineEvent(EventId.getUniqueEventId(OFFLINE_FILE), name, 
        organizer, maxParticipants, maxVolunteers, contactNumber, contactEmail, 
        description, startDateTime, endDateTime, eventLocation);
    }

    @Override
    public void writeToJSON() {
        CustomJson.addJsonToJsonArray(OFFLINE_FILE, this, this.getClass());
    }
}