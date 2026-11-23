package model;

import java.time.LocalDate;
import java.util.ArrayList;

// A bank customer. One customer can have many accounts.
public class Customer {

    private String customerID;
    private String firstName;
    private String surname;
    private String address;
    private String phone;
    private String email;
    private String username;
    private String password;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    public Customer(String customerID, String firstName, String surname, String address,
                    String phone, String email, String username, String password) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Opens a SAVINGS or INVESTMENT account.
    // Returns the new account, or null if e violater di rules.
    public Account openAccount(String type, String accountNumber, String branch,
                               double initialDeposit) {
        String today = LocalDate.now().toString();
        Account account = null;

        if (type.equalsIgnoreCase("SAVINGS")) {
            account = new SavingsAccount(accountNumber, 0, branch, today, this);
        } else if (type.equalsIgnoreCase("INVESTMENT")
                && initialDeposit >= InvestmentAccount.MIN_OPENING) {
            account = new InvestmentAccount(accountNumber, 0, branch, today, this);
        }

        if (account != null) {
            if (initialDeposit > 0) {
                account.deposit(initialDeposit, "Initial deposit");
            }
            accounts.add(account);
        }
        return account;
    }

    // OVERLOADED version for a CHEQUE account: it also needs the employer details.
    public Account openAccount(String type, String accountNumber, String branch,
                               double initialDeposit, String employerName,
                               String employerAddress) {
        if (!type.equalsIgnoreCase("CHEQUE")
                || employerName == null || employerName.equals("")) {
            return null;    // cheque accounts are only for working people
        }
        String today = LocalDate.now().toString();
        Account account = new ChequeAccount(accountNumber, 0, branch, today, this,
                employerName, employerAddress);
        if (initialDeposit > 0) {
            account.deposit(initialDeposit, "Initial deposit");
        }
        accounts.add(account);
        return account;
    }

    // Used later when we load a customer's accounts from the database
    public void addAccount(Account account) {
        accounts.add(account);
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    // Finds one of this customer's accounts by its number (null if not found)
    public Account getAccount(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    // Getters and setters
    public String getCustomerID() { return customerID; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public String toString() {
        return customerID + " - " + getFullName();
    }
}