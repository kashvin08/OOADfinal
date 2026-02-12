package logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fine {
    private String fineId;
    private String licensePlate;
    private double amount;
    private LocalDateTime issuedDate;
    private boolean paid;
    private String reason;
    private FineScheme scheme;
    
    public Fine(String licensePlate, double amount, String reason, FineScheme scheme) {
        this.licensePlate = licensePlate;
        this.amount = amount;
        this.reason = reason;
        this.scheme = scheme;
        this.issuedDate = LocalDateTime.now();
        this.paid = false;
        this.fineId = generateFineId();
    }
    
    public Fine(String fineId, String licensePlate, double amount, LocalDateTime issuedDate, 
                boolean paid, String reason, String schemeName) {
        this.fineId = fineId;
        this.licensePlate = licensePlate;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.paid = paid;
        this.reason = reason;
        this.scheme = null;
    }
    
    private String generateFineId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "F-" + licensePlate + "-" + issuedDate.format(formatter);
    }
    
    public void markAsPaid() {
        this.paid = true;
    }
    
    // Getters
    public String getFineId() { return fineId; }
    public String getLicensePlate() { return licensePlate; }
    public double getAmount() { return amount; }
    public LocalDateTime getIssuedDate() { return issuedDate; }
    public boolean isPaid() { return paid; }
    public String getReason() { return reason; }
    public FineScheme getScheme() { return scheme; }
    
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String schemeName = (scheme != null) ? scheme.getSchemeName() : "Unknown";
        return String.join("|", 
            fineId, 
            licensePlate, 
            String.valueOf(amount), 
            issuedDate.format(formatter), 
            String.valueOf(paid), 
            reason,
            schemeName
        );
    }
    
    public static Fine fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new Fine(
            parts[0],
            parts[1],
            Double.parseDouble(parts[2]),
            LocalDateTime.parse(parts[3], formatter),
            Boolean.parseBoolean(parts[4]),
            parts[5],
            parts[6]
        );
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Fine ID: %s | Plate: %s | Amount: $%.2f | Date: %s | Status: %s",
                fineId, licensePlate, amount, issuedDate.format(formatter), 
                paid ? "PAID" : "UNPAID");
    }
}
