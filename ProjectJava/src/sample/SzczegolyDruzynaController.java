package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SzczegolyDruzynaController {
    @FXML
    private Text nazwaTXT;
    @FXML
    private Text dyscyplinaTXT;
    @FXML
    private Text ligaTXT;
    @FXML
    private Text dataTXT;


    private String nazwa;


    public void initialize() throws SQLException {
        nazwa=DruzynaController.getNazwa();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Drużyna WHERE Nazwa="+"'"+nazwa+"'");

        while(rs.next()) {
            nazwaTXT.setText(rs.getString("Nazwa"));
            dyscyplinaTXT.setText(DruzynaController.getDyscyplina());
            ligaTXT.setText(DruzynaController.getLiga());
            dataTXT.setText(rs.getString("Data_zalozenia"));
        }
    }


    public void close(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
