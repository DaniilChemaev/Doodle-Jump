package com.chemaev;

import com.chemaev.client.GameClient;
import com.chemaev.models.Ops;
import com.chemaev.models.Platform;
import com.chemaev.models.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class Game {
    Image backgroundImg = new Image("images/background.png");
    public static final int STAGE_WIDTH = 450;
    public static final int STAGE_HEIGHT = 700;
    public static ArrayList<Platform> platforms = new ArrayList<>();
    public static final CopyOnWriteArrayList<Ops> otherPlayersShadows = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    public static final int BLOCK_SIZE = 68;
    private static final Game game = new Game();
    private static GameClient gameClient;
    private static final Pane appPane = new Pane();
    private static Stage primaryStage;
    private static Scene scene;
    private Pane gamePane = new Pane();
    private static String name;
    Text scoreText = new Text();
    Text topScoreText = new Text();
    AnimationTimer timer;


    private void configureSinglePlayer() {
        Player player = new Player(name, false);
        // !TODO Get rid of adding player to array
        players.add(player);
        gamePane.getChildren().add(player);
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
                if (now - lastFrameTime >= 20_000_000) {
                    for (Player p : players) {
                        p.update();
                        if (p.didFall()) {
                            System.out.println("GAME OVER");
                            showWinMenu("123");
                            timer.stop();
                        }
                    }
                    lastFrameTime = now;
                    playerScore(players.get(0));
                }
            }
        };
        timer.start();
    }

    private void configureMultiPlayer() throws IOException {
        gameClient = new GameClient();
        gameClient.start();

        Player player = new Player(name, true);
        gamePane.getChildren().add(player);
        players.add(player);
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

        String message = String.format("new %s %s %s\n", name, player.getTranslateX(), player.getTranslateY());
        gameClient.sendMessage(message);

        timer = new AnimationTimer() {
            private long lastFrameTime = 0;

            @Override
            public void handle(long now) {
                if (now - lastFrameTime >= 20_000_000) {
                    for (Player p : players) {
                        p.update();
                        if (p.didFall()) {
                            System.out.println("GAME OVER");
                            showWinMenu(name);
                            timer.stop();
                        }
                    }
                    lastFrameTime = now;
                    playerScore(players.get(0));
                }
            }
        };
        timer.start();
    }

    public synchronized void createNewOps(String playerName, double x, double y) {
        boolean isFound = false;

        for (Ops ops : otherPlayersShadows) {
            if (ops.getName().equals(playerName)) {
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            javafx.application.Platform.runLater(() -> {
                Ops ops = new Ops(playerName, x, y);
                otherPlayersShadows.add(ops);
                gamePane.getChildren().add(ops);

//                pane.getChildren().add(hpLabel);
//                pane.getChildren().add(nameLabel);
            });
        }
    }

    public synchronized void moveOps(String playersName, double x, double y, int height) {
        boolean isFound = false;

        for (Ops ops : otherPlayersShadows) {
            if (ops.getName().equals(playersName)) {
                for (Player p : players) {
                    if (!p.getName().equals(playersName)) {
                        javafx.application.Platform.runLater(() -> {
                            ops.move(x, y, height, p.getCurrentHeight());
                        });
                        isFound = true;
                    }
                }
            }
        }

        if (!isFound) {
            createNewOps(playersName, x, y);
        }
    }

    public void startSinglePlayer() throws IOException {
        configureSinglePlayer();
    }

    public void startMultiPlayer() throws IOException {
        configureMultiPlayer();
    }

    public void showWinMenu(String winnerName) {
        new WinMenu(primaryStage, winnerName);
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
            gamePane.getChildren().add(platform);
        }
        addPlatforms();
        appPane.getChildren().addAll(background, gamePane);
    }


    // !TODO Fix platforms movements. Double shifts
    private void addPlatforms() {
        for (Player p : players) {
            p.translateYProperty().addListener((obs, old, newValue) -> {
                p.checkPlayerPos = false;
                if (p.getTranslateY() <= STAGE_HEIGHT / 2 && !p.isFalling) {
                    p.checkPlayerPos = true;
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

    private void playerScore(Player currentPlayer) {
        gamePane.getChildren().remove(scoreText);
//
        scoreText.setText("Score: " + currentPlayer.getCurrentHeight());
        scoreText.setTranslateX(30);
        scoreText.setTranslateY(20);
        scoreText.setScaleX(2);
        scoreText.setScaleY(2);
        gamePane.getChildren().add(scoreText);
//        System.out.println(players.toString());
//
        if (otherPlayersShadows.size() >= 1) {
            gamePane.getChildren().remove(topScoreText);
            int maxScore = currentPlayer.getCurrentHeight();
            String maxScorePlayerName = currentPlayer.getName();
            for (Ops p : otherPlayersShadows) {
                if (maxScore < p.getCurrentHeight()) {
                    maxScore = p.getCurrentHeight();
                    maxScorePlayerName = p.getName();
                }
            }
            topScoreText.setText(String.format("Top score: \"%s\" %s", maxScorePlayerName, maxScore));
            topScoreText.setTranslateX(50);
            topScoreText.setTranslateY(50);
            topScoreText.setScaleX(2);
            topScoreText.setScaleY(2);
            gamePane.getChildren().add(topScoreText);
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

    public GameClient getGameClient() {
        return gameClient;
    }
}