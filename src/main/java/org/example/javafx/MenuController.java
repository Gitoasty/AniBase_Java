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
        stage.setMinWidth(menuPane.getWidth());
        stage.setMinHeight(menuPane.getHeight());
        stage.show();
    }

    @FXML
    public void closeApp(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        Stage stage = (Stage) clicked.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        //dedicated variables for easier control of button width and font size
        double butt = 0.2;
        double font = 0.03;
        {
            anime.prefWidthProperty().bind(menuPane.widthProperty().multiply(butt));
            studio.prefWidthProperty().bind(menuPane.widthProperty().multiply(butt));
            years.prefWidthProperty().bind(menuPane.widthProperty().multiply(butt));
            genres.prefWidthProperty().bind(menuPane.widthProperty().multiply(butt));
            by_years.prefWidthProperty().bind(menuPane.widthProperty().multiply(butt));
            exit.prefWidthProperty().bind(menuPane.widthProperty().multiply(butt));
        }

        {
            anime.styleProperty().bind(menuPane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
            studio.styleProperty().bind(menuPane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
            years.styleProperty().bind(menuPane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
            genres.styleProperty().bind(menuPane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
            by_years.styleProperty().bind(menuPane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
            exit.styleProperty().bind(menuPane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
        }
    }
}
