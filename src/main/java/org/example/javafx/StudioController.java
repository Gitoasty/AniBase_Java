package org.example.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

public class StudioController implements Initializable {

    @FXML
    private AnchorPane studioPane;
    @FXML
    private ImageView studioBackground;
    @FXML
    private ListView<String> theList;
    @FXML
    private TextField studio, year;

    private String dbURL = "jdbc:sqlite:database.db";
    private String tableName = "studio";
    private Connection conn = null;
    private Statement stat = null;

    public void updateList() {
        theList.getItems().clear();
        String sql = "SELECT studio FROM " + tableName;

        try {
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (!theList.getItems().contains(rs.getString("studio"))) {
                    theList.getItems().add(rs.getString("studio"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void newRow(ActionEvent event) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (studio, year) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studio.getText());
            pstmt.setString(2, year.getText());
            pstmt.executeUpdate();
            System.out.println("Added " + studio.getText() + " to " + tableName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        updateList();
    }

    public void editRow(ActionEvent event) throws SQLException {
        String sql = "UPDATE " + tableName + " SET year = ? WHERE studio = ?";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, year.getText());
            pstmt.setString(2, theList.getSelectionModel().getSelectedItem());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        updateList();
    }

    public void deleteRow(ActionEvent event) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE studio = ?";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, theList.getSelectionModel().getSelectedItem());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        updateList();
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        String css = getClass().getResource("/pretty.css").toExternalForm(); //for multiple scenes
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        studioPane.widthProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
            /* Adjusts the ImageView size to match window size when resizing */
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                studioBackground.setFitWidth(studioPane.getWidth());
                studioBackground.setFitHeight(studioPane.getHeight());
            }
        });

        try {
            conn = DriverManager.getConnection(dbURL);

            stat = conn.createStatement();

            String createtableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                    +"studio VARCHAR(30) PRIMARY KEY NOT NULL, "
                    +"year VARCHAR(4) NOT NULL DEFAULT 0000, "
                    +"favorite INGETER DEFAULT 0 "
                    +")";

            stat.executeUpdate(createtableSQL);
            System.out.println("No issues with creation");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                    if (conn != null) {
                        conn.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateList();
    }
}
