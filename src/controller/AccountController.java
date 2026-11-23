package controller;

import model.Account;
import model.Customer;
import model.InvestmentAccount;

import java.util.ArrayList;

// Opening accounts and looking at them.
public class AccountController extends BaseController {

    private static final String BRANCH = "Main Branch";

    // Opens a SAVINGS, INVESTMENT or CHEQUE account for the customer.
    // employerName and employerAddress are only needed for CHEQUE (the view can pass "").
    public boolean openAccount(String customerID, String type, String depositText,
                               String employerName, String employerAddress) {
        Customer customer = customerDAO.getCustomerById(customerID);
        if (customer == null) {
            message = "Customer not found. Please log in again.";
            return false;
        }
        if (Validator.isEmpty(type)) {
            message = "Please choose an account type.";
            return false;
        }
        if (!Validator.isNumber(depositText) || Validator.toNumber(depositText) < 0) {
            message = "Please enter a valid opening deposit.";
            return false;
        }
        double deposit = Validator.toNumber(depositText);

        String accountNumber = accountDAO.getNextAccountNumber();
        if (accountNumber == null) {
            message = "Could not create an account number. Please try again.";
            return false;
        }

        Account account = null;

        if (type.equalsIgnoreCase("INVESTMENT")) {
            if (deposit < InvestmentAccount.MIN_OPENING) {
                message = "An investment account needs an opening deposit of at least BWP "
                        + InvestmentAccount.MIN_OPENING + ".";
                return false;
            }
            account = customer.openAccount(type, accountNumber, BRANCH, deposit);

        } else if (type.equalsIgnoreCase("CHEQUE")) {
            if (Validator.isEmpty(employerName) || Validator.isEmpty(employerAddress)) {
                message = "A cheque account is only for people who work. "
                        + "Please enter your employer's name and address.";
                return false;
            }
            account = customer.openAccount(type, accountNumber, BRANCH, deposit,
                    employerName.trim(), employerAddress.trim());

        } else {
            account = customer.openAccount(type, accountNumber, BRANCH, deposit);
        }

        if (account == null) {
            message = "Could not open the account. Check the account type.";
            return false;
        }

        if (accountDAO.addAccount(account)) {
            message = "Your " + account.getAccountType() + " account " + accountNumber
                    + " was opened successfully.";
            return true;
        }
        message = "Could not save the account. Please try again.";
        return false;
    }

    // The account numbers of this customer (for a drop-down list in the view)
    public ArrayList<String> getAccountNumbers(String customerID) {
        ArrayList<String> numbers = new ArrayList<String>();
        Customer customer = customerDAO.getCustomerById(customerID);
        if (customer != null) {
            for (Account account : customer.getAccounts()) {
                numbers.add(account.getAccountNumber());
            }
        }
        return numbers;
    }

    // Text describing one account, including its balance
    public String getAccountSummary(String customerID, String accountNumber) {
        Account account = findAccount(customerID, accountNumber);
        if (account == null) {
            return message;
        }
        return account.toString();
    }
}
