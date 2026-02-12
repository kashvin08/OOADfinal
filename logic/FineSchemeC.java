package logic;

public class FineSchemeC implements FineScheme {
    private static final double PERCENTAGE_PER_30MIN = 0.50;
    
    @Override
    public double calculateFine(long overstayMinutes, double baseRate) {
        if (overstayMinutes <= 0) {
            return 0.0;
        }
        
        // Calculate number of 30-minute blocks (round up)
        int blocks = (int) Math.ceil(overstayMinutes / 30.0);
        
        return blocks * PERCENTAGE_PER_30MIN * baseRate;
    }
    
    @Override
    public String getSchemeName() {
        return "Option C: Percentage-Based";
    }
    
    @Override
    public String getDescription() {
        return "50% of base rate per 30 minutes of overstay";
    }
}
