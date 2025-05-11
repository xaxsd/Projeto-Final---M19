package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe utilitária para gerenciamento de conexões com o banco de dados MySQL.
 * Implementa o padrão Singleton para reutilização de conexões.
 */
public class DatabaseConnection {
    // Configurações padrão (devem ser modificadas conforme seu ambiente)
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/sales_management";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";
    
    private static Connection connection = null;

    /**
     * Obtém uma conexão com o banco de dados (Singleton)
     * @return Objeto Connection configurado
     * @throws SQLException Se ocorrer erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carrega o driver JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Configura propriedades adicionais
                Properties properties = new Properties();
                properties.setProperty("user", DEFAULT_USER);
                properties.setProperty("password", DEFAULT_PASSWORD);
                properties.setProperty("useSSL", "false");
                properties.setProperty("autoReconnect", "true");
                properties.setProperty("characterEncoding", "UTF-8");
                properties.setProperty("useUnicode", "true");
                
                // Estabelece a conexão
                connection = DriverManager.getConnection(DEFAULT_URL, properties);
                
                // Configurações adicionais da conexão
                connection.setAutoCommit(false); // Desativa auto-commit para controle transacional
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado", e);
            }
        }
        return connection;
    }

    /**
     * Fecha a conexão com o banco de dados
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    /**
     * Testa a conexão com o banco de dados
     * @return true se a conexão foi estabelecida com sucesso
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn.isValid(2); // Testa com timeout de 2 segundos
        } catch (SQLException e) {
            System.err.println("Falha no teste de conexão: " + e.getMessage());
            return false;
        }
    }

    /**
     * Executa uma transação no banco de dados
     * @param transaction Lambda contendo as operações a serem executadas
     * @throws SQLException Se ocorrer erro durante a transação
     */
    public static void executeTransaction(SQLTransaction transaction) throws SQLException {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
            transaction.execute(conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @FunctionalInterface
    public interface SQLTransaction {
        void execute(Connection connection) throws SQLException;
    }
}