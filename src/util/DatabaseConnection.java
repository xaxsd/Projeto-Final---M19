package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Замість цього використовувати ваші власні параметри підключення до БД
    private static final String URL = "jdbc:mysql://localhost:3306/sales_management";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Метод для отримання підключення до бази даних
    public static Connection getConnection() throws SQLException {
        try {
            // Завантаження драйвера MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found", e);
        }
    }
}
