package main;

import javafx.application.Application;
import app.LoginScreen;

/**
 * Classe principal que inicia a aplicação JavaFX.
 * Esta classe serve como ponto de entrada para o sistema.
 */
public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * @param args Argumentos de linha de comando (não utilizados nesta aplicação)
     */
    public static void main(String[] args) {
        try {
            // Configura o tratamento de exceções não capturadas
            Thread.setDefaultUncaughtExceptionHandler(Main::handleUncaughtException);
            
            // Inicia a aplicação JavaFX chamando a tela de Login
            Application.launch(LoginScreen.class, args);
            
        } catch (Exception e) {
            System.err.println("Falha crítica ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();
            // Aqui você poderia registrar o erro em um arquivo de log
        }
    }
    
    /**
     * Manipulador de exceções não capturadas.
     * @param thread A thread onde ocorreu a exceção
     * @param exception A exceção ocorrida
     */
    private static void handleUncaughtException(Thread thread, Throwable exception) {
        System.err.println("Exceção não tratada na thread " + thread.getName());
        exception.printStackTrace();
        // Aqui você poderia:
        // 1. Registrar o erro em um arquivo de log
        // 2. Mostrar uma mensagem amigável ao usuário
        // 3. Tentar recuperar o aplicativo ou encerrá-lo graciosamente
    }
}