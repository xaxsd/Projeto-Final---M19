package main.java.model;
import java.sql.*;
import java.security.*;
import java.util.Base64;

public class PlayerDAO {
    
    // Метод для реєстрації нового гравця
    public static boolean registerPlayer(Player player) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(player.getPassword(), salt);
        
        String sql = "INSERT INTO Players (username, password, email, salt) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, player.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, player.getEmail());
            stmt.setString(4, salt);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Помилка реєстрації: " + e.getMessage());
            return false;
        }
    }

    // Метод для входу гравця
    public static Player login(String username, String password) {
        String sql = "SELECT * FROM Players WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String salt = rs.getString("salt");
                String inputHash = hashPassword(password, salt);
                
                if (storedHash.equals(inputHash)) {
                    return new Player(
                            rs.getInt("player_id"),
                            rs.getString("username"),
                            storedHash,
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Помилка входу: " + e.getMessage());
        }
        return null;
    }

    // Перевірка наявності імені користувача
    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Players WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Помилка перевірки імені: " + e.getMessage());
        }
        return false;
    }
    
    // Генерація солі для хешування паролю
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Хешування паролю з використанням солі
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Помилка хешування паролю", e);
        }
    }

    // Отримання підключення до бази даних
    private static Connection getConnection() throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/project_m19?useSSL=false&serverTimezone=UTC";
        String USER = "root"; 
        String PASSWORD = "ваш_пароль";
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}