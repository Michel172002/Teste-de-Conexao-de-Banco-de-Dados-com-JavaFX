module com.example.bancodedados {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bancodedados to javafx.fxml;
    exports com.example.bancodedados;
}