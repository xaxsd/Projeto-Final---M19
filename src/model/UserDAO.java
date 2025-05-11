package model;

import java.sql.*;
import util.DatabaseConnection;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        try {
            connection = DatabaseConnection.getConnection(); // Підключення до бази даних
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    // Метод для перевірки, чи є користувач з таким логіном і паролем
    public boolean authenticate(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);  // Вставка логіну користувача в запит
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password"); // Отримуємо збережений пароль
                return storedPassword.equals(password); // Порівнюємо введений пароль з збереженим
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Якщо користувач не знайдений або паролі не співпали
    }

    // Метод для додавання нового користувача (за потребою)
    public void addUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Для безпеки використовуйте хешування пароля перед збереженням
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
