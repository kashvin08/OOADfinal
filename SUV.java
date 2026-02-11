package domain.vehicles;
import shared.Enums.VehicleType;

public class SUV extends Vehicle {
    public SUV(String licensePlate) {
        super(licensePlate, VehicleType.SUV_TRUCK);
    }
}