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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Funds implements Initializable {
    @FXML
    private Button exit;

    @FXML
    private VBox vertical;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    protected void ExitClick(ActionEvent event) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/temp_transactions.csv"));
        writer.write("");
        writer.close();

        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void ExpenseClick(ActionEvent event) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("Log_Expense-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void AddfundsClick(ActionEvent event) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("Log_AddFunds-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void Add2Database(ActionEvent event) throws IOException, SQLException {
        File file = new File("assets/temp_transactions.csv");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String a = scanner.nextLine();
                String[] entry = a.split(",");
                if (entry.length ==2){
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
                    String state = "INSERT INTO `pos`.`ledger`(`Description`,`Inflow`,`Date`)VALUES(?,?,current_timestamp());";
                    PreparedStatement psmt = connection.prepareStatement(state);
                    psmt.setString(1,entry[0]);
                    psmt.setFloat(2,Float.parseFloat(entry[1]));
                    psmt.executeUpdate();
                    connection.close();
                }else{
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
                    String state = "INSERT INTO `pos`.`ledger`(`Description`,`Outflow`,`Date`)VALUES(?,?,current_timestamp());";
                    PreparedStatement psmt = connection.prepareStatement(state);
                    psmt.setString(1,entry[0]);
                    psmt.setFloat(2,Float.parseFloat(entry[2]));
                    psmt.executeUpdate();
                    connection.close();
                }

            }scanner.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("assets/temp_transactions.csv"));
            writer.write("");
            writer.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



        Parent root = FXMLLoader.load(getClass().getResource("Funds-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int a = 1;
        try{
            File file = new File("assets/temp_transactions.csv");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()== true){
                String temp = scanner.nextLine();
                String[] temparr = temp.split(",");


                //all for the button
                Button button = new Button("-");
                button.setStyle("-fx-background-color: #ff0000; ");
                int finalA = a;


                button.setOnAction(event -> {
                    Parent root = null;
                    try {
                        Delete(finalA);
                        root = FXMLLoader.load(getClass().getResource("Funds-view.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);}
                });



                Label Desc = new Label(temparr[0]);
                Desc.setMinWidth(280);
                Label Inflow = new Label(temparr[1]);
                Inflow.setMinWidth(70);
                Label Outflow= new Label();
                if (temparr.length == 2){Outflow.setText("");} else if (temparr.length == 3) {
                    Outflow.setText(temparr[2]);}

                Outflow.setMinWidth(30);
                HBox hbox = new HBox(30); // spacing = 8
                System.out.println(Desc.getWidth());
                hbox.getChildren().add(Desc);
                hbox.getChildren().add(Inflow);
                hbox.getChildren().add(Outflow);
                hbox.getChildren().add(button);
                vertical.getChildren().add(hbox);
                Line line = new Line(0,0,1000,0);
                vertical.getChildren().add(line);
                a++;



            }scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);}
    }


    public void Delete(int a) throws IOException {
        String[] temp2 = new String[0];

        File file = new File("assets/temp_transactions.csv");
        Scanner scanner = new Scanner(file);

        int size = 0;
        while (scanner.hasNextLine()){
            String temp = scanner.nextLine();
            size++;
        }
        temp2 = new String[size];
        Scanner scanner2 = new Scanner(file);
        int in = 0;
        int index = 1;

        while (scanner2.hasNextLine()){
            String temp = scanner2.nextLine();
            if (index == a){
            }else{
                temp2[in]= temp;
                in++;
            }
            index++;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("assets/temp_transactions.csv"));

        for (int i = 0;i<size-1;i++){
            writer.append(temp2[i]+"\n");
        }
        writer.close();



    }
}
