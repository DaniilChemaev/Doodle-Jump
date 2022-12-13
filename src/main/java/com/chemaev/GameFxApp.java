package com.chemaev;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameFxApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new Menu(stage);
    }
}
