package org.example.javafx;

//put this in front of controller name: org.example.javafx.

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
        try {
            String url = "jdbc:sqlite:database.db";
            Connection conn = DriverManager.getConnection(url);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("pretty.css");

            Image icon = new Image("nico.png");
            stage.getIcons().add(icon);
            stage.setTitle("Anibase 1.1");

            stage.setMinHeight(425);
            stage.setMinWidth(600);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit(Stage stage) {
        stage.close();
        System.out.println("Closed app");
    }
}