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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Cash_Register implements Initializable {

    @FXML
    private Button Quantity;

    @FXML
    private Button Enter;

    @FXML
    private Button Cancel;

    @FXML
    private Button Exit;

    @FXML
    private VBox Left;

    @FXML
    private VBox Right;

    @FXML
    private Text Product;

    @FXML
    private Text Total;

    @FXML
    private Text warning;

    @FXML
    private TextField Number;



    private Stage stage;
    private Scene scene;
    private Parent root;

    private ArrayList<String[]> lists = new ArrayList<String[]>();
    private int selected = 0;
    private float bill = 0;
    private ArrayList<String[]> order = new ArrayList<String[]>();

    @FXML
    protected void cancelClick(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Cash_Register-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void Enter(ActionEvent event) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
        Statement statement = connection.createStatement();
        if(isNumeric(Total.getText())){
            if(Float.parseFloat(Total.getText()) > 0){
                String state = "INSERT INTO `pos`.`ledger`(`Description`,`Inflow`,`Date`)VALUES('Cashier Transaction',?,current_timestamp);";
                PreparedStatement psmt = connection.prepareStatement(state);
                psmt.setFloat(1, Float.parseFloat(Total.getText()));
                psmt.executeUpdate();
                statement.execute("SET @customer_id = LAST_INSERT_ID() ;");
                for (int i=0;i<order.size();i++){
                    String[] aaa = order.get(i);
                    state = "INSERT INTO reference (`product_ID`,`quanitity`,`Date`,value, Ledger_Ledger_ID,`product name`)VALUES(?,?,current_timestamp(),?, @customer_id,?);";
                    PreparedStatement psmt2 = connection.prepareStatement(state);
                    psmt2.setInt(1, Integer.parseInt(aaa[0]));
                    psmt2.setInt(2, Integer.parseInt(aaa[3]));
                    psmt2.setFloat(3, Float.parseFloat(aaa[4]));
                    psmt2.setString(4, aaa[1]);
                    psmt2.executeUpdate();
                }
                Total.setText("");
                Right.getChildren().clear();
                warning.setText("Order entered");
                warning.setStyle("-fx-text-fill: #000000; ");
                order.clear();




            }else{
                warning.setStyle("-fx-text-fill: #ff0000; ");
                warning.setText("ERROR: Nothing entered");

            }

        }else {
            warning.setStyle("-fx-text-fill: #ff0000; ");
            warning.setText("How did this happen");

        }


    }



    @FXML
    protected void ExitClick(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void Quantity(ActionEvent event) throws IOException {
        if (isNumeric(Number.getText())){
            String st = Number.getText();
            int quant = Integer.parseInt(st);

            String[] temp = new String[5];
            String[] or = lists.get(selected);
            temp[0] = or[0];
            temp[1] = or[1];
            temp[2] = or[2];
            temp[3] = st;

            Float price = Float.parseFloat(or[2]);
            float total = quant*price;
            temp[4] = String.valueOf(total);
            System.out.println(temp[4]);
            order.add(temp);
            Number.setText("");
            Product.setText("");
            bill += total;
            Total.setText(String.valueOf(bill));
            rightbox();
        }
        else{
            warning.setStyle("-fx-text-fill: #ff0000; ");
            warning.setText("ERROR: only whole numbers for quantity please");

        }


    }

    @FXML
    private void rightbox(){
        Right.getChildren().clear();
        for(int i = 0; i<order.size(); i++){
            String[] temporary = order.get(i);

            Button button = new Button("-");
            int finalI = i;
            button.setOnAction(event -> {
                order.remove(finalI);
                bill -= Float.parseFloat(temporary[4]);
                Total.setText(String.valueOf(bill));
                rightbox();
            });
            button.setStyle("-fx-background-color: #ff0000; ");
            button.setMaxHeight(20);
            button.setMinHeight(20);

            HBox hbox = new HBox(20); // spacing = 8
            Label name = new Label(temporary[1]);
            name.setMinWidth(180);

            Label q = new Label(temporary[3]);
            q.setMinWidth(20);

            float pri = Float.parseFloat(temporary[4]);
            Label pr = new Label(String.format("%.2f",pri));
            pr.setMinWidth(50);
            pr.setTextAlignment(TextAlignment.RIGHT);
            pr.setAlignment(Pos.BASELINE_RIGHT);
            System.out.println(pr.getWidth());

            hbox.getChildren().add(name);
            hbox.getChildren().add(q);
            hbox.getChildren().add(pr);
            hbox.getChildren().add(button);
            Right.getChildren().add(hbox);



            Line line = new Line(0,0,350,0);
            Right.getChildren().add(line);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int counter=0;

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            Statement statement = connection.createStatement();

            ResultSet resultset = statement.executeQuery("select * from products where status = 1");

            while (resultset.next()){


                String[] temparr = new String[3];
                temparr[0]= resultset.getString("Product_ID");
                temparr[1]= resultset.getString("Name");
                temparr[2] = resultset.getString("price");

                lists.add(temparr);
                //all for the button
                Button button = new Button("Add");

                int finalCounter = counter;
                button.setOnAction(event -> {
                    Product.setText(temparr[1]);
                    selected = finalCounter;
                });


                HBox hbox = new HBox(20); // spacing = 8

                Label Name = new Label(temparr[1]);
                Name.setMinWidth(220);

                float pr = resultset.getFloat("price");
                String strprice = String.format("%.2f",pr);
                Label price = new Label(strprice);
                price.setTextAlignment(TextAlignment.RIGHT);
                price.setMinWidth(50);
                price.setAlignment(Pos.BASELINE_RIGHT);
                hbox.getChildren().add(new Label(""));
                hbox.getChildren().add(Name);
                hbox.getChildren().add(price);
                hbox.getChildren().add(button);
                Left.getChildren().add(hbox);
                Line line = new Line(0,0,350,0);
                Left.getChildren().add(line);
                counter++;
            }connection.close();



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


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
        System.out.println(strNum);
        return true;
    }
}
