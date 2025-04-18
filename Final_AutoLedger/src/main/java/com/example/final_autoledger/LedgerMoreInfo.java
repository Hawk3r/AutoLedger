package com.example.final_autoledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LedgerMoreInfo {


    @FXML
    private Button exit;

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private VBox vertical;
    @FXML
    private Label lab;
    @FXML
    private Label totlabel;
    int a;
    float total = 0;

    @FXML
    protected void onButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Ledger-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void display(int n) throws SQLException {
        a = n;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
        String query = "SELECT * FROM reference WHERE Ledger_Ledger_ID='" + a+ "'";
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) {
            Label Name = new Label(rs.getString("product name"));
            Name.setMinWidth(140);
            Label Quantity = new Label(rs.getString("quanitity"));
            Quantity.setMinWidth(30);
            Label Total = new Label(rs.getString("value"));
            Total.setMinWidth(60);
            Total.setAlignment(Pos.BASELINE_RIGHT);
            Label Date = new Label(rs.getString("Date"));
            Date.setMinWidth(60);

            total += rs.getFloat("value");

            HBox hbox = new HBox(40); // spacing = 8
            hbox.getChildren().add(Name);
            hbox.getChildren().add(Quantity);
            hbox.getChildren().add(Total);
            hbox.getChildren().add(Date);
            vertical.getChildren().add(hbox);
            Line line = new Line(0,0,505,0);
            vertical.getChildren().add(line);

        }
        lab.setText(String.valueOf(a));
        String strprice = String.format("%.2f",total);
        totlabel.setText(strprice);
        connection.close();
    }
}
