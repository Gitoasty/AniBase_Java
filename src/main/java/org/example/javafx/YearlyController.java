package org.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class YearlyController {

    @FXML
    private AnchorPane yearlyPane;
    @FXML
    private ImageView yearlyBackground;
    @FXML
    private ListView<String> changedList, notFavorite, isFavorite;
    @FXML
    private Button studio, genre, years, clicked, addFavorite, removeFavorite, back;

    private String dbURL = "jdbc:sqlite:database.db";
    private String tableName;
    private Connection conn = null;
    private Statement stat = null;

    public void studioList(ActionEvent event) {
        clicked = (Button) event.getSource();
        tableName = clicked.getId();
        changedList.getItems().clear();
        String sql = "SELECT studio FROM " + tableName;

        try {
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (!changedList.getItems().contains(rs.getString("studio"))) {
                    changedList.getItems().add(rs.getString("studio"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setNotFavorite();
        setIsFavorite();
    }

    public void genreList(ActionEvent event) {
        clicked = (Button) event.getSource();
        tableName = clicked.getId();
        changedList.getItems().clear();
        String sql = "SELECT genre FROM " + tableName;

        try {
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (!changedList.getItems().contains(rs.getString("genre"))) {
                    changedList.getItems().add(rs.getString("genre"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setNotFavorite();
        setIsFavorite();
    }

    public void yearsList(ActionEvent event) {
        clicked = (Button) event.getSource();
        tableName = clicked.getId();
        changedList.getItems().clear();
        String sql = "SELECT year FROM " + tableName;

        try {
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (!changedList.getItems().contains(rs.getString("year"))) {
                    changedList.getItems().add(rs.getString("year"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setNotFavorite();
        setIsFavorite();
    }

    private void setNotFavorite() {
        notFavorite.getItems().clear();
        if (Objects.equals(tableName, "years")) {
            String sql = "SELECT year FROM " + tableName + " WHERE favorite = 0";

            try {
                Connection conn = DriverManager.getConnection(dbURL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    if (!notFavorite.getItems().contains(rs.getString("year"))) {
                        notFavorite.getItems().add(rs.getString("year"));
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "SELECT " + clicked.getId() + " FROM " + tableName + " WHERE favorite = 0";

            try {
                Connection conn = DriverManager.getConnection(dbURL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    if (!notFavorite.getItems().contains(rs.getString(clicked.getId()))) {
                        notFavorite.getItems().add(rs.getString(clicked.getId()));
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setIsFavorite() {
        isFavorite.getItems().clear();
        if (Objects.equals(tableName, "years")) {
            String sql = "SELECT year FROM " + tableName + " WHERE favorite = 1";

            try {
                Connection conn = DriverManager.getConnection(dbURL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    if (!isFavorite.getItems().contains(rs.getString("year"))) {
                        isFavorite.getItems().add(rs.getString("year"));
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "SELECT " + clicked.getId() + " FROM " + tableName + " WHERE favorite = 1";

            try {
                Connection conn = DriverManager.getConnection(dbURL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    if (!isFavorite.getItems().contains(rs.getString(clicked.getId()))) {
                        isFavorite.getItems().add(rs.getString(clicked.getId()));
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void isFavorite(ActionEvent event) throws SQLException {

        Button favorites = (Button) event.getSource();
        int value = 0;
        if (Objects.equals(favorites.getId(), "addFavorite")) {
            value = 1;
        }

        if (Objects.equals(tableName, "years")) { //this was weird, didn't work with "==" for comparison, wonder why
            String sql = "UPDATE " + tableName + " SET favorite = ? WHERE year = ?";

            try (Connection conn = DriverManager.getConnection(dbURL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, value);
                pstmt.setString(2, changedList.getSelectionModel().getSelectedItem());

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE " + tableName + " SET favorite = ? WHERE " + clicked.getId() + " = ?";

            try (Connection conn = DriverManager.getConnection(dbURL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, value);
                pstmt.setString(2, changedList.getSelectionModel().getSelectedItem());

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        setNotFavorite();
        setIsFavorite();
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
}
