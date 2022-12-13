package com.chemaev;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Menu {
    private String title = "Menu";
    private AnchorPane pane = null;
    private VBox vBox;
    public Button singlePlayer;
    public Button multiPlayer;
    private Stage stage;
    //    private final GameMap map = GameMap.getInstance();
    private TextField nameTextField;

    public Menu(Stage stage) {
        this.stage = stage;

        configure();
    }

    private void configure() {
        pane = new AnchorPane();
        vBox = new VBox(15);
        Font font = Font.font("Courier New", FontWeight.BOLD, 20);

        singlePlayer = new Button("singlePlayer");
//        singlePlayer.setOnAction(singlePlayerEvent);
        singlePlayer.setMaxWidth(500);
        singlePlayer.setMaxHeight(500);
        singlePlayer.setFont(font);

        multiPlayer = new Button("multiPlayer");
//        multiPlayer.setOnAction(multiPlayerEvent);
        multiPlayer.setMaxWidth(500);
        multiPlayer.setMaxHeight(500);
        multiPlayer.setFont(font);

        Label nameLabel = new Label("Enter your nickname:");

        nameTextField = new TextField();
        nameTextField.setMaxWidth(500);
        nameTextField.setMaxHeight(500);

        vBox.getChildren().add(nameLabel);
        vBox.getChildren().add(nameTextField);
        vBox.getChildren().add(singlePlayer);
        vBox.getChildren().add(multiPlayer);

        AnchorPane.setTopAnchor(vBox, 5.0);
        AnchorPane.setLeftAnchor(vBox, 10.0);
        AnchorPane.setRightAnchor(vBox, 10.0);
        pane.getChildren().add(vBox);

        Scene scene = new Scene(pane, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
