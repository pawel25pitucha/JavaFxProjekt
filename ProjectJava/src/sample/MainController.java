package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public void changeViewDodajZawodnik(ActionEvent event) throws IOException {
        Parent dodajView= FXMLLoader.load(getClass().getResource("DodajZawodnika.fxml"));
        Scene scene=new Scene(dodajView);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


}
