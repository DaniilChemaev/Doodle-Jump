import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Player extends Pane {
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
}
