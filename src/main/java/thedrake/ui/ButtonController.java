package thedrake.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ButtonController {

    @FXML
    private Button endButton;

    Stage primaryStage = new Stage();

    public void twoPlayers() throws IOException {

        URL url = Objects.requireNonNull(ButtonController.class.getResource("game_view.fxml"));

        primaryStage.setScene(new Scene(FXMLLoader.load(url)));
        primaryStage.setTitle("The Drake");
        primaryStage.show();
    }

    public void close() {
        Stage stage = (Stage) endButton.getScene().getWindow();
        stage.close();
    }
}
