package app;

import model.UserDAO;
import java.util.Scanner;

/**
 * Classe responsável pela autenticação de usuários no sistema.
 * Oferece uma interface de linha de comando para login.
 */
public class Authentication {
    private final UserDAO userDAO;

    /**
     * Construtor que inicializa o DAO de usuários.
     */
    public Authentication() {
        this.userDAO = new UserDAO();
    }

    /**
     * Autentica um usuário com nome e senha.
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return true se autenticação for bem-sucedida
     */
    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            System.err.println("Nome de usuário e senha são obrigatórios!");
            return false;
        }
        return userDAO.authenticate(username, password);
    }

    /**
     * Ponto de entrada para autenticação via console.
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();

        try {
            System.out.println("=== Sistema de Autenticação ===");
            
            System.out.print("Usuário: ");
            String username = scanner.nextLine().trim();
            
            System.out.print("Senha: ");
            String password = scanner.nextLine().trim();

            if (auth.login(username, password)) {
                System.out.println("\nAutenticação bem-sucedida! Bem-vindo, " + username + "!");
            } else {
                System.err.println("\nFalha na autenticação. Credenciais inválidas.");
            }
        } finally {
            scanner.close();
            System.out.println("\nSessão encerrada.");
        }
    }
}