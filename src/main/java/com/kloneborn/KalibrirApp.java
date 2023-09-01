package com.kloneborn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class KalibrirApp extends Application {

    public static final String VERSION = "0.0.1";
    public static final String TITLE = "Kalibrir";
    private static final String iconPath = "img/counter.png";

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("view/word-counter"));
        stage.setTitle("KloneBorn - " + TITLE + " - " + VERSION);
        stage.getIcons().add(
            new Image(
                KalibrirApp.class.getResourceAsStream(
                    iconPath
                )
            )
        );
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KalibrirApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}