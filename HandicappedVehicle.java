package domain.vehicles;
import shared.Enums.VehicleType;

public class HandicappedVehicle extends Vehicle {
    public HandicappedVehicle(String licensePlate) {
        super(licensePlate, VehicleType.HANDICAPPED);
    }
}