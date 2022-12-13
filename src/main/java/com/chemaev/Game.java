package com.chemaev;

import com.chemaev.models.Platform;
import com.chemaev.models.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;


public class Game extends Application {
    Image backgroundImg = new Image("images/background.png");
    public static final int WIDTH = 450;
    public static final int HEIGHT = 900;

    public static ArrayList<Platform> platforms = new ArrayList<>();
    public static final int BLOCK_SIZE = 68;

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();

    private Pane gameRoot;
    private Pane appRoot;

    public Player player;

    private void newPlayer() {
        player = new Player();
        player.setTranslateX(WIDTH / 2.5);
        player.setTranslateY(HEIGHT - 100);
        gameRoot.getChildren().add(player);
    }

    private void initContent() {
        ImageView background = new ImageView(backgroundImg);
        background.setFitHeight(HEIGHT);
        background.setFitWidth(WIDTH);

        int shift = HEIGHT - 30;
        int min = 130;
        int max = 160;

        for (int i = 0; i < 8; i++) {
            shift -= min + (int) (Math.random() * ((max - min) + 1));
            Platform platform = new Platform(1, (int) (Math.random() * 5 * BLOCK_SIZE), shift);

            platforms.add(platform);
            gameRoot.getChildren().add(platform);
        }
        addPlatforms();
        appRoot.getChildren().addAll(background, gameRoot);
    }

    private void update() {
        playerControl();
        checkSide();
    }

    private void playerControl() {
//        System.out.println(player.playerVelocity.getY());
        if (player.getTranslateY() >= 5) {
            player.jump();
            player.setCanJump(false);
        }
        if (isPressed(KeyCode.LEFT)) {
            player.setScaleX(-1);
            player.moveX(-7);
        }
        if (isPressed(KeyCode.RIGHT)) {
            player.setScaleX(1);
            player.moveX(7);
        }
        if (player.playerVelocity.getY() < 10) {
            player.playerVelocity = player.playerVelocity.add(0, 1);
        } else player.setCanJump(false);
        player.moveY((int) player.playerVelocity.getY());

    }

    private void checkSide() {
        if (player.getTranslateX() < -72) {  // Если ушел влево
            player.setTranslateX(430);
        } else if (player.getTranslateX() > 430) {  // Если ушел вправо
            player.setTranslateX(-60);
        }
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    private void addPlatforms() {
        player.translateYProperty().addListener((obs, old, newValue) -> {
            if (player.getTranslateY() < HEIGHT / 3) {
                for (Platform platform : platforms) {
                    platform.setTranslateY(platform.getTranslateY() + 1);
                    if (platform.getTranslateY() == HEIGHT + 1) {  // Выход платформы за пределы видимой карты
                        platform.setTranslateY(0);
                        platform.setTranslateX(Math.random() * 6 * BLOCK_SIZE);
                    }
                }
            }
        });
    }

//    @Override
//    public void start(Stage primaryStage) {
//        appRoot = new Pane();
//        gameRoot = new Pane();
//        appRoot.setPrefSize(WIDTH, HEIGHT);
//
//        newPlayer();
//        initContent();
//
//
//        Scene scene = new Scene(appRoot);
//        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
//        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
//
//        primaryStage.setTitle("Doodle Jump");
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();
//
//        AnimationTimer timer = new AnimationTimer() {
//            private long lastFrameTime = 0;
//
//            @Override
//            public void handle(long now) {
//                if (now - lastFrameTime >= 10_000_000) {
//                    update();
//                    lastFrameTime = now;
//                }
//            }
//        };
//        timer.start();
//    }


    @Override
    public void start(Stage stage) throws Exception {
        new Menu(stage);
    }
}
