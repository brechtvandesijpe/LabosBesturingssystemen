package Classes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.util.Objects;

// De GUIController klasse is de FXML Controller
// Hij legt de link tussen de GUI en het programma
public class Main extends Application {
    @Override
    public void start(final Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI.fxml")));
        primaryStage.setTitle("Lab Assignment 2 : Virtual Memory");
        primaryStage.setScene(new Scene((root), 1200, 650));
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
