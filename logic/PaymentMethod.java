package logic;

public interface PaymentMethod {
   
    boolean processPayment(double amount);
  
    String getMethodName();
  
    boolean validate();
}
