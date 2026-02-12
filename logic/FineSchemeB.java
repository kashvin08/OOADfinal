package logic;

public class FineSchemeB implements FineScheme {
    
    @Override
    public double calculateFine(long overstayMinutes, double baseRate) {
        if (overstayMinutes <= 0) {
            return 0.0;
        }
        
        double fine = 0.0;
        long remainingMinutes = overstayMinutes;
        
        // First hour: 1x base rate
        if (remainingMinutes > 0) {
            long firstHourMinutes = Math.min(remainingMinutes, 60);
            fine += (firstHourMinutes / 60.0) * baseRate;
            remainingMinutes -= firstHourMinutes;
        }
        
        // Second hour: 2x base rate
        if (remainingMinutes > 0) {
            long secondHourMinutes = Math.min(remainingMinutes, 60);
            fine += (secondHourMinutes / 60.0) * baseRate * 2;
            remainingMinutes -= secondHourMinutes;
        }
        
        // Third hour and beyond: 3x base rate
        if (remainingMinutes > 0) {
            fine += (remainingMinutes / 60.0) * baseRate * 3;
        }
        
        return fine;
    }
    
    @Override
    public String getSchemeName() {
        return "Option B: Progressive Rate";
    }
    
    @Override
    public String getDescription() {
        return "1st hour: 1x rate, 2nd hour: 2x rate, 3rd+ hour: 3x rate";
    }
}
