package app;

import model.UserDAO;
import java.util.Scanner;

public class Authentication {

    private UserDAO userDAO;

    public Authentication() {
        userDAO = new UserDAO();  // Ініціалізуємо DAO для роботи з користувачами
    }

    // Метод для аутентифікації користувача
    public boolean login(String username, String password) {
        return userDAO.authenticate(username, password);  // Викликаємо метод з DAO для перевірки
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (auth.login(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }

        scanner.close();
    }
}
