package model;

import java.time.LocalDateTime;

// One record of money going in or out of an account.
// type is "DEPOSIT", "WITHDRAW" or "INTEREST".
public class Transaction {

    // private = only this class can touch these directly (encapsulation)
    private int transactionID;
    private String type;
    private double amount;
    private String dateTime;
    private double balanceAfter;
    private String note;

    // Constructor: runs when we write  new Transaction(...)
    public Transaction(String type, double amount, double balanceAfter, String note) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.note = note;
        this.dateTime = LocalDateTime.now().toString();   // current date and time as text
    }

    // Getters: let other classes READ the private data
    public int getTransactionID() { return transactionID; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDateTime() { return dateTime; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getNote() { return note; }

    // Setters: only for the values the database gives back later
    public void setTransactionID(int transactionID) { this.transactionID = transactionID; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String toString() {
        return dateTime + " | " + type + " | BWP " + amount + " | balance BWP " + balanceAfter + " | " + note;
    }
}