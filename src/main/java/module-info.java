module com.example.experiment1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.experiment1 to javafx.fxml;
    exports com.example.experiment1;
    exports com.example.experiment1.Controller;
    opens com.example.experiment1.Controller to javafx.fxml;
    opens com.example.experiment1.Class to javafx.base;
}