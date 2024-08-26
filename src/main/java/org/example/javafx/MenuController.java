package org.example.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable{

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuPane.widthProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
            /* Adjusts the ImageView size to match window size when resizing */
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                menuBackground.setFitWidth(menuPane.getWidth());
                menuBackground.setFitHeight(menuPane.getHeight());
            }
        });
    }
}
