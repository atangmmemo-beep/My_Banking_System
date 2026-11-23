package view;

import controller.LoginController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// The first screen: the customer logs in or chooses to register.
public class LoginView extends BaseView {

    private LoginController loginController = new LoginController();

    public LoginView(Stage stage) {
        super(stage);
    }

    public void show() {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label messageLabel = new Label();

        Button loginButton = makeButton("Login");
        Button registerButton = makeButton("Register as a new customer");

        // e -> { ... } means: "when the button is clicked, run this code"
        loginButton.setOnAction(e -> {
            boolean ok = loginController.login(usernameField.getText(), passwordField.getText());
            if (ok) {
                new MenuView(stage, loginController.getCustomerID(),
                        loginController.getCustomerName()).show();
            } else {
                showMessage(messageLabel, loginController.getMessage(), false);
            }
        });

        registerButton.setOnAction(e -> new RegisterCustomerView(stage).show());

        GridPane form = makeForm();
        addRow(form, 0, "Username:", usernameField);
        addRow(form, 1, "Password:", passwordField);

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Banking System"), new Label("Please log in"),
                form, loginButton, registerButton, messageLabel);
        showScreen(layout, 420, 400);
    }
}