package thedrake.ui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import thedrake.tile.PlayingSide;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Win {

    Button returnButton = new Button("Návrat na úvodní obrazovku");

    Button newGameButton = new Button("Nová hra");

    public Win(PlayingSide side, VBox mainBox, GridPane gridPane, HBox top, HBox bottom) {

        mainBox.getChildren().removeAll(gridPane, top, bottom);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(17.6025);
        dropShadow.setHeight(18.66);
        dropShadow.setSpread(0.46);
        dropShadow.setWidth(53.75);

        if (side == PlayingSide.BLUE) {

            Label label = new Label("Vyhrává modrý tým!");
            label.setFont(new Font("Imprint MT Shadow", 55));
            label.setEffect(dropShadow);
            mainBox.getChildren().add(label);

            mainBox.setId("blue_back");
            mainBox.getStylesheets().addAll(String.valueOf(this.getClass().getResource("blue_wins.css")));

        } else if (side == PlayingSide.ORANGE) {

            Label label = new Label("Vyhrává oranževý tým!");
            label.setFont(new Font("Imprint MT Shadow", 55));
            label.setEffect(dropShadow);
            mainBox.getChildren().add(label);

            mainBox.setId("orange_back");
            mainBox.getStylesheets().addAll(String.valueOf(this.getClass().getResource("orange_wins.css")));
        }

        Label label1 = new Label("");
        mainBox.getChildren().add(label1);

        returnButton.setId("returnButton");
        mainBox.getChildren().add(returnButton);

        newGameButton.setId("newGameButton");
        mainBox.getChildren().add(newGameButton);

        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setPadding(new Insets(50));
        mainBox.setSpacing(40);

        returnButton.setOnMouseClicked(e -> returnAction());
        newGameButton.setOnMouseClicked(e -> {
            try {
                newGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void returnAction() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    private void newGame() throws IOException {
        Stage stage = (Stage) newGameButton.getScene().getWindow();
        stage.close();

        URL url = Objects.requireNonNull(ButtonController.class.getResource("game_view.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(FXMLLoader.load(url)));
        primaryStage.setTitle("The Drake");
        primaryStage.show();
    }
}
