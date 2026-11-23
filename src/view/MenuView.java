package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// The main menu shown after logging in. Each button opens another screen.
public class MenuView extends BaseView {

    public MenuView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);

    }

    public void show() {
        Button accountsButton = makeButton("My Details and Accounts");
        Button openButton = makeButton("Open an Account");
        Button depositButton = makeButton("Deposit");
        Button withdrawButton = makeButton("Withdraw");
        Button historyButton = makeButton("Transaction History");
        Button interestButton = makeButton("Pay Monthly Interest");
        Button logoutButton = makeButton("Log Out");

        accountsButton.setOnAction(e -> new AccountView(stage, customerID, customerName).show());
        openButton.setOnAction(e -> new OpenAccountView(stage, customerID, customerName).show());
        depositButton.setOnAction(e -> new DepositView(stage, customerID, customerName).show());
        withdrawButton.setOnAction(e -> new WithdrawView(stage, customerID, customerName).show());
        historyButton.setOnAction(e -> new TransactionHistoryView(stage, customerID, customerName).show());
        interestButton.setOnAction(e -> new InterestView(stage, customerID, customerName).show());
        logoutButton.setOnAction(e -> new LoginView(stage).show());

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Welcome, " + customerName),
                new Label("What would you like to do?"),
                accountsButton, openButton, depositButton, withdrawButton,
                historyButton, interestButton, logoutButton);
        showScreen(layout, 420, 520);
    }
}
