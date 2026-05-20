package com.fitnessclub;

import com.fitnessclub.util.NotificareUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/fitnessclub/main.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);
        stage.setTitle("🏋 Fitness Club Manager");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();

        // Notificare dupa ce interfata e incarcata
        Platform.runLater(NotificareUtil::verificaExpirari);
    }

    public static void main(String[] args) {
        launch(args);
    }
}