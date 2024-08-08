package common;

import java.lang.Math;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import static json.CustomJson.MAPPER;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.time.Duration;

public class Location {

    private String address;

    // @JsonUnwrapped
    private Coordinates coordinates;
    private static final Scanner INPUT = new Scanner(System.in);

    public Location(
            @JsonProperty("address") String address, @JsonProperty("lat") double latitude,
            @JsonProperty("long") double longitude) {
        this.address = address;
        this.coordinates = new Coordinates(latitude, longitude);
    }

    public String getAddress() {
        return address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public static double calcDistance(Location l1, Location l2) {
        return Coordinates.calcDistance(l1.coordinates, l2.coordinates);
    }

    public final static class Coordinates {
        private double latitude;
        private double longitude;

        public Coordinates(
                @JsonProperty("lat") double latitude,
                @JsonProperty("long") double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public static double calcDistance(Coordinates c1, Coordinates c2) {

            final int R = 6371; // Radius of the earth
            double lat1 = c1.latitude;
            double lat2 = c2.latitude;
            double lon1 = c1.longitude;
            double lon2 = c2.longitude;

            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c * 1000; // convert to meters

            return distance;
        }

    }

    public static Location getLocation(String helpString){
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = null;
        String address = null;
        do {
            System.out.println("Enter " + helpString + " address: ");
            address = INPUT.nextLine();
            String urlifiedSearchString = "%22" + address.replace(" ", "%20") + "%22";

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create("https://geocode.maps.co/search?q=" + urlifiedSearchString))
                        .build();
                response = httpClient.send(request,
                        HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            if(response == null)
                throw new NullPointerException("The response is NULL");

        } while (response.statusCode() != 200);
        try {
            JsonNode mapJson = MAPPER.readTree(response.body());
            double latitude = Double.parseDouble(mapJson.get(0).get("lat").toString().replace('\"', ' '));
            double longitude = Double.parseDouble(mapJson.get(0).get("lon").toString().replace('\"', ' '));
            return new Location(address, latitude, longitude);
        } catch (JsonProcessingException f) {
            System.out.println("Smaller address please!");
            throw new RuntimeException("Error Processing Json");
        }

    }

    @Override
    public String toString() {
        return address;
    }
}
