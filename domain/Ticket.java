package domain;

import java.time.format.DateTimeFormatter;

public class Ticket {
    private String ticketId;
    private String spotId;
    private String entryTimeStr;

    public Ticket(Vehicle v, ParkingSpot s) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddHHmm");
        this.ticketId = v.getPlateNumber() + v.getEntryTime().format(dtf);
        this.spotId = s.getId();
        this.entryTimeStr = v.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return "=== PARKING TICKET ===\n" +
               "Ticket ID: " + ticketId + "\n" +
               "Spot: " + spotId + "\n" +
               "Time: " + entryTimeStr + "\n" +
               "======================";
    }
}