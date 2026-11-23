package view;

import controller.AccountController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// The PARENT of all the screens. It holds the window (Stage) and small helper methods.
// A view only shows things and collects what the user types.
// It never contains business rules: it asks a controller to do the work.
public class BaseView {

    protected Stage stage;
    protected String customerID;        // who is logged in (empty before login)
    protected String customerName;
    protected AccountController accountController = new AccountController();

    // Used by the screens shown BEFORE login
    public BaseView(Stage stage) {
        this.stage = stage;
    }

    // Used by the screens shown AFTER login
    public BaseView(Stage stage, String customerID, String customerName) {
        this.stage = stage;
        this.customerID = customerID;
        this.customerName = customerName;
    }

    protected Label makeTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        return label;
    }

    protected Button makeButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(240);
        return button;
    }

    // A grid for "Label: [text box]" rows
    protected GridPane makeForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    protected void addRow(GridPane grid, int row, String labelText, Node field) {
        grid.add(new Label(labelText), 0, row);
        grid.add(field, 1, row);
    }

    // Shows a message in green (success) or red (problem)
    protected void showMessage(Label label, String text, boolean success) {
        label.setText(text);
        label.setWrapText(true);
        if (success) {
            label.setStyle("-fx-text-fill: green;");
        } else {
            label.setStyle("-fx-text-fill: red;");
        }
    }

    // Puts a layout into the window
    protected void showScreen(VBox layout, double width, double height) {
        layout.setPadding(new Insets(20));
        layout.setSpacing(12);
        layout.setAlignment(Pos.TOP_CENTER);
        stage.setScene(new Scene(layout, width, height));
        stage.show();
    }

    // A drop-down list holding the logged-in customer's account numbers
    protected ComboBox<String> makeAccountBox() {
        ComboBox<String> box = new ComboBox<String>();
        box.getItems().addAll(accountController.getAccountNumbers(customerID));
        if (box.getItems().size() > 0) {
            box.setValue(box.getItems().get(0));
        }
        return box;
    }

    // Shows the details and balance of the account chosen in the drop-down
    protected void showSummary(Label label, ComboBox<String> box) {
        if (box.getValue() == null) {
            label.setText("You have no accounts yet. Open one first.");
        } else {
            label.setText(accountController.getAccountSummary(customerID, box.getValue()));
        }
    }

    protected Button makeBackButton() {
        Button back = makeButton("Back to Menu");
        back.setOnAction(e -> goToMenu());
        return back;
    }

    protected void goToMenu() {
        new MenuView(stage, customerID, customerName).show();
    }
}