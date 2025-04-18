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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Ledger implements Initializable {
    @FXML
    private Button exit;

    @FXML
    private Label label;
    @FXML
    private Label Tot;
    private float total;

    @FXML
    private VBox vertical;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String[] printable;




    @FXML
    protected void ExitClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void currentmonth(ActionEvent event) throws SQLException {
        vertical.getChildren().clear();
        try {

            changetable("select * from ledger where Date >= DATE_SUB(current_timestamp(), INTERVAL 1 month) order by Ledger_ID desc");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        label.setText("Current Month");
    }

    @FXML
    protected void monthAgo(ActionEvent event) throws SQLException {
        vertical.getChildren().clear();
        try {
            changetable("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 1 month) and Date >= DATE_SUB(current_timestamp(), INTERVAL 2 month) order by Ledger_ID desc;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        label.setText("Last Month");
    }

    @FXML
    protected void twoMonthsAgo(ActionEvent event) throws SQLException {
        vertical.getChildren().clear();
        try {
            changetable("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 2 month) and Date >= DATE_SUB(current_timestamp(), INTERVAL 3 month) order by Ledger_ID desc;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        label.setText("2 Months ago");
    }

    @FXML
    protected void threeMonthsAgo(ActionEvent event) throws SQLException {
        vertical.getChildren().clear();
        try {
            changetable("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 3 month) and Date >= DATE_SUB(current_timestamp(), INTERVAL 4 month)order by Ledger_ID desc;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        label.setText("3 Months ago");
    }

    @FXML
    protected void fourMonthsAgo(ActionEvent event) throws SQLException {
        vertical.getChildren().clear();
        try {
            changetable("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 4 month) and Date >= DATE_SUB(current_timestamp(), INTERVAL 5 month)order by Ledger_ID desc;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        label.setText("4 Months ago");
    }

    @FXML
    protected void history(ActionEvent event) throws SQLException {
        vertical.getChildren().clear();
        try {
            changetable("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 5 month) order by Ledger_ID desc;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        label.setText("history");
    }

    @FXML
    protected void print(ActionEvent event) throws SQLException, FileNotFoundException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
        Statement stm = connection.createStatement();
        String query = "select count(*) from ledger where Date >= DATE_SUB(current_timestamp(), INTERVAL 1 month)";
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        int a  = rs.getInt(1);
        String[] printable = new String[a];
        int counter = 0;

        rs = stm.executeQuery("select * from ledger where Date >= DATE_SUB(current_timestamp(), INTERVAL 1 month) order by Ledger_ID desc;");
        while (rs.next()){
            String ID= rs.getString("Ledger_ID");
            String Desc= rs.getString("Description");
            String inf= rs.getString("Inflow");
            String outf= rs.getString("Outflow");
            String Date= rs.getString("Date");
            printable[counter] = ID+","+Desc+","+inf+","+outf+","+Date;
            counter++;
        }
        FileChooser fc = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fc.getExtensionFilters().add(extFilter);
        fc.setTitle("Save as");
        File file = fc.showSaveDialog(stage);
        if (file !=null){
            PrintStream write = new PrintStream(file);
            write.println("ID, Description,Inflow,Outflow,Date");
            for (int i=0;i<printable.length;i++){
                write.println(printable[i]);
            }
            write.flush();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            changetable("select * from ledger where Date >= DATE_SUB(current_timestamp(), INTERVAL 1 month) order by Ledger_ID desc;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        label.setText("Current Month");
    }


    private void changetable(String month) throws SQLException {
        total =0;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
        Statement statement = connection.createStatement();
        ResultSet resultset = statement.executeQuery(month);
        while (resultset.next()){
            Label ID = new Label(resultset.getString("Ledger_ID"));
            ID.setMinWidth(60);
            ID.setAlignment(Pos.BASELINE_RIGHT);
            Label desc = new Label(resultset.getString("Description"));
            desc.setMinWidth(130);
            Button button = new Button("view");
            String a = (resultset.getString("Ledger_ID"));

            if (resultset.getString("Description").equals("Cashier Transaction")){
                button.setOnAction(event -> {
                    Parent root = null;
                    try {
                        FXMLLoader lod = new FXMLLoader(getClass().getResource("LedgerMoreInfo-view.fxml"));
                        root = lod.load();
                        LedgerMoreInfo ledgerMoreInfo = lod.getController();
                        ledgerMoreInfo.display(Integer.valueOf(a));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);} catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            float In = resultset.getFloat("Inflow");
            total += In;
            String Inflow = String.format("%.2f",In);
            if (In ==0){
                Inflow = "";
            }
            Label inf = new Label(Inflow);
            inf.setTextAlignment(TextAlignment.RIGHT);
            inf.setMinWidth(50);
            inf.setAlignment(Pos.BASELINE_RIGHT);

            float Out = resultset.getFloat("Outflow");
            total -= Out;
            String Outflow = String.format("%.2f",Out);
            if (Out ==0){
                Outflow = "";
            }
            Label outf = new Label(Outflow);
            outf.setTextAlignment(TextAlignment.RIGHT);
            outf.setMinWidth(50);
            outf.setAlignment(Pos.BASELINE_RIGHT);

            Label date = new Label(resultset.getString("Date"));
            date.setMinWidth(60);

            HBox hbox = new HBox(40); // spacing = 8
            hbox.getChildren().add(ID);
            hbox.getChildren().add(desc);
            hbox.getChildren().add(inf);
            hbox.getChildren().add(outf);
            hbox.getChildren().add(date);
            if (resultset.getString("Description").equals("Cashier Transaction")){
                hbox.getChildren().add(button);
            }

            vertical.getChildren().add(hbox);
            Line line = new Line(0,0,660,0);
            vertical.getChildren().add(line);


        }connection.close();
        String ttl = String.format("%.2f",total);
        Tot.setText(ttl);

    }


}

