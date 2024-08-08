package events;

import users.Organizer;


import static json.CustomJson.addJsonToJsonArray;
import java.time.LocalDateTime;
// import java.time.Period;
// import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;



public class OnlineEvent extends Event {
    private String attendeeUrl;
    private String volunteerUrl;
    public static String ONLINE_FILE = "app/src/main/files/events/online_events.json";
    public static Scanner INPUT = new Scanner(System.in);
    // Constructors

    public OnlineEvent(){}
    public OnlineEvent(EventId id, String name, Organizer organizer, int maxParticipants, int maxVolunteers,
                       String contactNumber, String contactEmail, String description,
                       LocalDateTime start, LocalDateTime end, String attendeeUrl, String volunteerUrl) {
        super(id, name, organizer, maxParticipants, maxVolunteers, contactNumber, contactEmail,
                description, start, end);
        this.attendeeUrl = attendeeUrl;
        this.volunteerUrl = volunteerUrl;
    }

    // Getter methods for URLs
    public String getAttendeeUrl() {
        return attendeeUrl;
    }

    public String getVolunteerUrl() {
        return volunteerUrl;
    }

    // Implementation of abstract methods
    @Override
    public void displayDetails() {
        System.out.println("Online Event Details:");
        super.displayDetails();
        System.out.println("Attendee URL: " + getAttendeeUrl());
        System.out.println("Volunteer URL: " + getVolunteerUrl());
    }

    @Override
    public void writeToJSON() {
       addJsonToJsonArray(ONLINE_FILE, this, this.getClass());
    }

    public static OnlineEvent inputOnlineEvent(Organizer organizer){
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
        System.out.print("Give the URL for your attendees: ");
        String attendeeUrl = INPUT.nextLine();
        System.out.print("Give the URL for your volunteers: ");
        String volunteerUrl = INPUT.nextLine();
        return new OnlineEvent(EventId.getUniqueEventId(OnlineEvent.ONLINE_FILE), name, organizer, maxParticipants,
                maxVolunteers,
                contactNumber, contactEmail, description, startDateTime, endDateTime,
                attendeeUrl, volunteerUrl);
    }
}