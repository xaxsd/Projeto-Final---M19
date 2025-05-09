package main.java.controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.model.Player;
import main.java.model.PlayerDAO;
import javafx.fxml.FXMLLoader;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Будь ласка, заповніть всі поля");
            return;
        }

        Player player = PlayerDAO.login(username, password);
        if (player != null) {
            loadMainMenu(player);
        } else {
            messageLabel.setText("Невірний логін або пароль");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Будь ласка, заповніть всі поля");
            return;
        }

        if (password.length() < 6) {
            messageLabel.setText("Пароль повинен містити щонайменше 6 символів");
            return;
        }

        Player newPlayer = new Player(username, password, "");
        if (PlayerDAO.registerPlayer(newPlayer)) {
            messageLabel.setText("Реєстрація успішна! Виконайте вхід.");
        } else {
            messageLabel.setText("Помилка реєстрації: ім'я вже зайняте");
        }
    }

    private void loadMainMenu(Player player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainMenu.fxml"));
            Parent root = loader.load();
            
            MainMenuController controller = loader.getController();
            controller.setPlayerData(player);
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Головне меню");
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Помилка завантаження головного меню");
        }
    }
}