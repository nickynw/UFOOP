package code;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


/*Main class has been kept relatively empty, this is because there is the option
in the future of doing alternative versions of the game, which could be accessed
by a different menu. In this case, it is helpful to have the current 'Game' class
already seperated.*/

public class Main extends Application {
    private File directory;
    private Scene scene;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;

    public void start(Stage primaryStage) throws Exception {
        //create Highscore folder to store textfile
        directory = new File( "." + "/Highscore");
        if (!directory.exists()) directory.mkdirs();
        ///set up scene
        scene = new Scene(new Group(), WIDTH, HEIGHT, true);
        scene.setFill(Color.BLACK);
        scene.setCursor(Cursor.NONE);
        scene.setCamera( getCamera());
        //start game, this style of starting game is used to allow other 'game' classes in the future.
        Game game = new Game(scene, primaryStage, directory, loadHighScore());
        //setup stage
        primaryStage.setTitle("U.F.O.O.P");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/images/icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Camera getCamera() {
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(15000);
        camera.setNearClip(0.1);
        camera.setDepthTest(DepthTest.ENABLE);
        return camera;
    }

    private int loadHighScore() throws Exception {
        File file = new File( directory+"\\"+"Highscore.txt");
        if (file.exists()) {
            Path path = Paths.get(directory + "\\" + "Highscore.txt");
            Scanner scanner = new Scanner(path);
            try {
                return (Integer.parseInt(scanner.nextLine()));
            } catch (Exception E) { }
        }
        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}