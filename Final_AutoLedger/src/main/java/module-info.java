module com.example.final_autoledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.final_autoledger to javafx.fxml;
    exports com.example.final_autoledger;
}