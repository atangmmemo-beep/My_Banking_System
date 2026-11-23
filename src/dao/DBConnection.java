package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Handles the connection to the MySQL database.
// "static" means we can write DBConnection.getConnection() without using "new".
public class DBConnection {

    private static final String USER= "root";
    private static final String PASSWORD = "your_mysql_password";

    private static final String OPTIONS = "?sslMode=DISABLED&allowPublicKeyRetrieval=true";

    // connects to the MySQL server only (used once, to create the database)
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/" + OPTIONS;

    // connects to our own database called bank_db
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank_db" + OPTIONS;

    // "throws SQLException" = a warning that something can go wrong with the database.
    // The DAO classes deal with it using try / catch.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // Creates the database and the three tables if they do not exist yet
    public static void createTables() {
        String customers = "CREATE TABLE IF NOT EXISTS customers ("
                + "customer_id VARCHAR(10) PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "surname VARCHAR(50) NOT NULL, "
                + "address VARCHAR(100), "
                + "phone VARCHAR(20), "
                + "email VARCHAR(100), "
                + "username VARCHAR(30) NOT NULL UNIQUE, "
                + "password VARCHAR(100) NOT NULL)";

        String accounts = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_number VARCHAR(15) PRIMARY KEY, "
                + "account_type VARCHAR(12) NOT NULL, "
                + "balance DOUBLE NOT NULL, "
                + "branch VARCHAR(50), "
                + "date_opened VARCHAR(30) NOT NULL, "
                + "employer_name VARCHAR(80), "
                + "employer_address VARCHAR(100), "
                + "customer_id VARCHAR(10) NOT NULL, "
                + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))";

        String transactions = "CREATE TABLE IF NOT EXISTS transactions ("
                + "transaction_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "account_number VARCHAR(15) NOT NULL, "
                + "type VARCHAR(10) NOT NULL, "
                + "amount DOUBLE NOT NULL, "
                + "date_time VARCHAR(40) NOT NULL, "
                + "balance_after DOUBLE NOT NULL, "
                + "note VARCHAR(100), "
                + "FOREIGN KEY (account_number) REFERENCES accounts(account_number))";

        // make the database itself
        try (Connection con = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement st = con.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS bank_db");
        } catch (SQLException e) {
            System.out.println("Could not create the database: " + e.getMessage());
            return;
        }

        // Step 2: make the tables inside it
        // try (...) closes the connection for us when the block ends
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.execute(customers);
            st.execute(accounts);
            st.execute(transactions);
        } catch (SQLException e) {
            System.out.println("Could not create tables: " + e.getMessage());
        }
    }
}