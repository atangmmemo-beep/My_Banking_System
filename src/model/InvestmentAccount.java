package model;

// An investment account pays more interest and allows withdrawals.
public class InvestmentAccount extends Account implements InterestBearing {

    // static final = a fixed value that never changes
    public static final double MIN_OPENING = 500.00;

    private double interestRate = 0.05;     // 5% per month

    public InvestmentAccount(String accountNumber, double balance, String branch,
                             String dateOpened, Customer owner) {
        super(accountNumber, balance, branch, dateOpened, owner);
    }

    @Override
    public boolean withdraw(double amount) {
        return takeMoney(amount);   // allowed, as long as there is enough money
    }

    @Override
    public double calculateInterest() {
        return getBalance() * interestRate;
    }

    @Override
    public void payInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            addInterest(interest);
        }
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }

    public double getInterestRate() { return interestRate; }
}