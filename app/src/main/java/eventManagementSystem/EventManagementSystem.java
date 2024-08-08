/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package eventManagementSystem;

import static json.CustomJson.MAPPER;
import static json.CustomJson.WRITER;
import static json.CustomJson.writeJsonToFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;

import events.Event;
import events.EventId;
import events.OfflineEvent;
import events.OnlineEvent;
import exceptions.AlreadyParticipatedException;
import exceptions.ConflictingParticipationException;
import exceptions.InvalidLoginException;
import json.CustomJson;
import json.JSONConvertable;
import users.Attendee;
import users.Organizer;
import users.User;
import users.Volunteer;


public class EventManagementSystem {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final LinkedHashSet<OfflineEvent> OFFLINE_EVENTS = CustomJson.objectArrayFromFile(OfflineEvent.OFFLINE_FILE, OfflineEvent.class);
    private static final LinkedHashSet<OnlineEvent> ONLINE_EVENTS = CustomJson.objectArrayFromFile(OnlineEvent.ONLINE_FILE, OnlineEvent.class);
    private static final LinkedHashMap<EventId, Event> EVENTS = allEvents(OFFLINE_EVENTS, ONLINE_EVENTS);

    public static void main(String[] args) throws JsonProcessingException, IOException {
        int roleChoice = -1;
        while (roleChoice != 4) {
            System.out.println("Choose a role:");
            System.out.println("1. Organizer");
            System.out.println("2. Attendee");
            System.out.println("3. Volunteer");
            System.out.println("4. Exit");
            roleChoice = INPUT.nextInt();
            INPUT.nextLine(); // Consume the newline character

            switch (roleChoice) {
                case 1:
                    processOrganizer();
                    break;
                case 2:
                    processAttendee();
                    break;
                case 3:
                    processVolunteer();
                    break;
                case 4:
                    System.out.println("Thank you for using our service. Please come again.");
                    break;
                default:
                    System.out.println("Invalid choice. Enter a valid choice");
                    break;
            }

            updateEvents(OFFLINE_EVENTS,OfflineEvent.OFFLINE_FILE);
            updateEvents(ONLINE_EVENTS, OnlineEvent.ONLINE_FILE);

        }
    }

    private static LinkedHashMap<EventId, Event> allEvents(LinkedHashSet<OfflineEvent> offlineEvents,
     LinkedHashSet<OnlineEvent> onlineEvents) {
        LinkedHashMap<EventId, Event> result = new LinkedHashMap<>();
        for(Event event : offlineEvents){
            result.put(event.getId(), event);
        }
        for(Event event : onlineEvents){
            result.put(event.getId(), event);
        }
        return result;
    }

    private static<T extends Event> void updateEvents(LinkedHashSet<T> events, String filePath) {
        String json = CustomJson.toJsonArray(events);
        writeJsonToFile(filePath, json);
    }

    private static <T extends User> void updateUser(T newUser, String filepath) throws IOException {
        ArrayList<T> oldstuff = MAPPER.readValue(new File(filepath), MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, newUser.getClass()));
        int userIndex = oldstuff.indexOf(newUser);
        oldstuff.set(userIndex, newUser);
        WRITER.writeValue(new File(filepath), oldstuff);
    }


    private static <T extends User & UserOperation<T>> T signUpOrLogin(T user, String userCredentialsFile,
            String userDataFile, Class<T> clazz) {
        String className = user.getClass().getSimpleName();
        boolean validChoice;
        do {
            validChoice = true;
            System.out.println("Choose an action:");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");

            int actionChoice = INPUT.nextInt();
            INPUT.nextLine(); // Consume the newline character
            switch (actionChoice) {
                case 1:
                    System.out.println(className + " Sign Up");
                    Credentials.addUsernameAndPassword(userCredentialsFile);
                    user = user.signUp(user);
                    break;
                case 2:
                    System.out.println(className + " Log In");
                    try{
                    user = login(userCredentialsFile, userDataFile, clazz);
                    }catch(InvalidLoginException e){
                        System.out.println(e);
                        validChoice = false;
                    }
                    break;
                case 3:
                    return null;
                default:
                    System.out.println("Invalid choice. Enter a valid choice ");
                    validChoice = false;
                    break;
            }
        } while (!validChoice);
        return user;
    }

    private static void processOrganizer() throws JsonProcessingException, IOException {
        final String ORGANIZER_CREDENTIALS = "app/src/main/files/login/organizer_credentials.txt";
        Organizer organizer = signUpOrLogin(new Organizer(), ORGANIZER_CREDENTIALS, Organizer.ORGANIZER_FILE, Organizer.class);
        if (organizer == null){
            System.out.println("Error?");
            return;
        }
        int choice = -1;
        while (choice != 4) {
            System.out.println("Choose an action:");
            System.out.println("1. Post new event");
            System.out.println("2. Display events");
            System.out.println("3. Cancel event");
            System.out.println("4. Exit");

            choice = INPUT.nextInt();
            INPUT.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    Event newEvent = organizer.createEvent(EVENTS);
                    EVENTS.put(newEvent.getId(), newEvent);
                    if(newEvent instanceof OnlineEvent)
                        ONLINE_EVENTS.add( (OnlineEvent) newEvent);
                    else if(newEvent instanceof OfflineEvent)
                        OFFLINE_EVENTS.add( (OfflineEvent) newEvent);
                    break;
                case 2:
                    organizer.displayEvents(EVENTS);
                    break;
                case 3:
                    System.out.println("Enter event ID to cancel:");
                    ArrayList<EventId> ids = new ArrayList<>(organizer.getEvents());
                    for (int i = 0; i < ids.size(); i++) {
                        System.out.println(i + 1 + ". " + ids.get(i));
                    }
                    EventId id = ids.get(Integer.parseInt(INPUT.nextLine()) - 1);
                    System.out.println(id);
                    organizer.cancelEvent(id);
                    EVENTS.remove(id);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    updateUser(organizer, Organizer.ORGANIZER_FILE);
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void processAttendee() throws IOException{
        final String ATTENDEE_CREDENTIALS = "app/src/main/files/login/attendee_credentials.txt";
        Attendee attendee = signUpOrLogin(new Attendee(), ATTENDEE_CREDENTIALS, Attendee.ATTENDEE_FILE, Attendee.class);
        if (attendee == null)
            return;
        int choice = -1;
        while (choice != 6) {
            System.out.println("Choose an action:");
            System.out.println("1. Display available events ");
            System.out.println("2. Register for event  ");
            System.out.println("3. Cancel registration ");
            System.out.println("4. Display registered events");
            System.out.println("5. Upcoming events");
            System.out.println("6. Exit");

            choice = INPUT.nextInt();
            INPUT.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    for (EventId id : EVENTS.keySet()) {
                        EVENTS.get(id).displayDetails();
                    }
                    break;
                case 2:
                    System.out.println("Enter event ID:");
                    ArrayList<EventId> ids = new ArrayList<>(EVENTS.keySet());
                    for (int i = 0; i < ids.size(); i++) {
                        System.out.println(i + 1 + ". " + ids.get(i));
                    }
                    EventId id = ids.get(Integer.parseInt(INPUT.nextLine()) - 1);
                    System.out.println(id);
                    try {
                        attendee.registerForEvent(id, EVENTS);
                        EVENTS.get(id).registerAttendee(attendee.getId());
                    } catch (AlreadyParticipatedException | ConflictingParticipationException e) {
                        System.out.println(e);
                    }
                    break;
                case 3:
                    System.out.println("Enter event ID to cancel:");
                    ArrayList<EventId> IDs = new ArrayList<>(attendee.getEvents());
                    for (int i = 0; i < IDs.size(); i++) {
                        System.out.println(i + 1 + ". " + IDs.get(i));
                    }
                    EventId ID = IDs.get(Integer.parseInt(INPUT.nextLine()) - 1);
                    System.out.println(ID);
                    attendee.cancelRegistration(ID);
                    EVENTS.get(ID).unregisterAttendee(attendee.getId());
                    break;
                case 4:
                    attendee.displayEvents(EVENTS);
                    updateUser(attendee, Attendee.ATTENDEE_FILE);
                    break;
                case 5:
                    for (EventId event : attendee.getEvents()) {
                        EVENTS.get(event).notification(attendee.getId());
                    }
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void processVolunteer() throws IOException{
        final String VOLUNTEER_CREDENTIALS = "app/src/main/files/login/volunteer_credentials.txt";
        Volunteer volunteer = signUpOrLogin(new Volunteer(), VOLUNTEER_CREDENTIALS, Volunteer.VOLUNTEER_FILE, Volunteer.class);
        int choice = -1;
        while (choice != 6) {
            System.out.println("Choose an action:");
            System.out.println("1. Display available events ");
            System.out.println("2. Volunteer  for event");
            System.out.println("3. Cancel  ");
            System.out.println("4. Display registered events ");
            System.out.println("5. Display upcoming events");
            System.out.println("6. Exit");

            choice = INPUT.nextInt();
            INPUT.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    for (EventId id : EVENTS.keySet()) {
                        EVENTS.get(id).displayDetails();
                    }
                    break;
                case 2:
                    System.out.println("Enter event ID:");
                    ArrayList<EventId> ids = new ArrayList<>(EVENTS.keySet());
                    for (int i = 0; i < ids.size(); i++) {
                        System.out.println(i + 1 + ". " + ids.get(i));
                    }
                    EventId id = ids.get(Integer.parseInt(INPUT.nextLine()) - 1);
                    System.out.println(id);
                    volunteer.registerForEvent(id, EVENTS);
                    EVENTS.get(id).registerVolunteer(volunteer.getId());
                    break;
                case 3:
                    System.out.println("Enter event ID to cancel:");
                    ArrayList<EventId> IDs = new ArrayList<>(volunteer.getEvents());
                    for (int i = 0; i < IDs.size(); i++) {
                        System.out.println(i + 1 + ". " + IDs.get(i));
                    }
                    EventId ID = IDs.get(Integer.parseInt(INPUT.nextLine()) - 1);
                    System.out.println(ID);
                    volunteer.cancelRegistration(ID);
                    EVENTS.get(ID).unregisterVolunteer(volunteer.getId());
                    break;
                case 4:
                    volunteer.displayEvents(EVENTS);
                    break;
                case 5:
                    for (EventId event : volunteer.getEvents()) {
                        EVENTS.get(event).notification(volunteer.getId());
                    }
                    break;
                case 6:
                    System.out.println("Exiting...");
                    updateUser(volunteer, Volunteer.VOLUNTEER_FILE);
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }

    }

    private static <T extends User & UserOperation<T>> T login(String credFilepath, String dataFilePath, Class<T> clazz) throws InvalidLoginException{
        try (
                BufferedReader reader = new BufferedReader(new FileReader(credFilepath))) {

            System.out.println("Login:");
            System.out.print("Enter username: ");
            String username = INPUT.nextLine();
            System.out.print("Enter password: ");
            String password = INPUT.nextLine();

            // Check if the provided username-password pair exists in the file
            int lineNum = Credentials.isValidCredentials(username, password, credFilepath);
            System.out.println("This is valid @" + lineNum);
            if (lineNum > 0) {
                return JSONConvertable.readFromJSON(MAPPER.readTree(new File(dataFilePath)), lineNum - 1, clazz);
            } else {
                throw new InvalidLoginException();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
