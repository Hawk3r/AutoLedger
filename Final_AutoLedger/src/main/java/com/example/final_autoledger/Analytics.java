package com.example.final_autoledger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Analytics implements Initializable {
    @FXML
    private Button exit;

    @FXML
    private Label income;

    @FXML
    private Label transactions;

    @FXML
    private LineChart<?,?> transactChart;

    @FXML
    private LineChart<?,?> incomeChart;




    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label label;
    int[] productslist;
    int[] amount_sold;
    String[] namelist;

    int[] weektrans = new int[7];
    int[] weekavg = new int[7];


    @FXML
    protected void onButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            Statement stm = connection.createStatement();
            String query = "select count(*) from products";
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            int a  = rs.getInt(1);
            int highest = 0;
            int count_high=0;
            productslist = new int[a];
            amount_sold = new int[a];
            namelist = new String[a];
            rs = stm.executeQuery("select * from products order by Product_ID asc");
            int counter = 0;
            while (rs.next()){
                productslist[counter]= rs.getInt("Product_ID");
                amount_sold[counter] = 0;
                namelist[counter] = rs.getString("Name");
                counter++;
            }

            rs = stm.executeQuery("select * from reference where Date >= DATE_SUB(current_timestamp(), INTERVAL 1 week) order by 'product name' asc;");
            while (rs.next()){
                for (int i=0;i<amount_sold.length;i++){
                    if(rs.getInt("product_ID")==productslist[i]){
                        amount_sold[i]++;
                    }
                }
            }

            for (int i=0;i<amount_sold.length;i++){
                if(amount_sold[i]>=highest){
                    highest = amount_sold[i];
                    count_high = i;}}
            label.setText(namelist[count_high]);


            //for day 1
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 1 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 2 day)");

            day(rs,0);

            //for day2
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 2 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 3 day)");
            day(rs,1);

            //foor day 3
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 3 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 4 day)");
            day(rs,2);

            //for day 4
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 4 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 5 day)");
            day(rs,3);

            //for day 5
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 5 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 6 day)");
            day(rs,4);

            //for day6
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 6 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 7 day)");
            day(rs,5);


            //for day 7
            rs = stm.executeQuery("select * from ledger where Date <= DATE_SUB(current_timestamp(), INTERVAL 8 day) and Date >= DATE_SUB(current_timestamp(), INTERVAL 7 day)");
            day(rs,6);


            int avgtrans =avg(weektrans);
            float avginc =avg(weekavg);
            income.setText(String.valueOf(avginc));
            transactions.setText(String.valueOf(avgtrans));

            transactChart.getXAxis().setLabel("x days ago");
            transactChart.getYAxis().setLabel("# of transactions");
            XYChart.Series series = new XYChart.Series();
            series.setName("daily transactions");
            for (int i =0;i < weektrans.length;i++){
                int av = i+1;
                series.getData().add(new XYChart.Data(String.valueOf(av),weektrans[i]));
            }

            transactChart.getData().add(series);


            series = new XYChart.Series();
            series.setName("daily income");
            for (int i =0;i < weektrans.length;i++){
                int av = i+1;
                series.getData().add(new XYChart.Data(String.valueOf(av),weekavg[i]));
            }
            incomeChart.getData().add(series);
            incomeChart.getXAxis().setLabel("x days ago");
            incomeChart.getYAxis().setLabel("amount earned");






            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public void day(ResultSet rs, int day) throws SQLException {
        int counter = 0;
        int total = 0;

        while (rs.next()){
            if (rs.getInt("Inflow")> 0){
                counter ++;
                total += rs.getInt("Inflow");
            }
        }
        weektrans[day] = counter;
        if (counter ==0) counter =1;
        weekavg[day] = (total);


    }

    public int avg(int[] array){
        int counter=0;
        for(int i=0;i< array.length;i++){
            counter += array[i];
        }
        int avg = counter/array.length;
        return avg;
    }
}
