package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("config.fxml"));
        TabPane pane = loader.load();
        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Skype Tools");

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
