package logic;

public interface FineScheme {
    double calculateFine(long overstayMinutes, double baseRate);
    
    String getSchemeName();
    
    String getDescription();
}
