import model.UserDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    private UserDAO userDAO = new UserDAO();

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (userDAO.authenticate(username, password)) {
                // Якщо логін успішний, перехід до головного вікна
                showMainScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials", ButtonType.OK);
                alert.showAndWait();
            }
        });

        vbox.getChildren().addAll(usernameField, passwordField, loginButton);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainScreen() {
        // Додати код для переходу до головного екрану після успішного логіну
    }

    public static void main(String[] args) {
        launch(args);
    }
}
