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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Log_Funds {

    @FXML
    private Button exit;
    @FXML
    private Button add;
    @FXML
    private Button confirm;

    @FXML
    private TextField descfield;
    @FXML
    private TextField costfield;
    @FXML
    private Label warning;

    private Stage stage;
    private Scene scene;
    private Parent root;
    int i = 0;


    @FXML
    protected void ExitClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void addExpense(ActionEvent event) throws IOException{

        boolean isnum = isNumeric(costfield.getText());
        if ((descfield.getText().length() <= 45) && (isnum) ){
            i =1;
            warning.setText("press confirm to confirm entry");
        }else warning.setText("description has to be 45 characters or less and amount has to be a number");


    }

    @FXML
    protected void addFunds(ActionEvent event) throws IOException{

        boolean isnum = isNumeric(costfield.getText());
        if ((descfield.getText().length() <= 45) && (isnum) ){
            i =1;
            warning.setText("press confirm to confirm entry");

        }else warning.setText("description has to be 45 characters or less and amount has to be a number");

    }


    @FXML
    protected void confirmaddition(ActionEvent event) throws SQLException {
        if (i == 1) {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            String state = "INSERT INTO `pos`.`ledger`(`Description`,`Inflow`,`Date`)VALUES(?,?,current_timestamp());";
            PreparedStatement psmt = connection.prepareStatement(state);
            psmt.setString(1,descfield.getText());
            psmt.setFloat(2,Float.parseFloat(costfield.getText()));
            psmt.executeUpdate();
            connection.close();
            i =0;
            descfield.clear();
            costfield.clear();
        }
        else warning.setText("press add first before pressing confirm. " +
                "Please double check your entry, once it is entered it cannot be deleted.");


    }

    @FXML
    protected void confirmexpense(ActionEvent event) throws SQLException {
        if (i == 1) {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            String state = "INSERT INTO `pos`.`ledger`(`Description`,`Outflow`,`Date`)VALUES(?,?,current_timestamp());";
            PreparedStatement psmt = connection.prepareStatement(state);
            psmt.setString(1,descfield.getText());
            psmt.setFloat(2,Float.parseFloat(costfield.getText()));
            psmt.executeUpdate();
            connection.close();
            i =0;
            descfield.clear();
            costfield.clear();
        }
        else warning.setText("press add first before pressing confirm. Please double check your entry, once it is entered it cannot be deleted.");

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
