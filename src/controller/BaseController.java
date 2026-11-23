package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import model.Account;
import model.Customer;
import model.Transaction;

import java.util.ArrayList;

// The PARENT of all the controllers. It holds the things they all need.
// Controllers are the middle layer:  View  ->  Controller  ->  Model + DAO
public class BaseController {

    // The DAOs are how controllers reach the database
    protected CustomerDAO customerDAO = new CustomerDAO();
    protected AccountDAO accountDAO = new AccountDAO();
    protected TransactionDAO transactionDAO = new TransactionDAO();

    // After every action the controller leaves a message here.
    // The view reads it with getMessage() and shows it to the user.
    protected String message = "";

    public String getMessage() {
        return message;
    }

    // Finds one of this customer's accounts. Returns null (and sets a message) if it fails.
    protected Account findAccount(String customerID, String accountNumber) {
        Customer customer = customerDAO.getCustomerById(customerID);
        if (customer == null) {
            message = "Customer not found. Please log in again.";
            return null;
        }
        if (Validator.isEmpty(accountNumber)) {
            message = "Please choose an account.";
            return null;
        }
        Account account = customer.getAccount(accountNumber);
        if (account == null) {
            message = "Account not found.";
            return null;
        }
        return account;
    }

    // Saves the new balance and the newest transaction to the database
    protected void saveChanges(Account account) {
        accountDAO.updateBalance(account);
        ArrayList<Transaction> list = account.getTransactions();
        if (list.size() > 0) {
            Transaction last = list.get(list.size() - 1);
            transactionDAO.addTransaction(account.getAccountNumber(), last);
        }
    }
}
