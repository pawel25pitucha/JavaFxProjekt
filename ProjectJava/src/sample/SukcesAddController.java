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
        if(AddPlayerController.getImie()==null && AddSedziaController.getImie()==null){
            imiePrint.setText(AddTrenerController.getImie());
            nazwiskoPrint.setText(AddTrenerController.getNazwisko());
            adresPrint.setText(AddTrenerController.getMiejscowosc()+"  "+AddTrenerController.getUlica()+"  "+AddTrenerController.getNr()+"  "+AddTrenerController.getKod());
            AddTrenerController.setImie(null);
        }else if(AddPlayerController.getImie()==null && AddTrenerController.getImie()==null){
            imiePrint.setText(AddSedziaController.getImie());
            nazwiskoPrint.setText(AddSedziaController.getNazwisko());
            adresPrint.setText(AddSedziaController.getMiejscowosc()+"  "+AddSedziaController.getUlica()+"  "+AddSedziaController.getNr()+"  "+AddSedziaController.getKod());
            AddSedziaController.setImie(null);
        }else{
            imiePrint.setText(AddPlayerController.getImie());
            nazwiskoPrint.setText(AddPlayerController.getNazwisko());
            adresPrint.setText(AddPlayerController.getMiejscowosc()+"  "+AddPlayerController.getUlica()+"  "+AddPlayerController.getNr()+"  "+AddPlayerController.getKod());
            AddPlayerController.setImie(null);
        }
    }



}
