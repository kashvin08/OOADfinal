package core;

import domain.ParkingSpot;
import domain.Ticket;
import domain.Vehicle;
import shared.Constants;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class EntryExitService {
    
    private ParkingLotManager manager;

    public EntryExitService() {
        this.manager = ParkingLotManager.getInstance();
    }

    // entry
    public List<ParkingSpot> findAvailableSpots(Vehicle vehicle) {
        return manager.getAvailableSpotsForVehicle(vehicle);
    }

    public Ticket processEntry(Vehicle vehicle, ParkingSpot spot) {
        spot.park(vehicle);
        String entryRecord = vehicle.getPlateNumber() + "|" + 
                             vehicle.getType() + "|" + 
                             spot.getId() + "|" + 
                             vehicle.getEntryTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                             //if wan directly see 2hour changes use this
                             //vehicle.setEntryTime(LocalDateTime.now().minusHours(2));
                             
        FileHandler.saveRecord(Constants.VEHICLES_FILE, entryRecord);
        
        return new Ticket(vehicle, spot);
    }
    

    // exit
    public ParkingSpot findVehicleByPlate(String plateNumber) {
        List<ParkingSpot> allSpots = manager.getAllSpots();
        for (ParkingSpot spot : allSpots) {
            if (spot.isOccupied()) {
                Vehicle v = spot.getCurrentVehicle();
                if (v.getPlateNumber().equalsIgnoreCase(plateNumber)) {
                    return spot;
                }
            }
        }
        return null;
    }

    public double calculateFee(ParkingSpot spot) {
        Vehicle v = spot.getCurrentVehicle();
        long minutes = Duration.between(v.getEntryTime(), LocalDateTime.now()).toMinutes();
        
        // calculation
        long hours = (long) Math.ceil(minutes / 60.0); 
        if (hours == 0) hours = 1;

        return spot.getRate() * hours;
    }
    
    public double calculateFine(ParkingSpot spot) {
        Vehicle v = spot.getCurrentVehicle();
        long hours = Duration.between(v.getEntryTime(), LocalDateTime.now()).toHours();

        if (hours > 24) {
            return Constants.FINE_FIXED; 
        }
        return 0.0;
    }

    public void processPayment(ParkingSpot spot, double amount) {
        Vehicle v = spot.getCurrentVehicle();
        String plateToRemove = v.getPlateNumber();
        
        //for receipt id
        int randomNum = (int)(Math.random() * 1000000);
        String receiptId = "" + randomNum;
        //save to exit history
        String exitRecord = receiptId + "|" + plateToRemove + "|" + LocalDateTime.now() + "|PAID:RM" + amount;
        FileHandler.saveRecord(Constants.EXIT_FILE, exitRecord);

        //remove record from parked_vehicles.txt
        removeVehicleFromFile(plateToRemove);
        manager.addRevenue(amount);
        spot.removeVehicle();
        
        //can remove
        System.out.println("Transaction Complete. Receipt ID: " + receiptId);
    }
    //this one is helper to remove specific line from txt
    private void removeVehicleFromFile(String plateToRemove) {
        List<String> allLines = FileHandler.loadAllRecords(Constants.VEHICLES_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : allLines) {
            //delete only selected car plate, keep the other cars record 
            if (!line.startsWith(plateToRemove + "|")) {
                updatedLines.add(line);
            }
        }
        
        // rewrite the file with the clean list
        FileHandler.overwriteFile(Constants.VEHICLES_FILE, updatedLines);
    }
}