package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Aplicação principal para gerenciamento de produtos.
 * Esta classe representa a interface gráfica inicial do sistema.
 */
public class ProductManagerApp extends Application {

    /**
     * Método principal de inicialização da interface JavaFX.
     * @param primaryStage O palco principal da aplicação
     */
    @Override
    public void start(Stage primaryStage) {
        // Configura o título da janela principal
        primaryStage.setTitle("Sistema de Gerenciamento de Produtos");
        
        // Cria um rótulo de boas-vindas
        Label welcomeLabel = new Label("Bem-vindo ao Sistema de Gerenciamento de Produtos!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Cria um layout básico e adiciona o rótulo
        StackPane rootLayout = new StackPane(welcomeLabel);
        rootLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");
        
        // Cria a cena principal com tamanho 800x600 pixels
        Scene mainScene = new Scene(rootLayout, 800, 600);
        
        // Define a cena no palco principal e exibe
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Ponto de entrada da aplicação JavaFX.
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Inicia a aplicação JavaFX
        launch(args);
    }
}