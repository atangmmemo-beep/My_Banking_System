package view;

import controller.CustomerController;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Shows the customer's details and all of their accounts with balances.
public class AccountView extends BaseView {

    private CustomerController customerController = new CustomerController();

    public AccountView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);
    }

    public void show() {
        String text = customerController.getCustomerDetails(customerID) + "\n\nYour accounts:\n";

        ArrayList<String> numbers = accountController.getAccountNumbers(customerID);
        if (numbers.size() == 0) {
            text = text + "\nYou have no accounts yet.";
        }
        for (String number : numbers) {
            text = text + "\n" + accountController.getAccountSummary(customerID, number);
        }

        TextArea detailsArea = new TextArea(text);
        detailsArea.setEditable(false);         // the user can read it but not change it
        detailsArea.setPrefHeight(300);

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("My Details and Accounts"), detailsArea,
                makeBackButton());
        showScreen(layout, 560, 500);
    }
}
