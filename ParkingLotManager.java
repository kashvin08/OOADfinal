package core;

import domain.ParkingSpot;
import domain.Vehicle;
import shared.Constants;
import shared.Enums.VehicleType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotManager {

    // multi level structure (all spots across floors)
    private final List<ParkingSpot> allSpots;
    private double totalRevenue = 0.0;

    // singleton private constructor
    private ParkingLotManager() {
        this.allSpots = new ArrayList<>();
        initializeParkingLot();
        loadParkedVehicles();
    }

    private static class Holder {
        private static final ParkingLotManager INSTANCE = new ParkingLotManager();
    }

    public static ParkingLotManager getInstance() {
        return Holder.INSTANCE;
    }
    // ------------------------------------------

    // initialize 5 floors with different spot types
    private void initializeParkingLot() {
        for (int floor = 1; floor <= 5; floor++) {
            for (int row = 1; row <= 2; row++) {
                for (int spotNum = 1; spotNum <= 5; spotNum++) {

                    String id = "F" + floor + "-R" + row + "-S" + spotNum;
                    String type;
                    double rate;

                    if (row == 1) {
                        type = "Regular";
                        rate = Constants.RATE_REGULAR;
                    } else if (spotNum == 1) {
                        type = "Handicapped";
                        rate = Constants.RATE_HANDICAPPED_RESERVED;
                    } else if (spotNum == 2) {
                        type = "Reserved";
                        rate = Constants.RATE_VIP_RESERVED;
                    } else {
                        type = "Compact";
                        rate = Constants.RATE_COMPACT;
                    }

                    allSpots.add(new ParkingSpot(id, type, rate));
                }
            }
        }
    }

    // entry system to find suitable available spots
    public List<ParkingSpot> getAvailableSpotsForVehicle(Vehicle vehicle) {
        List<ParkingSpot> suitableSpots = new ArrayList<>();

        for (ParkingSpot spot : allSpots) {
            if (!spot.isOccupied() && spot.canFit(vehicle)) {
                suitableSpots.add(spot);
            }
        }
        return suitableSpots;
    }
    
    private void loadParkedVehicles() {
        List<String> records = FileHandler.loadAllRecords(Constants.VEHICLES_FILE);
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (String line : records) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                String plate = parts[0];
                VehicleType type = VehicleType.valueOf(parts[1]); // Converts String to Enum
                String spotId = parts[2];
                LocalDateTime entryTime = LocalDateTime.parse(parts[3], dtf);

                // Recreate Vehicle Object
                Vehicle v = new Vehicle(plate, type);
                v.setEntryTime(entryTime);

                // Find the spot and park it
                for (ParkingSpot spot : allSpots) {
                    if (spot.getId().equals(spotId)) {
                        spot.park(v);
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Skipping corrupted line: " + line);
            }
        }
    }

    public List<ParkingSpot> getAllSpots() {
        return new ArrayList<>(allSpots); // Return copy to protect data
    }

    // admin panel occupancy report
    public double calculateOccupancyRate() {
        long occupiedCount = allSpots.stream()
                .filter(ParkingSpot::isOccupied)
                .count();

        if (allSpots.isEmpty()) return 0.0;
        return (double) occupiedCount / allSpots.size() * 100;
    }

    // track revenue
    public void addRevenue(double amount) {
        totalRevenue += amount;

        // persistence
        FileHandler.saveRecord(
                Constants.REVENUE_FILE,
                "Total Revenue: RM " + totalRevenue
        );
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}