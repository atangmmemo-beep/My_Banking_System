package view;

import controller.AccountController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// The customer chooses an account type and opens a new account.
public class OpenAccountView extends BaseView {

    public OpenAccountView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);
    }

    public void show() {
        ComboBox<String> typeBox = new ComboBox<String>();
        typeBox.getItems().addAll("SAVINGS", "INVESTMENT", "CHEQUE");
        typeBox.setValue("SAVINGS");

        TextField depositField = new TextField();
        TextField employerNameField = new TextField();
        TextField employerAddressField = new TextField();
        employerNameField.setDisable(true);         // only needed for CHEQUE accounts
        employerAddressField.setDisable(true);

        Label rulesLabel = new Label("Investment accounts need at least BWP 500 to open.\n"
                + "Cheque accounts are for working people (employer details needed).");
        Label messageLabel = new Label();

        Button openButton = makeButton("Open Account");
        Button backButton = makeBackButton();

        // Switch the employer boxes on only when CHEQUE is chosen
        typeBox.setOnAction(e -> {
            boolean isCheque = "CHEQUE".equals(typeBox.getValue());
            employerNameField.setDisable(!isCheque);
            employerAddressField.setDisable(!isCheque);
        });

        openButton.setOnAction(e -> {
            boolean ok = accountController.openAccount(customerID, typeBox.getValue(),
                    depositField.getText(), employerNameField.getText(),
                    employerAddressField.getText());
            showMessage(messageLabel, accountController.getMessage(), ok);
        });

        GridPane form = makeForm();
        addRow(form, 0, "Account type:", typeBox);
        addRow(form, 1, "Opening deposit (BWP):", depositField);
        addRow(form, 2, "Employer name:", employerNameField);
        addRow(form, 3, "Employer address:", employerAddressField);

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Open an Account"), form, rulesLabel,
                openButton, backButton, messageLabel);
        showScreen(layout, 480, 520);
    }
}