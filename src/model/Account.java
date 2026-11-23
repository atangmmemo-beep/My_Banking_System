package model;

import java.util.ArrayList;

// ABSTRACT class: a general idea of an account. You can NOT write new Account(...).
// You can only create its children: SavingsAccount, InvestmentAccount, ChequeAccount.
public abstract class Account {

    private String accountNumber;
    private double balance;
    private String branch;
    private String dateOpened;
    private Customer owner;   // an account must belong to a customer
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Account(String accountNumber, double balance, String branch,
                   String dateOpened, Customer owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.dateOpened = dateOpened;
        this.owner = owner;
    }

    // OVERLOADING: two methods with the same name but different parameters.
    public boolean deposit(double amount) {
        return deposit(amount, "Deposit");

    }

    public boolean deposit(double amount, String note) {
        if (amount <= 0) {
            return false;                       // reject zero or negative amounts
        }
        balance = balance + amount;
        Transaction t = new Transaction("DEPOSIT", amount, balance, note);
        transactions.add(t);                    // keep a record
        return true;
    }

    // ABSTRACT methods: no code here. Every child MUST write its own version.
    public abstract boolean withdraw(double amount);

    public abstract String getAccountType();

    // PROTECTED = usable by this class and its children only.
    // The children use it so the balance stays private.
    protected boolean takeMoney(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;                       // not enough money
        }
        balance = balance - amount;
        Transaction t = new Transaction("WITHDRAW", amount, balance, "Withdrawal");
        transactions.add(t);
        return true;
    }

    protected void addInterest(double interest) {
        balance = balance + interest;
        Transaction t = new Transaction("INTEREST", interest, balance, "Monthly interest");
        transactions.add(t);
    }

    // Used later when we load old transactions from the database
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Getters
    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public String getDateOpened() { return dateOpened; }
    public Customer getOwner() { return owner; }
    public ArrayList<Transaction> getTransactions() { return transactions; }

    public String toString() {
        return getAccountType() + " account " + accountNumber
                + " | Branch: " + branch + " | Balance: BWP " + balance;
    }
}