import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;


public class Game extends Application {
    Image backgroundImg = new Image("images/background.png");
    private static final int WIDTH = 450;
    private static final int HEIGHT = 900;

    private HashMap<KeyCode, Boolean> keys = new HashMap<>();

    private Pane gameRoot;
    private Pane appRoot;

    public Player player;

    private void newPlayer() {
        player = new Player();
        player.setTranslateX(185);
        player.setTranslateY(650);
        gameRoot.getChildren().add(player);
    }

    private void initContent() {
        ImageView background = new ImageView(backgroundImg);
        background.setFitHeight(HEIGHT);
        background.setFitWidth(WIDTH);
        appRoot.getChildren().addAll(background, gameRoot);
    }

    private void update() {
        playerControl();
        checkSide();
    }

    private void playerControl() {
        System.out.println(player.playerVelocity.getY());
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
//        System.out.println("X: " + player.getTranslateX());
//        System.out.println("Y: " + player.getTranslateY());
        if (player.getTranslateX() < -72) {  // Если ушел влево
            player.setTranslateX(430);
        } else if (player.getTranslateX() > 430) {  // Если ушел вправо
            player.setTranslateX(-60);
        }
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) {
        appRoot = new Pane();
        gameRoot = new Pane();
        appRoot.setPrefSize(WIDTH, HEIGHT);

        initContent();
        newPlayer();

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        primaryStage.setTitle("Doodle Jump");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
}
