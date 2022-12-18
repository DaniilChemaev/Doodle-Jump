package com.chemaev.models;

import com.chemaev.Game;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Ops extends Pane {
    private final double WIDTH = 76;
    private final double HEIGHT = 76;
    private static final Image playerImg = new Image("images/doodler.png");
    private static final ImageView playerView = new ImageView(playerImg);
    private String name;

    public Ops(String name, double x, double y) {
        playerView.setFitHeight(HEIGHT);
        playerView.setFitWidth(WIDTH);
        setTranslateX(x);
        setTranslateY(y);
        this.name = name;

        getChildren().addAll(playerView);
    }

    public void moveX(int value) {
        boolean movingRight = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
            setScaleX(movingRight ? 1 : -1);
        }
    }

//    public void moveY(int value) {
//        boolean movingDown = value > 0;
//        for (int i = 0; i < Math.abs(value); i++) {
//            for (Platform platform : Game.platforms) {
//                if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
//                    if (movingDown) {
//                        if (getTranslateY() + HEIGHT == platform.getTranslateY()) {
//                            setTranslateY(getTranslateY() - 1);
//                            canJump = true;
//                            isFalling = false;
//                            return;
//                        }
//                    }
//                }
//            }
//            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
//        }
//    }

//    public void jump() {
//        if (getTranslateY() >= 100 && canJump) {
//            playerVelocity = playerVelocity.add(0, -30);
//            canJump = false;
//        }
//        if (playerVelocity.getY() < 10) {
//            isFalling = false;
//            playerVelocity = playerVelocity.add(0, 1);
//        } else {
//            canJump = false;
//            isFalling = true;
//        }
//        moveY((int) playerVelocity.getY());
//    }

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

//    public void update() {
////        jump();
//        playerControl();
//        checkLRBounds();
//    }

//    public boolean didFall() {
//        boolean didFall = false;
//        if (this.getTranslateY() > Game.STAGE_HEIGHT) {
//            didFall = true;
//
//        }
//
//        return didFall;
//    }

    public String getName() {
        return name;
    }
}
