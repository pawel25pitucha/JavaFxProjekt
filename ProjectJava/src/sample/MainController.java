package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainController {

    public void changeViewAdd(ActionEvent event) throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("DodajZawodnika.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.show();
    }


}
