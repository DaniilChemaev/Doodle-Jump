import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
    Image backgroundImg = new Image("images/background.png");
    private static final int WIDTH = 500;
    private static final int HEIGHT = 1000;
    private Pane gameRoot;
    private Pane appRoot;

    private void newPlayer() {
        Player player = new Player();
        gameRoot.getChildren().add(player);
    }

    private void initContent() {
        ImageView background = new ImageView(backgroundImg);
        background.setFitHeight(HEIGHT);
        background.setFitWidth(WIDTH);
        appRoot.getChildren().addAll(background, gameRoot);
    }

    @Override
    public void start(Stage primaryStage) {
        appRoot = new Pane();
        gameRoot = new Pane();
        appRoot.setPrefSize(WIDTH, HEIGHT);

        initContent();

        Scene scene = new Scene(appRoot);
        newPlayer();

        primaryStage.setTitle("Doodle Jump");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
