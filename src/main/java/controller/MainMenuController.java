package main.java.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.model.Player;

public class MainMenuController {
    @FXML private VBox menuContainer;
    @FXML private Label playerInfo;
    
    private Player currentPlayer;
    
    public void setPlayerData(Player player) {
        this.currentPlayer = player;
        playerInfo.setText("Гравець: " + player.getUsername());
    }
    
    @FXML
    private void exitGame() {
        Stage stage = (Stage) menuContainer.getScene().getWindow();
        stage.close();
    }
}