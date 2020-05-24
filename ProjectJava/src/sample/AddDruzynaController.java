package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.TrenerModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddDruzynaController {
    @FXML
    private ComboBox<String> dyscyplinaTXT;
    @FXML
    private ComboBox<String> ligaTXT;
    @FXML
    private TextField nazwaTXT;
    @FXML
    private TableView<TrenerModel> table;
    @FXML
    private TableColumn<TrenerModel, String> imieCol;
    @FXML
    private TableColumn<TrenerModel, String> nazwiskoCol;
    @FXML
    private TableColumn<TrenerModel, String> peselCol;
    ObservableList<TrenerModel> oblist = FXCollections.observableArrayList();
    ObservableList<TrenerModel> oblistFiltered = FXCollections.observableArrayList();

    private String dyscyplina;

    private String nazwa;

    private String trenerId;



    @FXML
    public void initialize() throws SQLException {

        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Trener");
        while(rs.next()){
            oblist.add(new TrenerModel(rs.getString("Imię"),rs.getString("Nazwisko"),
                    rs.getString("Pesel")));
        }

        imieCol.setCellValueFactory(new PropertyValueFactory<TrenerModel,String>("imie"));
        nazwiskoCol.setCellValueFactory(new PropertyValueFactory<TrenerModel,String>("nazwisko"));
        peselCol.setCellValueFactory(new PropertyValueFactory<TrenerModel,String>("pesel"));
        table.setItems(oblist);

        ///choice box
        dyscyplinaTXT.setPromptText("Wybierz dyscyplinę");
        dyscyplinaTXT.getItems().add("Koszykówka");
        dyscyplinaTXT.getItems().add("Piłka nożna");
        dyscyplinaTXT.getItems().add("Piłka ręczna");
        dyscyplinaTXT.getItems().add("Siatkówka");

        //liga choicebox
        ligaTXT.setPromptText("Wybierz ligę");
        ligaTXT.getItems().add("SuperLiga");
        ligaTXT.getItems().add("1 Liga");
        ligaTXT.getItems().add("2 Liga");
        ligaTXT.getItems().add("3 Liga");
    }

    public void saveDruzyna(ActionEvent event) throws SQLException {
        dyscyplina=dyscyplinaTXT.getValue();
        nazwa=nazwaTXT.getText();
        String peselTrenera=table.getSelectionModel().getSelectedItem().getPesel();
        trenerId=getIdTrenera(peselTrenera);
       dodajDoBazy();
    }

    public String getIdTrenera(String pesel) throws SQLException {
        String id = null;
        ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Trener WHERE Pesel=" + "'" + pesel + "'");
        while (result.next()) {
            id = result.getString("Id");
        }
        return id;
    }

    private void dodajDoBazy(){

    }

}
