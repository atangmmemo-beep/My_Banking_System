package dao;

import model.Account;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// All the SQL for the customers table lives here.
public class CustomerDAO {

    // CREATE: save a new customer
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address, "
                + "phone, email, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerID());
            ps.setString(2, c.getFirstName());
            ps.setString(3, c.getSurname());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getPhone());
            ps.setString(6, c.getEmail());
            ps.setString(7, c.getUsername());
            ps.setString(8, c.getPassword());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not save customer: " + e.getMessage());
            return false;
        }
    }

    // READ: find one customer by username (used for login). Returns null if not found.
    public Customer getCustomerByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return makeCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Could not read customer: " + e.getMessage());
        }
        return null;
    }

    // READ: find one customer by ID. Returns null if not found.
    public Customer getCustomerById(String customerID) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return makeCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Could not read customer: " + e.getMessage());
        }
        return null;
    }

    // READ: every customer
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> list = new ArrayList<Customer>();
        String sql = "SELECT * FROM customers ORDER BY customer_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(makeCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Could not read customers: " + e.getMessage());
        }
        return list;
    }

    // UPDATE: change a customer's details (name, address, phone, email)
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ?, "
                + "phone = ?, email = ? WHERE customer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getSurname());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getPhone());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getCustomerID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not update customer: " + e.getMessage());
            return false;
        }
    }

    // DELETE: remove a customer, together with their accounts and transactions
    public boolean deleteCustomer(String customerID) {
        Customer c = getCustomerById(customerID);
        if (c == null) {
            return false;
        }
        AccountDAO accountDAO = new AccountDAO();
        for (Account a : c.getAccounts()) {
            accountDAO.deleteAccount(a.getAccountNumber());
        }
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customerID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Could not delete customer: " + e.getMessage());
            return false;
        }
    }

    // Turns one row of the customers table into a Customer object, with their accounts
    private Customer makeCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer(
                rs.getString("customer_id"),
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"));

        AccountDAO accountDAO = new AccountDAO();
        for (Account a : accountDAO.getAccountsByCustomer(c)) {
            c.addAccount(a);
        }
        return c;
    }
}