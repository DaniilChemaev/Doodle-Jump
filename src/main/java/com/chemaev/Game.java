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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class Game {
    Image backgroundImg = new Image("images/background.png");
    public static final int STAGE_WIDTH = 450;
    public static final int STAGE_HEIGHT = 900;
    public static ArrayList<Platform> platforms = new ArrayList<>();
    private static final CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    public static final int BLOCK_SIZE = 68;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private static final Game game = new Game();
    private static final Pane appPane = new Pane();
    private static Stage primaryStage;
    private static Scene scene;
    private Pane gameRoot;
    //    private Pane appRoot;
    private static String name;
//    public Player player;

    private void configureSinglePlayer() {
        gameRoot = new Pane();
//        appRoot.setPrefSize(WIDTH, HEIGHT);

        Player player = new Player();
        setPlayer(player);
        initContent();

        scene = new Scene(appPane, STAGE_WIDTH, STAGE_HEIGHT);

        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        primaryStage.setTitle("Doodle Jump");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long lastFrameTime = 0;

            @Override
            public void handle(long now) {
                if (now - lastFrameTime >= 10_000_000) {
                    update();
                    lastFrameTime = now;
                }
            }
        };
        timer.start();
    }

    public void startSinglePlayer() throws IOException {
        configureSinglePlayer();
    }


    private void setPlayer(Player player) {
        player.setTranslateX(STAGE_WIDTH / 2.5);
        player.setTranslateY(STAGE_HEIGHT - 100);
        gameRoot.getChildren().add(player);
        players.add(player);
    }

    private void initContent() {
        ImageView background = new ImageView(backgroundImg);
        background.setFitHeight(STAGE_HEIGHT);
        background.setFitWidth(STAGE_WIDTH);

        int shift = STAGE_HEIGHT - 30;
        int min = 130;
        int max = 160;

        for (int i = 0; i < 8; i++) {
            shift -= min + (int) (Math.random() * ((max - min) + 1));
            Platform platform = new Platform(1, (int) (Math.random() * 5 * BLOCK_SIZE), shift);

            platforms.add(platform);
            gameRoot.getChildren().add(platform);
        }
        addPlatforms();
        appPane.getChildren().addAll(background, gameRoot);
    }

    private void update() {
        for (Player p : players) {
            scene.setOnKeyPressed(p);
            scene.setOnKeyReleased(p);
        }
//        checkSide();
    }

//    private void playerControl() {
//        if (player.getTranslateY() >= 5) {
//            player.jump();
//            player.setCanJump(false);
//        }
//        if (isPressed(KeyCode.LEFT)) {
//            player.setScaleX(-1);
//            player.moveX(-7);
//        }
//        if (isPressed(KeyCode.RIGHT)) {
//            player.setScaleX(1);
//            player.moveX(7);
//        }
//        if (player.playerVelocity.getY() < 10) {
//            player.playerVelocity = player.playerVelocity.add(0, 1);
//        } else player.setCanJump(false);
//        player.moveY((int) player.playerVelocity.getY());
//    }


    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    private void addPlatforms() {
        for (Player p : players) {
            p.translateYProperty().addListener((obs, old, newValue) -> {
                if (p.getTranslateY() < STAGE_HEIGHT / 3) {
                    for (Platform platform : platforms) {
                        platform.setTranslateY(platform.getTranslateY() + 1);
                        if (platform.getTranslateY() == STAGE_HEIGHT + 1) {  // Выход платформы за пределы видимой карты
                            platform.setTranslateY(0);
                            platform.setTranslateX(Math.random() * 6 * BLOCK_SIZE);
                        }
                    }
                }
            });
        }
    }


    public void setName(String name) {
        Game.name = name;
    }

    public void setStage(Stage stage) throws IOException {
        Game.primaryStage = stage;
    }

    public static Game getInstance() {
        return game;
    }
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
//}
