package com.example.final_autoledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Edit{
    @FXML
    private Button cancel;
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
    int a;
    String[] temparr = new String[0];





    @FXML
    protected void CancelClick(ActionEvent event) throws IOException {


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
            String state = "UPDATE `products` SET status = 0 WHERE Product_ID=?";
            PreparedStatement psmt = connection.prepareStatement(state);
            psmt.setInt(1,a);
            psmt.executeUpdate();
            root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));

            String state2 = "INSERT INTO `products`(`Name`,`price`,`status`,`Date Created`)VALUES(?,?,1,current_timestamp());";
            PreparedStatement psmt2 = connection.prepareStatement(state2);
            psmt2.setString(1,namefield.getText());
            psmt2.setFloat(2,f);
            psmt2.executeUpdate();
            connection.close();



            Parent root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }else warning.setText("Name has to be 45 characters or less, and price has to be a number");

    }



    public void display(int n) throws SQLException {
        a =n;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
        String query = "SELECT * FROM products WHERE Product_ID='" + a+ "'";
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) {
            namefield.setText(rs.getString("Name"));
            pricefield.setText(rs.getString("price"));
        }
        connection.close();
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
