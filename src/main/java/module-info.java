module com.example.experiment1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.experiment1 to javafx.fxml;
    exports com.example.experiment1;
}