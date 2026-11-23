package dao;

import model.Account;
import model.Customer;
import model.InterestBearing;
import model.Transaction;

import java.util.ArrayList;

// Run this class ONCE (right-click -> Run 'SampleData.main()') to fill the database
// with 10 sample customers and their accounts.
public class SampleData {

    private static CustomerDAO customerDAO = new CustomerDAO();
    private static AccountDAO accountDAO = new AccountDAO();
    private static TransactionDAO transactionDAO = new TransactionDAO();

    public static void main(String[] args) {
        DBConnection.createTables();

        if (customerDAO.getAllCustomers().size() > 0) {
            System.out.println("The database already has data. Nothing added.");
            return;
        }

        Customer c1 = makeCustomer("C001", "Thabo", "Molefe", "Block 8, Gaborone", "71234567", "thabo@email.com", "thabo");
        Account a1 = openAccount(c1, "SAVINGS", 1500, null, null);
        deposit(a1, 500);
        payInterest(a1);

        Customer c2 = makeCustomer("C002", "Neo", "Kgosi", "Broadhurst, Gaborone", "72345678", "neo@email.com", "neo");
        Account a2 = openAccount(c2, "CHEQUE", 3000, "Sunrise Traders", "Main Mall, Gaborone");
        withdraw(a2, 800);

        Customer c3 = makeCustomer("C003", "Kabelo", "Seretse", "Phakalane, Gaborone", "73456789", "kabelo@email.com", "kabelo");
        Account a3 = openAccount(c3, "INVESTMENT", 2000, null, null);
        withdraw(a3, 300);
        payInterest(a3);
        openAccount(c3, "SAVINGS", 700, null, null);

        Customer c4 = makeCustomer("C004", "Boitumelo", "Modise", "Mogoditshane", "74567890", "boitumelo@email.com", "boitumelo");
        openAccount(c4, "SAVINGS", 1200, null, null);

        Customer c5 = makeCustomer("C005", "Tshepo", "Dikgang", "Tlokweng", "75678901", "tshepo@email.com", "tshepo");
        openAccount(c5, "CHEQUE", 5000, "Kalahari Mining", "Industrial Site, Francistown");
        openAccount(c5, "INVESTMENT", 1000, null, null);

        Customer c6 = makeCustomer("C006", "Lesego", "Mogapi", "Francistown", "76789012", "lesego@email.com", "lesego");
        Account a6 = openAccount(c6, "INVESTMENT", 800, null, null);
        payInterest(a6);

        Customer c7 = makeCustomer("C007", "Naledi", "Phiri", "Maun", "77890123", "naledi@email.com", "naledi");
        Account a7 = openAccount(c7, "SAVINGS", 300, null, null);
        deposit(a7, 200);

        Customer c8 = makeCustomer("C008", "Onkabetse", "Tau", "Palapye", "71901234", "onkabetse@email.com", "onkabetse");
        Account a8 = openAccount(c8, "CHEQUE", 2500, "Palapye Clinic", "Main Road, Palapye");
        withdraw(a8, 500);
        deposit(a8, 1000);

        Customer c9 = makeCustomer("C009", "Kagiso", "Moremi", "Serowe", "72012345", "kagiso@email.com", "kagiso");
        openAccount(c9, "SAVINGS", 950, null, null);
        openAccount(c9, "INVESTMENT", 600, null, null);
        openAccount(c9, "CHEQUE", 1800, "Serowe Hardware", "Industrial Area, Serowe");

        Customer c10 = makeCustomer("C010", "Mpho", "Ntsima", "Kasane", "73123456", "mpho@email.com", "mpho");
        Account a10 = openAccount(c10, "SAVINGS", 4000, null, null);
        payInterest(a10);

        // Read everything back from the database to prove it worked
        System.out.println("Customers saved in the database:");
        for (Customer c : customerDAO.getAllCustomers()) {
            System.out.println(c);
            for (Account a : c.getAccounts()) {
                System.out.println("    " + a);
            }
        }
    }


    private static Customer makeCustomer(String id, String first, String surname, String address,
                                         String phone, String email, String username) {
        Customer c = new Customer(id, first, surname, address, phone, email, username, "pass1234");
        customerDAO.addCustomer(c);
        return c;
    }

    // Opens the account in memory (model) and then saves it in the database (DAO)
    private static Account openAccount(Customer c, String type, double deposit,
                                       String employer, String employerAddress) {
        String number = accountDAO.getNextAccountNumber();
        Account a;
        if (type.equals("CHEQUE")) {
            a = c.openAccount(type, number, "Main Branch", deposit, employer, employerAddress);
        } else {
            a = c.openAccount(type, number, "Main Branch", deposit);
        }
        accountDAO.addAccount(a);
        return a;
    }

    private static void deposit(Account a, double amount) {
        a.deposit(amount);
        accountDAO.updateBalance(a);
        saveLastTransaction(a);
    }

    private static void withdraw(Account a, double amount) {
        a.withdraw(amount);
        accountDAO.updateBalance(a);
        saveLastTransaction(a);
    }

    private static void payInterest(Account a) {
        // Only savings and investment accounts are InterestBearing
        if (a instanceof InterestBearing) {
            InterestBearing account = (InterestBearing) a;
            account.payInterest();
            accountDAO.updateBalance(a);
            saveLastTransaction(a);
        }
    }

    // The newest transaction is the last one in the account's list
    private static void saveLastTransaction(Account a) {
        ArrayList<Transaction> list = a.getTransactions();
        Transaction last = list.get(list.size() - 1);
        transactionDAO.addTransaction(a.getAccountNumber(), last);
    }
}