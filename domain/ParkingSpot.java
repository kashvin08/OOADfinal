package domain;

import shared.Enums.VehicleType;
import shared.Constants;

public class ParkingSpot {
    private String id;
    private String type; 
    private double rate;
    private boolean occupied;
    private Vehicle currentVehicle;

    public ParkingSpot(String id, String type, double rate) {
        this.id = id;
        this.type = type;
        this.rate = rate;
        this.occupied = false;
    }

    public boolean isOccupied() { return occupied; }
    public Vehicle getCurrentVehicle() { return currentVehicle; }
    public String getId() { return id; }
    public double getRate() { return rate; }
    public String getType() { return type; }

    public boolean canFit(Vehicle v) {
        if (occupied) return false;
        if (v.getType() == VehicleType.HANDICAPPED) return true;
        if (this.type.equals("Compact")) {
            return v.getType() == VehicleType.MOTORCYCLE || v.getType() == VehicleType.CAR;
        } else if (this.type.equals("Regular")) {
            return v.getType() == VehicleType.CAR || v.getType() == VehicleType.SUV_TRUCK;
        } else if (this.type.equals("Handicapped")) {
            return v.getType() == VehicleType.HANDICAPPED;
        }
        
        return false; 
    }

    public void park(Vehicle v) {
        this.currentVehicle = v;
        this.occupied = true;
    }

    public void removeVehicle() {
        this.currentVehicle = null;
        this.occupied = false;
    }
}