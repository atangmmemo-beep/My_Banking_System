package controller;

import model.Customer;

// Registering a new customer and changing customer details.
public class CustomerController extends BaseController {

    public boolean registerCustomer(String firstName, String surname, String address,
                                    String phone, String email, String username,
                                    String password) {
        if (Validator.isEmpty(firstName) || Validator.isEmpty(surname)
                || Validator.isEmpty(address) || Validator.isEmpty(phone)
                || Validator.isEmpty(email) || Validator.isEmpty(username)
                || Validator.isEmpty(password)) {
            message = "Please fill in all the fields.";
            return false;
        }

        phone = phone.replace(" ", "");     // allow spaces in the phone number
        if (!Validator.isValidPhone(phone)) {
            message = "The phone number must contain digits only (7 to 15 digits).";
            return false;
        }
        if (!Validator.isValidEmail(email.trim())) {
            message = "Please enter a valid email address.";
            return false;
        }
        if (password.length() < 6) {
            message = "The password must be at least 6 characters long.";
            return false;
        }
        if (customerDAO.getCustomerByUsername(username.trim()) != null) {
            message = "That username is already taken.";
            return false;
        }

        String customerID = makeCustomerID();
        Customer customer = new Customer(customerID, firstName.trim(), surname.trim(),
                address.trim(), phone, email.trim(), username.trim(), password);

        if (customerDAO.addCustomer(customer)) {
            message = "Registration successful. Your customer ID is " + customerID + ".";
            return true;
        }
        message = "Could not register the customer. Please try again.";
        return false;
    }

    public boolean updateCustomer(String customerID, String firstName, String surname,
                                  String address, String phone, String email) {
        Customer customer = customerDAO.getCustomerById(customerID);
        if (customer == null) {
            message = "Customer not found.";
            return false;
        }
        if (Validator.isEmpty(firstName) || Validator.isEmpty(surname)
                || Validator.isEmpty(address) || Validator.isEmpty(phone)
                || Validator.isEmpty(email)) {
            message = "Please fill in all the fields.";
            return false;
        }
        phone = phone.replace(" ", "");
        if (!Validator.isValidPhone(phone)) {
            message = "The phone number must contain digits only (7 to 15 digits).";
            return false;
        }
        if (!Validator.isValidEmail(email.trim())) {
            message = "Please enter a valid email address.";
            return false;
        }

        customer.setFirstName(firstName.trim());
        customer.setSurname(surname.trim());
        customer.setAddress(address.trim());
        customer.setPhone(phone);
        customer.setEmail(email.trim());

        if (customerDAO.updateCustomer(customer)) {
            message = "Your details have been updated.";
            return true;
        }
        message = "Could not update your details.";
        return false;
    }

    // Returns the customer's details as text for the view to display
    public String getCustomerDetails(String customerID) {
        Customer customer = customerDAO.getCustomerById(customerID);
        if (customer == null) {
            return "Customer not found.";
        }
        return "Customer ID: " + customer.getCustomerID() + "\n"
                + "Name: " + customer.getFullName() + "\n"
                + "Address: " + customer.getAddress() + "\n"
                + "Phone: " + customer.getPhone() + "\n"
                + "Email: " + customer.getEmail() + "\n"
                + "Number of accounts: " + customer.getAccounts().size();
    }

    // Makes IDs like C001, C002, ... and skips any that are already used
    private String makeCustomerID() {
        int number = customerDAO.getAllCustomers().size() + 1;
        String id = formatID(number);
        while (customerDAO.getCustomerById(id) != null) {
            number++;
            id = formatID(number);
        }
        return id;
    }

    private String formatID(int number) {
        if (number < 10) {
            return "C00" + number;
        } else if (number < 100) {
            return "C0" + number;
        }
        return "C" + number;
    }
}
