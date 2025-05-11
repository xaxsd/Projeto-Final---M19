package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ProductManagerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Головна програма");
        
        // Тут ваш основний інтерфейс програми
        Label label = new Label("Ласкаво просимо до головної програми!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}