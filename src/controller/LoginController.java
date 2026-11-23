package controller;

import model.Customer;

// Handles logging in. The view gives us what the user typed; we check it.
public class LoginController extends BaseController {

    private String customerID;      // set when the login works
    private String customerName;

    public boolean login(String username, String password) {
        if (Validator.isEmpty(username) || Validator.isEmpty(password)) {
            message = "Please enter your username and password.";
            return false;
        }

        Customer customer = customerDAO.getCustomerByUsername(username.trim());

        // Same message for "no such user" and "wrong password" (safer)
        if (customer == null || !customer.getPassword().equals(password)) {
            message = "Wrong username or password.";
            return false;
        }

        customerID = customer.getCustomerID();
        customerName = customer.getFullName();
        message = "Welcome, " + customerName + ".";
        return true;
    }

    public void logout() {
        customerID = null;
        customerName = null;
        message = "You have been logged out.";
    }

    // The view keeps this ID and sends it to the other controllers
    public String getCustomerID() { return customerID; }
    public String getCustomerName() { return customerName; }
}
