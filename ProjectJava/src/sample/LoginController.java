package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameTXT;
    @FXML
    private TextField passwordTXT;
    @FXML
    private Label Status;


    public void Login(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(ConnectionDB.ConnectionDB(usernameTXT.getText(),passwordTXT.getText())){
            System.out.println("Witaj w bazie byczku");
           Parent view= FXMLLoader.load(getClass().getResource("viewsFXML/Main.fxml"));
           Scene scene=new Scene(view);
           Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
           window.setScene(scene);
           window.show();
        }else{
            Status.setText("Niepoprawny login lub hasło!");
        }
    }
}
