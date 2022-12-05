import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Platform extends Pane {
    public int type;
    private static final double WIDTH = 68;
    private static final double HEIGHT = 14;

    Image platformImg = new Image("images/platforms.png");
    ImageView platformView;

    public Platform(int type, int x, int y) {
        platformView = new ImageView(platformImg);
        platformView.setFitWidth(WIDTH);
        platformView.setFitHeight(HEIGHT);
        setTranslateX(x);
        setTranslateY(y);

        this.type = type;

        if (type == 1) {
            platformView.setViewport(new Rectangle2D(0, 0, 68, 14));
        }
        if (type == 2) {
            platformView.setViewport(new Rectangle2D(0, 14, 68, 20));
        }

        getChildren().addAll(platformView);
//        Game.platforms.add(this);
//        Game.gameRoot.getChildren().add(this);
    }
}
