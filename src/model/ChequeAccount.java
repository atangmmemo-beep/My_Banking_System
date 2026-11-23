package model;

// A cheque account is for people who work (salary goes in here).
// It does NOT earn interest, so it does NOT implement InterestBearing.
public class ChequeAccount extends Account {

    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountNumber, double balance, String branch,
                         String dateOpened, Customer owner,
                         String employerName, String employerAddress) {
        super(accountNumber, balance, branch, dateOpened, owner);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public boolean withdraw(double amount) {
        return takeMoney(amount);

    }

    @Override
    public String getAccountType() {
        return "CHEQUE";

    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}