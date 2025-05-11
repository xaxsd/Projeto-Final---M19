package main;  // Додаємо оголошення пакету

import javafx.application.Application;
import app.LoginScreen;

public class Main {
    public static void main(String[] args) {
        // Запускаємо JavaFX додаток
        Application.launch(LoginScreen.class, args);
    }
}
