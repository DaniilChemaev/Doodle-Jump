package com.chemaev;

import com.chemaev.models.Platform;
import com.chemaev.models.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class Game {
    Image backgroundImg = new Image("images/background.png");
    public static final int STAGE_WIDTH = 450;
    public static final int STAGE_HEIGHT = 700;
    public static ArrayList<Platform> platforms = new ArrayList<>();
    private static final CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    public static final int BLOCK_SIZE = 68;
    private static final Game game = new Game();
    private static final Pane appPane = new Pane();
    private static Stage primaryStage;
    private static Scene scene;
    private Pane gameRoot;
    private static String name;
    AnimationTimer timer;


    private void configureSinglePlayer() {
        gameRoot = new Pane();

        Player player = new Player();
        setPlayer(player);
        initContent();

        scene = new Scene(appPane, STAGE_WIDTH, STAGE_HEIGHT);

        for (Player p : players) {
            scene.setOnKeyPressed(p);
            scene.setOnKeyReleased(p);
        }

        primaryStage.setTitle("Doodle Jump");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        timer = new AnimationTimer() {
            private long lastFrameTime = 0;

            @Override
            public void handle(long now) {
                if (now - lastFrameTime >= 10_000_000) {
                    for (Player p : players) {
                        p.update();
                        if (p.didFall()) {
                            System.out.println("GAME OVER");
                            showWinMenu("123");
                            timer.stop();
                        }
                    }
                    lastFrameTime = now;
                }
            }
        };
        timer.start();
    }

    public void showWinMenu(String winnerName) {
        new WinMenu(primaryStage, winnerName);
    }

    public void startSinglePlayer() throws IOException {
        configureSinglePlayer();
    }


    private void setPlayer(Player player) {
        player.setTranslateX(190);
        player.setTranslateY(540);
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

    private void addPlatforms() {
        for (Player p : players) {
            p.translateYProperty().addListener((obs, old, newValue) -> {
                if (p.getTranslateY() <= STAGE_HEIGHT / 2) {
//                    System.out.println("p.getTranslateY() < STAGE_HEIGHT / 2");
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