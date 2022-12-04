import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Player extends Pane {
    private static final double WIDTH = 76;
    private static final double HEIGHT = 76;
    private static final Image playerImg = new Image("images/doodler.png");
    private static final ImageView imageView = new ImageView(playerImg);

    public Player() {
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);

        getChildren().addAll(imageView);
    }
}
