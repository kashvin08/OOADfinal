package core;

import domain.ParkingSpot;
import domain.Vehicle;
import shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotManager {

    //multi level structure (all spots across floors)
    private final List<ParkingSpot> allSpots;
    private double totalRevenue = 0.0;

    //singleton private constructor
    private ParkingLotManager() {
        this.allSpots = new ArrayList<>();
        initializeParkingLot();
    }

    //thread safe
    private static class Holder {
        private static final ParkingLotManager INSTANCE =
                new ParkingLotManager();
    }

    public static ParkingLotManager getInstance() {
        return Holder.INSTANCE;
    }

    //initialize 5 floors with different spot types
    private void initializeParkingLot() {//basically in every floor row 2 spot 1 (oku) and row 2 spot 2 (vip)
        for (int floor = 1; floor <= 5; floor++) {
            for (int row = 1; row <= 2; row++) {
                for (int spotNum = 1; spotNum <= 5; spotNum++) {

                    String id = "F" + floor + "-R" + row + "-S" + spotNum;//row 1 = regular,row 2 = handicapped/reserved/compact
                    String type;
                    double rate;

                    if (row == 1) {//row 1 in every floor reserved for SUV and normal cars
                        type = "Regular";
                        rate = Constants.RATE_REGULAR;
                    } else if (spotNum == 1) {//row 2 spot 1 is always for oku
                        type = "Handicapped";
                        rate = Constants.RATE_HANDICAPPED_RESERVED;
                    } else if (spotNum == 2) {//spot 2 in row 2 is for vip
                        type = "Reserved";
                        rate = Constants.RATE_VIP_RESERVED;
                    } else {//rest of row 2 is for smaller vehicles/motor
                        type = "Compact";
                        rate = Constants.RATE_COMPACT;
                    }

                    allSpots.add(new ParkingSpot(id, type, rate));
                }
            }
        }
    }

    //entry system to find suitable available spots
    public List<ParkingSpot> getAvailableSpotsForVehicle(Vehicle vehicle) {
        List<ParkingSpot> suitableSpots = new ArrayList<>();

        for (ParkingSpot spot : allSpots) {
            if (!spot.isOccupied() && spot.canFit(vehicle)) {
                suitableSpots.add(spot);
            }
        }
        return suitableSpots;
    }

    //admin panel occupancy report
    public double calculateOccupancyRate() {
        long occupiedCount = allSpots.stream()
                .filter(ParkingSpot::isOccupied)
                .count();

        return (double) occupiedCount / allSpots.size() * 100;
    }

    //track revenue
    public void addRevenue(double amount) {
        totalRevenue += amount;

        //persistence
        FileHandler.saveRecord(
                Constants.REVENUE_FILE,
                "Total Revenue: RM " + totalRevenue
        );
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }


    // ADMIN GUI --- Ming Sheng

    public List<ParkingSpot> getAllSpots() {
        return new ArrayList<>(allSpots);
    }

    private String currentFineScheme = "Fixed"; // Default scheme

    public void setFineScheme(String scheme) {
        this.currentFineScheme = scheme;
    }

    public String getFineScheme() {
        return currentFineScheme;
    }

    public int getTotalParkedVehicles() {
        return (int) allSpots.stream().filter(ParkingSpot::isOccupied).count();
    }
}
