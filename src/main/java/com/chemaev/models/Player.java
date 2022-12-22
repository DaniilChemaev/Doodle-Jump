package com.chemaev.models;

import com.chemaev.Game;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


public class Player extends Pane implements EventHandler<KeyEvent> {
    private final double WIDTH = 76;
    private final double HEIGHT = 76;
    public Point2D playerVelocity = new Point2D(0, 10);
    private boolean canJump;
    public boolean isFalling;
    private static final Image playerImg = new Image("images/doodler.png");
    private static final ImageView playerView = new ImageView(playerImg);
    private KeyCode lastKeyCode;
    private String name;
    private int height = 0;
    public boolean checkPlayerPos;
    private boolean isMultiplayer;
    private final Game game = Game.getInstance();


    public Player(String name, boolean isMultiplayer) {
        playerView.setFitHeight(HEIGHT);
        playerView.setFitWidth(WIDTH);
        setTranslateX(190);
        setTranslateY(540);
        this.canJump = true;
        this.name = name;
        this.isMultiplayer = isMultiplayer;

        getChildren().addAll(playerView);
    }

    public Player(String name, double x, double y) {
        playerView.setFitHeight(HEIGHT);
        playerView.setFitWidth(WIDTH);
        setTranslateX(x);
        setTranslateY(y);
        this.canJump = true;
        this.name = name;

        getChildren().addAll(playerView);
    }

    public void moveX(int value) {
        boolean movingRight = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
            setScaleX(movingRight ? 1 : -1);
            if (isMultiplayer) {
                String message = String.format("move %s %s %s %s\n", name, getTranslateX(), getTranslateY(), height);
                game.getGameClient().sendMessage(message);
            }
        }
    }

    public void moveY(int value) {
        boolean movingDown = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Platform platform : Game.platforms) {
                if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (getTranslateY() + HEIGHT == platform.getTranslateY()) {
                            setTranslateY(getTranslateY() - 1);
                            canJump = true;
                            isFalling = false;
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
            if (isMultiplayer) {
                String message = String.format("move %s %s %s %s\n", name, getTranslateX(), getTranslateY(), height);
                game.getGameClient().sendMessage(message);
            }
        }
    }

    public void jump() {
        if (getTranslateY() >= 100 && canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
        if (playerVelocity.getY() < 10) {
            isFalling = false;
            playerVelocity = playerVelocity.add(0, 1);
        } else {
            canJump = false;
            isFalling = true;
        }
        moveY((int) playerVelocity.getY());
    }

    public void move(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    private void checkLRBounds() {
        if (getTranslateX() < -60) {
            setTranslateX(445);
        }
        if (getTranslateX() > 445) {
            setTranslateX(-60);
        }
        if (getTranslateY() < 10) {
            setTranslateY(10);
        }
    }

    public void update() {
        jump();
        addHeight();
        playerControl();
        checkLRBounds();
    }

    public void addHeight() {
        if (checkPlayerPos) {
            height += 5;
        }
    }

    private boolean isPressed(KeyCode key) {
        return key == lastKeyCode;
    }

    private void playerControl() {
        if (isPressed(KeyCode.LEFT)) {
            setScaleX(-1);
            moveX(-7);
        }
        if (isPressed(KeyCode.RIGHT)) {
            setScaleX(1);
            moveX(7);
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType().getName().equals("KEY_PRESSED")) {
            lastKeyCode = event.getCode();
        } else if (event.getEventType().getName().equals("KEY_RELEASED")) {
            lastKeyCode = null;
        }
    }

    public boolean didFall() {
        boolean didFall = false;
        if (this.getTranslateY() > Game.STAGE_HEIGHT) {
            didFall = true;

        }

        return didFall;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHeight() {
        return height;
    }
}
