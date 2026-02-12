package logic;

public class FineSchemeA implements FineScheme {
    private static final double FLAT_FINE = 50.0;
    
    @Override
    public double calculateFine(long overstayMinutes, double baseRate) {
        if (overstayMinutes <= 0) {
            return 0.0;
        }
        return FLAT_FINE;
    }
    
    @Override
    public String getSchemeName() {
        return "Option A: Flat Rate";
    }
    
    @Override
    public String getDescription() {
        return "Fixed fine of $" + FLAT_FINE + " for any overstay";
    }
}
