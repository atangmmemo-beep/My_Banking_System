package view;

import controller.TransactionController;
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Shows every transaction of the account the customer picks.
public class TransactionHistoryView extends BaseView {

    private TransactionController transactionController = new TransactionController();

    public TransactionHistoryView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);
    }

    public void show() {
        ComboBox<String> accountBox = makeAccountBox();
        TextArea historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setPrefHeight(300);

        showHistory(accountBox, historyArea);
        accountBox.setOnAction(e -> showHistory(accountBox, historyArea));

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Transaction History"), new Label("Account:"),
                accountBox, historyArea, makeBackButton());
        showScreen(layout, 700, 520);
    }

    // Fills the text area with one line per transaction
    private void showHistory(ComboBox<String> accountBox, TextArea historyArea) {
        ArrayList<String> lines = transactionController.getTransactionHistory(
                customerID, accountBox.getValue());

        if (lines.size() == 0) {
            historyArea.setText(transactionController.getMessage());
            return;
        }
        String text = "";
        for (String line : lines) {
            text = text + line + "\n";
        }
        historyArea.setText(text);
    }
}