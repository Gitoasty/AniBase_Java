package org.example.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


/* Never trust a "simple tool" ever again, in this household, we commit via file upload, ALWAYS */
public class AnimeController implements Initializable {

    @FXML
    private StackPane animePane;
    @FXML
    private GridPane pane;
    @FXML
    private ListView<String> theList;
    @FXML
    private TextField name, original, studio, genre, year;
    @FXML
    private Label nLabel, oLabel, sLabel, gLabel, yLabel;
    @FXML
    private Button saveButton, editButton, deleteButton, backButton;

    private String dbURL = "jdbc:sqlite:database.db";
    private String tableName = "anime";
    private Connection conn = null;
    private Statement stat = null;

    public void updateList() {
        theList.getItems().clear();
        String sql = "SELECT name FROM " + tableName;

        try {
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (!theList.getItems().contains(rs.getString("name"))) {
                    theList.getItems().add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertStudio() {
        String sql = "INSERT INTO studio (studio) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(dbURL)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, studio.getText());
                pstmt.executeUpdate();
            }
            System.out.println("Studio added");
        } catch (SQLException e) {
            System.out.println("Studio already exists");
        }
    }

    private void insertGenre() {
        String sql = "INSERT INTO genre (genre) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(dbURL)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, genre.getText());
                pstmt.executeUpdate();
            }
            System.out.println("Genre added");
        } catch (SQLException e) {
            System.out.println("Genre already exists");
        }
    }

    private void insertYear() {
        String sql = "INSERT INTO years (year) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(dbURL)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, year.getText());
                pstmt.executeUpdate();
            }
            System.out.println("Year added");
        } catch (SQLException e) {
            System.out.println("Year already exists");
        }
    }

    public void newRow(ActionEvent event) throws SQLException {

        /* Adds row to anime */
        String sql = "INSERT INTO " + tableName + " (name, original, studio, genre, year) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name.getText());
            pstmt.setString(2, original.getText());
            pstmt.setString(3, studio.getText());
            pstmt.setString(4, genre.getText());
            pstmt.setString(5, year.getText());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            System.out.println("Added " + name.getText() + " to " + tableName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        insertStudio();
        insertYear();
        insertGenre();

        updateList();
    }

    public void editRow(ActionEvent event) throws SQLException {
        String sql = "UPDATE " + tableName + " SET original = ?, studio = ?, genre = ?, year = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, original.getText());
            pstmt.setString(2, studio.getText());
            pstmt.setString(3, genre.getText());
            pstmt.setString(4, year.getText());
            pstmt.setString(5, theList.getSelectionModel().getSelectedItem());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        insertStudio();
        insertYear();
        insertGenre();

        updateList();
    }

    public void deleteRow(ActionEvent event) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE name = ?";

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

        try {
            conn = DriverManager.getConnection(dbURL);

            stat = conn.createStatement();

            String createtableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                    +"name TEXT PRIMARY KEY NOT NULL, "
                    +"original TEXT NOT NULL, "
                    +"studio VARCHAR(30) NOT NULL, "
                    +"genre VARCHAR(20) NOT NULL, "
                    +"year VARCHAR(4) NOT NULL, "
                    +"favorite INTEGER DEFAULT 0"
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

        //private Button saveButton, editButton, deleteButton, backButton;

        {
            //dedicated variables for easier control of font size
            double font = 0.03;
            double button_font = 0.02;

            { //sets the font of the elements
                theList.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));

                name.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                original.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                studio.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                genre.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                year.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));

                nLabel.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                oLabel.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                sLabel.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                gLabel.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));
                yLabel.styleProperty().bind(animePane.widthProperty().multiply(font).asString("-fx-font-size: %.2fpx;"));

                saveButton.styleProperty().bind(animePane.widthProperty().multiply(button_font).asString("-fx-font-size: %.2fpx;"));
                editButton.styleProperty().bind(animePane.widthProperty().multiply(button_font).asString("-fx-font-size: %.2fpx;"));
                deleteButton.styleProperty().bind(animePane.widthProperty().multiply(button_font).asString("-fx-font-size: %.2fpx;"));
                backButton.styleProperty().bind(animePane.widthProperty().multiply(button_font).asString("-fx-font-size: %.2fpx;"));
            }
        }
    }
}
