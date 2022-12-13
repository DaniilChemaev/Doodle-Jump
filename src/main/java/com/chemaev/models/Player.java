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
    public Point2D playerVelocity = new Point2D(0, 0);

    private boolean canJump;
    private static final Image playerImg = new Image("images/doodler.png");
    private static final ImageView playerView = new ImageView(playerImg);

    public Player() {
        playerView.setFitHeight(HEIGHT);
        playerView.setFitWidth(WIDTH);
        this.canJump = true;

        getChildren().addAll(playerView);
    }

    public void moveX(int value) {
        boolean movingRight = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    public void moveY(int value) {
        boolean movingDown = value > 0;
        for (int i = 0; i < Game.platforms.size(); i++) {
            System.out.print(Game.platforms.get(i).getTranslateY() + " ");
        }
        System.out.println();
        for (int i = 0; i < Math.abs(value); i++) {
            for (Platform platform : Game.platforms) {
                if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (this.getTranslateY() + this.HEIGHT == platform.getTranslateY()) {
                            System.out.println(platform.getTranslateY());
                            this.setTranslateY(this.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
        }

    }

    public void jump() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    private void checkSide() {
        if (getTranslateX() < -72) {  // Если ушел влево
            setTranslateX(430);
        } else if (getTranslateX() > 430) {  // Если ушел вправо
            setTranslateX(-60);
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (getTranslateY() >= 5) {
            jump();
            setCanJump(false);
        }

        switch (event.getCode()) {
            case LEFT:
                setScaleX(-1);
                moveX(-7);
            case RIGHT:
                setScaleX(1);
                moveX(7);
        }

        checkSide();

        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        } else setCanJump(false);
        moveY((int) playerVelocity.getY());
    }

    @Override
    public void handle(KeyEvent event) {

        if (event.getEventType().getName().equals("KEY_PRESSED")) {
            handleKeyPressed(event);
        }
    }
}
