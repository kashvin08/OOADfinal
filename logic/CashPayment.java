package logic.finance;

/**
 * Cash payment implementation
 */
public class CashPayment implements PaymentMethod {
    private double amountTendered;
    
    public CashPayment(double amountTendered) {
        this.amountTendered = amountTendered;
    }
    
    @Override
    public boolean processPayment(double amount) {
        if (!validate() || amountTendered < amount) {
            return false;
        }
        // In real system, this would interface with cash register
        return true;
    }
    
    @Override
    public String getMethodName() {
        return "Cash";
    }
    
    @Override
    public boolean validate() {
        return amountTendered > 0;
    }
    
    public double getChange(double amount) {
        return amountTendered - amount;
    }
    
    public double getAmountTendered() {
        return amountTendered;
    }
}
