package logic;

import core.FileHandler;
import java.util.*;
import java.util.stream.Collectors;
import shared.Constants;

public class FinancialManager {
    private List<Fine> fines;
    private List<PaymentTransaction> transactions;
    private FineScheme currentFineScheme;
    
    public FinancialManager() {
        this.fines = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.currentFineScheme = new FineSchemeA();
        loadFinesFromFile();
    }

    public void setFineScheme(FineScheme scheme) {
        this.currentFineScheme = scheme;
    }
    
    public FineScheme getCurrentFineScheme() {
        return currentFineScheme;
    }
  
    public Fine issueFine(String licensePlate, long overstayMinutes, double baseRate) {
        double fineAmount = currentFineScheme.calculateFine(overstayMinutes, baseRate);
        String reason = String.format("Overstay: %d minutes", overstayMinutes);
        
        Fine fine = new Fine(licensePlate, fineAmount, reason, currentFineScheme);
        fines.add(fine);
        saveFineToFile(fine);
        
        return fine;
    }

    public PaymentTransaction processPayment(String fineId, PaymentMethod paymentMethod) {
        Fine fine = findFineById(fineId);
        
        if (fine == null) {
            throw new IllegalArgumentException("Fine not found: " + fineId);
        }
        
        if (fine.isPaid()) {
            throw new IllegalStateException("Fine already paid: " + fineId);
        }
        
        PaymentTransaction transaction = new PaymentTransaction(
            fine.getLicensePlate(), 
            fine.getAmount(), 
            paymentMethod,
            fineId
        );
        
        boolean paymentSuccess = paymentMethod.processPayment(fine.getAmount());
        
        if (paymentSuccess) {
            transaction.markAsSuccessful();
            fine.markAsPaid();
            saveAllFinesToFile();
        }
        
        transactions.add(transaction);
        return transaction;
    }

    public List<Fine> getUnpaidFines(String licensePlate) {
        return fines.stream()
                .filter(f -> f.getLicensePlate().equalsIgnoreCase(licensePlate) && !f.isPaid())
                .collect(Collectors.toList());
    }
  
    public List<Fine> getAllFines(String licensePlate) {
        return fines.stream()
                .filter(f -> f.getLicensePlate().equalsIgnoreCase(licensePlate))
                .collect(Collectors.toList());
    }

    public double getTotalUnpaidAmount(String licensePlate) {
        return getUnpaidFines(licensePlate).stream()
                .mapToDouble(Fine::getAmount)
                .sum();
    }
 
    public Fine findFineById(String fineId) {
        return fines.stream()
                .filter(f -> f.getFineId().equals(fineId))
                .findFirst()
                .orElse(null);
    }

    public List<Fine> getAllFines() {
        return new ArrayList<>(fines);
    }
  
    public List<PaymentTransaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
 
    public List<PaymentTransaction> getTransactions(String licensePlate) {
        return transactions.stream()
                .filter(t -> t.getLicensePlate().equalsIgnoreCase(licensePlate))
                .collect(Collectors.toList());
    }
    
    private void saveFineToFile(Fine fine) {
        FileHandler.saveRecord(Constants.FINES_FILE, fine.toFileString());
    }
    
    private void saveAllFinesToFile() {
        List<String> fineRecords = fines.stream()
                .map(Fine::toFileString)
                .collect(Collectors.toList());
        FileHandler.overwriteFile(Constants.FINES_FILE, fineRecords);
    }
    
    private void loadFinesFromFile() {
        List<String> records = FileHandler.loadAllRecords(Constants.FINES_FILE);
        for (String record : records) {
            Fine fine = Fine.fromFileString(record);
            if (fine != null) {
                fines.add(fine);
            }
        }
    }
}
