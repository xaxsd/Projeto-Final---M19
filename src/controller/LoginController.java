package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Player;
import model.PlayerDAO;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Player player = PlayerDAO.login(username, password);
        if (player != null) {
            messageLabel.setText("Welcome, " + player.getUsername() + "!");
            // TODO: Load game screen
        } else {
            messageLabel.setText("Invalid credentials.");
        }
    }

    @FXML
    private void handleRegister() {
        // Реєстрацію можна зробити в модальному вікні
        messageLabel.setText("Registration not implemented yet.");
    }
}
