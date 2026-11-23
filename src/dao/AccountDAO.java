package dao;

import model.Account;
import model.ChequeAccount;
import model.Customer;
import model.InvestmentAccount;
import model.SavingsAccount;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// All the SQL for the accounts table lives here.
public class AccountDAO {

    // CREATE: save a new account (and any transactions it already has, e.g. the first deposit)
    public boolean addAccount(Account a) {
        String sql = "INSERT INTO accounts (account_number, account_type, balance, branch, "
                + "date_opened, employer_name, employer_address, customer_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean saved = false;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getAccountType());
            ps.setDouble(3, a.getBalance());
            ps.setString(4, a.getBranch());
            ps.setString(5, a.getDateOpened());

            if (a.getAccountType().equals("CHEQUE")) {
                // (ChequeAccount) a  = treat this Account as a ChequeAccount so we can read employer details
                ChequeAccount cheque = (ChequeAccount) a;
                ps.setString(6, cheque.getEmployerName());
                ps.setString(7, cheque.getEmployerAddress());
            } else {
                ps.setString(6, null);   // savings and investment accounts have no employer
                ps.setString(7, null);
            }
            ps.setString(8, a.getOwner().getCustomerID());
            ps.executeUpdate();
            saved = true;
        } catch (SQLException e) {
            System.out.println("Could not save account: " + e.getMessage());
        }

        // save the transactions the account already has (for example "Initial deposit")
        if (saved) {
            TransactionDAO transactionDAO = new TransactionDAO();
            for (Transaction t : a.getTransactions()) {
                transactionDAO.addTransaction(a.getAccountNumber(), t);
            }
        }
        return saved;
    }

    // READ: all accounts that belong to one customer (with their transactions)
    public ArrayList<Account> getAccountsByCustomer(Customer owner) {
        ArrayList<Account> list = new ArrayList<Account>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, owner.getCustomerID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String number = rs.getString("account_number");
                String type = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                String branch = rs.getString("branch");
                String dateOpened = rs.getString("date_opened");

                // build the right kind of account for the type stored in the table
                Account a = null;
                if (type.equals("SAVINGS")) {
                    a = new SavingsAccount(number, balance, branch, dateOpened, owner);
                } else if (type.equals("INVESTMENT")) {
                    a = new InvestmentAccount(number, balance, branch, dateOpened, owner);
                } else if (type.equals("CHEQUE")) {
                    a = new ChequeAccount(number, balance, branch, dateOpened, owner,
                            rs.getString("employer_name"), rs.getString("employer_address"));
                }

                if (a != null) {
                    TransactionDAO transactionDAO = new TransactionDAO();
                    for (Transaction t : transactionDAO.getTransactionsByAccount(number)) {
                        a.addTransaction(t);
                    }
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not read accounts: " + e.getMessage());
        }
        return list;
    }

    // UPDATE: save the new balance after a deposit, withdrawal or interest payment
    public boolean updateBalance(Account a) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, a.getBalance());
            ps.setString(2, a.getAccountNumber());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not update balance: " + e.getMessage());
            return false;
        }
    }

    // DELETE: remove an account (its transactions must go first)
    public boolean deleteAccount(String accountNumber) {
        new TransactionDAO().deleteTransactionsByAccount(accountNumber);
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not delete account: " + e.getMessage());
            return false;
        }
    }

    // Makes the next account number: 1000001, 1000002, ...
    public String getNextAccountNumber() {
        String sql = "SELECT COUNT(*) FROM accounts";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return String.valueOf(1000001 + rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Could not make account number: " + e.getMessage());
        }
        return null;
    }
}