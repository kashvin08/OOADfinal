package logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentTransaction {
    private String transactionId;
    private String licensePlate;
    private double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime transactionDate;
    private boolean successful;
    private String fineId;
    
    public PaymentTransaction(String licensePlate, double amount, PaymentMethod paymentMethod, String fineId) {
        this.licensePlate = licensePlate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.fineId = fineId;
        this.transactionDate = LocalDateTime.now();
        this.transactionId = generateTransactionId();
        this.successful = false;
    }
    
    private String generateTransactionId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "TXN-" + transactionDate.format(formatter);
    }
    
    public void markAsSuccessful() {
        this.successful = true;
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getLicensePlate() { return licensePlate; }
    public double getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public boolean isSuccessful() { return successful; }
    public String getFineId() { return fineId; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Transaction: %s | Amount: $%.2f | Method: %s | Date: %s | Status: %s",
                transactionId, amount, paymentMethod.getMethodName(), 
                transactionDate.format(formatter), successful ? "SUCCESS" : "FAILED");
    }
}
