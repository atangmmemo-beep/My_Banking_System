package dao;

import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO = Data Access Object. All the SQL for the transactions table lives here.
// In the SQL, each ? is filled in later with ps.setString(...), ps.setDouble(...), etc.
public class TransactionDAO {

    // CREATE: save one transaction
    public boolean addTransaction(String accountNumber, Transaction t) {
        String sql = "INSERT INTO transactions "
                + "(account_number, type, amount, date_time, balance_after, note) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, t.getType());
            ps.setDouble(3, t.getAmount());
            ps.setString(4, t.getDateTime());
            ps.setDouble(5, t.getBalanceAfter());
            ps.setString(6, t.getNote());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not save transaction: " + e.getMessage());
            return false;
        }
    }

    // READ: all transactions of one account, oldest first
    public ArrayList<Transaction> getTransactionsByAccount(String accountNumber) {
        ArrayList<Transaction> list = new ArrayList<Transaction>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("note"));
                t.setTransactionID(rs.getInt("transaction_id"));
                t.setDateTime(rs.getString("date_time"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Could not read transactions: " + e.getMessage());
        }
        return list;
    }

    // DELETE: remove all transactions of one account
    public boolean deleteTransactionsByAccount(String accountNumber) {
        String sql = "DELETE FROM transactions WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not delete transactions: " + e.getMessage());
            return false;
        }
    }
}