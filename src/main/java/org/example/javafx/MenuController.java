package org.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button anime, studio, years, genres, by_years, exit;

    @FXML
    private ImageView menuBackground;

    @FXML
    private GridPane menuPane;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void change(ActionEvent event) throws IOException { /* Changes scene depending on button id */
        Button clicked = (Button) event.getSource();
        System.out.println("Switched scene to: " + clicked.getId());

        root = FXMLLoader.load(getClass().getResource(clicked.getId() + ".fxml"));
        stage = (Stage) clicked.getScene().getWindow();
        scene = new Scene(root);
        String css = getClass().getResource("/pretty.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void closeApp(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        Stage stage = (Stage) clicked.getScene().getWindow();
        stage.close();
    }
}
