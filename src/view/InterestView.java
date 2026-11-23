package view;

import controller.InterestController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Pays the monthly interest to all savings and investment accounts.
public class InterestView extends BaseView {

    private InterestController interestController = new InterestController();

    public InterestView(Stage stage, String customerID, String customerName) {
        super(stage, customerID, customerName);
    }

    public void show() {
        Label infoLabel = new Label("Savings accounts earn 0.05% and investment accounts earn 5%.\n"
                + "Run this once a month. Running it twice pays the interest twice.");
        Label messageLabel = new Label();
        Button payButton = makeButton("Pay Interest Now");

        payButton.setOnAction(e -> {
            boolean ok = interestController.payInterestToAll();
            showMessage(messageLabel, interestController.getMessage(), ok);
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(makeTitle("Pay Monthly Interest"), infoLabel,
                payButton, makeBackButton(), messageLabel);
        showScreen(layout, 520, 360);
    }
}