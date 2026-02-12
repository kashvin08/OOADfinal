package logic;

public class CardPayment implements PaymentMethod {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    
    public CardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public boolean processPayment(double amount) {
        if (!validate()) {
            return false;
        }
        return true;
    }
    
    @Override
    public String getMethodName() {
        return "Card";
    }
    
    @Override
    public boolean validate() {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            return false;
        }
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            return false;
        }
        return true;
    }    
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}

