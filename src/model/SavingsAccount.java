package model;

// "extends" = SavingsAccount is a kind of Account (INHERITANCE).
// "implements" = it keeps the promise made by the InterestBearing interface.
public class SavingsAccount extends Account implements InterestBearing {

    private double interestRate = 0.0005;   // 0.05% per month

    public SavingsAccount(String accountNumber, double balance, String branch,
                          String dateOpened, Customer owner) {
        super(accountNumber, balance, branch, dateOpened, owner);   // pass details up to Account
    }

    // @Override = we are replacing the abstract method from Account
    @Override
    public boolean withdraw(double amount) {
        return false;   // savings accounts do NOT allow withdrawals
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
        return "SAVINGS";
    }

    public double getInterestRate() { return interestRate; }
}