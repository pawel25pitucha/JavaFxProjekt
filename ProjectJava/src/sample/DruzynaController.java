package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DruzynaController {


    public void changeViewAddDruzyna() throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Druzyna/DodajDruzyna.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.show();
    }
    public void getBack(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Main.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.show();
    }
}
