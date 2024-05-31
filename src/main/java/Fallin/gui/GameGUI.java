package Fallin.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GameGUI class sets up the primary stage and scene to display.
 */
public class GameGUI extends Application {
    /**
     * The start method is called when the JavaFX application is launched and sets up GUI.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Fallin/gui/game_gui.fxml"));
        Scene scene = new Scene(root, 1200, 720);

        primaryStage.setTitle("Fallin Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Lock the window size
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
