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
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Fine ID: %s | Plate: %s | Amount: $%.2f | Date: %s | Status: %s",
                fineId, licensePlate, amount, issuedDate.format(formatter), 
                paid ? "PAID" : "UNPAID");
    }
}
