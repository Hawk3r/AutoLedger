package com.example.final_autoledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Addproducts {
    @FXML
    private Button exit;

    @FXML
    private Button change;

    @FXML
    private TextField namefield;

    @FXML
    private TextField pricefield;

    @FXML
    private Label warning;

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    protected void ExitClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void ChangeClick(ActionEvent event) throws IOException, SQLException {
        boolean isnum = isNumeric(pricefield.getText());
        if ((namefield.getText().length() <= 45) && (isnum)){
            float f=Float.parseFloat(pricefield.getText());
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            String state = "INSERT INTO `products`(`Name`,`price`,`status`,`Date Created`)VALUES(?,?,1,current_timestamp());";
            PreparedStatement psmt = connection.prepareStatement(state);
            psmt.setString(1,namefield.getText());
            psmt.setFloat(2,f);
            psmt.executeUpdate();
            connection.close();


            Parent root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else warning.setText("Name has to be 45 characters or less, and price has to be a number");
    }




    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



}
