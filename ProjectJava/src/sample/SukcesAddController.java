package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;



public class SukcesAddController {

    ///dane okna sukces
    @FXML
    private Label imiePrint;
    @FXML
    private Label nazwiskoPrint;
    @FXML
    private Label adresPrint;


    @FXML
    public void closeWindow(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void initialize() {
        imiePrint.setText(AddPlayerController.getImie());
        nazwiskoPrint.setText(AddPlayerController.getNazwisko());
        adresPrint.setText(AddPlayerController.getMiejscowosc()+"  "+AddPlayerController.getUlica()+"  "+AddPlayerController.getNr()+"  "+AddPlayerController.getKod());
    }



}
