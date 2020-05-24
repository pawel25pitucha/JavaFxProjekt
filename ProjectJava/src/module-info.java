module ProjectJava {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    opens sample;
    opens sample.viewsFXML;
    exports sample.models;


}