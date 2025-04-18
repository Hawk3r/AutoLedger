package com.example.final_autoledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button Analytics;
    @FXML
    private Button Funds;
    @FXML
    private Button Ledger;
    @FXML
    private Button Products;
    @FXML
    private Button Register;

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void AnalyticsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Analytics-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void expenseClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Log_Expense-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void addClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Log_AddFunds-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void LedgersClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Ledger-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void ProductsClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void RegisterClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Cash_Register-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void closeButtonAction(ActionEvent event) throws  IOException{
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.close();
    }
}