package view;

import controller.CustomerController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// A new customer fills in this form to register.
public class RegisterCustomerView extends BaseView {

    private CustomerController customerController = new CustomerController();

    public RegisterCustomerView(Stage stage) {
        super(stage);
    }

    public void show() {
        TextField firstNameField = new TextField();
        TextField surnameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label messageLabel = new Label();

        Button registerButton = makeButton("Register");
        Button backButton = makeButton("Back to Login");

        registerButton.setOnAction(e -> {
            boolean ok = customerController.registerCustomer(
                    firstNameField.getText(), surnameField.getText(), addressField.getText(),
                    phoneField.getText(), emailField.getText(), usernameField.getText(),
                    passwordField.getText());
            showMessage(messageLabel, customerController.getMessage(), ok);
        });

        backButton.setOnAction(e -> new LoginView(stage).show());

        GridPane form = makeForm();
        addRow(form, 0, "First name:", firstNameField);
        addRow(form, 1, "Surname:", surnameField);
        addRow(form, 2, "Address:", addressField);
        addRow(form, 3, "Phone:", phoneField);
        addRow(form, 4, "Email:", emailField);
        addRow(form, 5, "Username:", usernameField);
        addRow(form, 6, "Password:", passwordField);

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Register"), form, registerButton,
                backButton, messageLabel);
        showScreen(layout, 460, 560);
    }
}