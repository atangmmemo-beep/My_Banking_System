package view;

import controller.TransactionController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Deposit money into one of the customer's accounts.
public class DepositView extends BaseView {

    private TransactionController transactionController = new TransactionController();

    public DepositView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);
    }

    public void show() {
        ComboBox<String> accountBox = makeAccountBox();
        TextField amountField = new TextField();
        TextField noteField = new TextField();
        Label summaryLabel = new Label();
        Label messageLabel = new Label();

        Button depositButton = makeButton("Deposit");
        Button backButton = makeBackButton();

        showSummary(summaryLabel, accountBox);
        accountBox.setOnAction(e -> showSummary(summaryLabel, accountBox));

        depositButton.setOnAction(e -> {
            boolean ok = transactionController.deposit(customerID, accountBox.getValue(),
                    amountField.getText(), noteField.getText());
            showMessage(messageLabel, transactionController.getMessage(), ok);
            showSummary(summaryLabel, accountBox);      // show the new balance
            if (ok) {
                amountField.setText("");
                noteField.setText("");
            }
        });

        GridPane form = makeForm();
        addRow(form, 0, "Account:", accountBox);
        addRow(form, 1, "Amount (BWP):", amountField);
        addRow(form, 2, "Note (optional):", noteField);

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Deposit"), form, summaryLabel,
                depositButton, backButton, messageLabel);
        showScreen(layout, 480, 480);
    }
}