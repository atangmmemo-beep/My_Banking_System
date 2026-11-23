import dao.DBConnection;
import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        DBConnection.createTables();        // makes sure the database and tables exist
        stage.setTitle("Banking System");
        new LoginView(stage).show();        // the first screen the user sees
    }

    public static void main(String[] args) {
        launch(args);
    }
}
