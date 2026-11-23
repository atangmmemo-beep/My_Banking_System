package controller;

import model.Account;
import model.Transaction;

import java.util.ArrayList;

// Deposits, withdrawals and transaction history.
// Every change is done in the model first, then saved with the DAOs.
public class TransactionController extends BaseController {

    public boolean deposit(String customerID, String accountNumber,
                           String amountText, String note) {
        Account account = findAccount(customerID, accountNumber);
        if (account == null) {
            return false;               // findAccount already set the message
        }
        if (!Validator.isPositiveNumber(amountText)) {
            message = "Please enter an amount greater than zero.";
            return false;
        }
        double amount = Validator.toNumber(amountText);

        // Uses the OVERLOADED deposit methods from the model
        boolean done;
        if (Validator.isEmpty(note)) {
            done = account.deposit(amount);
        } else {
            done = account.deposit(amount, note.trim());
        }

        if (!done) {
            message = "The deposit could not be made.";
            return false;
        }
        saveChanges(account);
        message = "Deposited BWP " + amount + ". New balance: BWP " + account.getBalance();
        return true;
    }

    public boolean withdraw(String customerID, String accountNumber, String amountText) {
        Account account = findAccount(customerID, accountNumber);
        if (account == null) {
            return false;
        }
        if (!Validator.isPositiveNumber(amountText)) {
            message = "Please enter an amount greater than zero.";
            return false;
        }
        double amount = Validator.toNumber(amountText);

        // Each account type decides for itself (polymorphism)
        if (account.withdraw(amount)) {
            saveChanges(account);
            message = "Withdrew BWP " + amount + ". New balance: BWP " + account.getBalance();
            return true;
        }

        if (account.getAccountType().equals("SAVINGS")) {
            message = "Savings accounts do not allow withdrawals.";
        } else {
            message = "Not enough money. Your balance is BWP " + account.getBalance();
        }
        return false;
    }

    // Each transaction as one line of text, oldest first
    public ArrayList<String> getTransactionHistory(String customerID, String accountNumber) {
        ArrayList<String> lines = new ArrayList<String>();
        Account account = findAccount(customerID, accountNumber);
        if (account == null) {
            return lines;               // empty list, message explains why
        }
        for (Transaction t : account.getTransactions()) {
            lines.add(t.toString());
        }
        if (lines.size() == 0) {
            message = "There are no transactions on this account yet.";
        }
        return lines;
    }
}
