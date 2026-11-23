package controller;

import model.Account;
import model.Customer;
import model.InterestBearing;

// Pays the monthly interest to every account that earns interest.
// Run it once a month (running it twice pays the interest twice!).
public class InterestController extends BaseController {

    public boolean payInterestToAll() {
        int count = 0;

        for (Customer customer : customerDAO.getAllCustomers()) {
            for (Account account : customer.getAccounts()) {

                // Only Savings and Investment accounts implement InterestBearing
                if (account instanceof InterestBearing) {
                    InterestBearing interestAccount = (InterestBearing) account;

                    int before = account.getTransactions().size();
                    interestAccount.payInterest();

                    // an empty account earns nothing, so no new transaction appears
                    if (account.getTransactions().size() > before) {
                        saveChanges(account);
                        count++;
                    }
                }
            }
        }

        message = "Interest was paid to " + count + " account(s).";
        return true;
    }
}