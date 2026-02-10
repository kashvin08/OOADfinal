package domain;

import shared.Enums.VehicleType;
import java.time.LocalDateTime;

public class Vehicle {
    private String plateNumber;
    private VehicleType type;
    private LocalDateTime entryTime;

    public Vehicle(String plateNumber, VehicleType type) {
        this.plateNumber = plateNumber;
        this.type = type;
        this.entryTime = LocalDateTime.now();
    }

    public void setEntryTime(LocalDateTime time) {
        this.entryTime = time;
    }
    public String getPlateNumber() { return plateNumber; }
    public VehicleType getType() { return type; }
    public LocalDateTime getEntryTime() { return entryTime; }
}