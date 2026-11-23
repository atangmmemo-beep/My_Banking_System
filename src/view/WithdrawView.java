package view;

import controller.TransactionController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Withdraw money from one of the customer's accounts.
public class WithdrawView extends BaseView {

    private TransactionController transactionController = new TransactionController();

    public WithdrawView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);

    }

    public void show() {
        ComboBox<String> accountBox = makeAccountBox();
        TextField amountField = new TextField();
        Label summaryLabel = new Label();
        Label messageLabel = new Label();

        Button withdrawButton = makeButton("Withdraw");
        Button backButton = makeBackButton();

        showSummary(summaryLabel, accountBox);
        accountBox.setOnAction(e -> showSummary(summaryLabel, accountBox));

        withdrawButton.setOnAction(e -> {
            boolean ok = transactionController.withdraw(customerID, accountBox.getValue(),
                    amountField.getText());
            showMessage(messageLabel, transactionController.getMessage(), ok);
            showSummary(summaryLabel, accountBox);
            if (ok) {
                amountField.setText("");
            }
        });

        GridPane form = makeForm();
        addRow(form, 0, "Account:", accountBox);
        addRow(form, 1, "Amount (BWP):", amountField);

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Withdraw"), form, summaryLabel,
                withdrawButton, backButton, messageLabel);
        showScreen(layout, 480, 440);
    }
}