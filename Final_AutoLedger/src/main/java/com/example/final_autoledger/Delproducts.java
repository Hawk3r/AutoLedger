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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Delproducts implements Initializable {
    @FXML
    private Button exit;

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Label label;


    @FXML
    private VBox vertical;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private int editdel = 0;

    @FXML
    protected void ExitClick(ActionEvent event) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int a = 1;
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            Statement statement = connection.createStatement();

            ResultSet resultset = statement.executeQuery("select * from products");
            while (resultset.next()){
                if (resultset.getBoolean("status")){

                    Button button = new Button("-");
                    int finalA = resultset.getInt("Product_ID");

                    button.setOnAction(event -> {
                        Parent root = null;
                        try {
                            String state = "UPDATE `products` SET status = 0 WHERE Product_ID=?";
                            PreparedStatement psmt = connection.prepareStatement(state);
                            psmt.setInt(1,finalA);
                            psmt.executeUpdate();
                            root = FXMLLoader.load(getClass().getResource("Products-view.fxml"));
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);

                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);} catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    button.setStyle("-fx-background-color: #ff0000; ");

                    Label name = new Label(resultset.getString("Name"));
                    name.setMinWidth(270);
                    float pr = resultset.getFloat("price");
                    String strprice = String.format("%.2f",pr);
                    Label price = new Label(strprice);
                    price.setTextAlignment(TextAlignment.RIGHT);
                    price.setMinWidth(50);
                    price.setAlignment(Pos.BASELINE_RIGHT);

                    HBox hbox = new HBox(20); // spacing = 8
                    hbox.getChildren().add(new Label(String.valueOf(a)));
                    hbox.getChildren().add(name);
                    hbox.getChildren().add(price);
                    hbox.getChildren().add(button);
                    vertical.getChildren().add(hbox);
                    Line line = new Line(0,0,500,0);
                    vertical.getChildren().add(line);
                    a++;


                }
            }
        } catch (SQLException e) {throw new RuntimeException(e);}


    }

}

