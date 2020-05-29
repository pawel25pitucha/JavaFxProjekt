package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.models.DruzynaModel;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SzukajDruzynyController {
    @FXML
    private TableView<DruzynaModel> table;
    @FXML
    private TableColumn nazwaCol;
    @FXML
    private TableColumn dyscyplinaCol;
    @FXML
    private TableColumn ligaCol;
    
    private static String nazwa1;
    private static String nazwa2;
    private String helper;

    public static String getNazwa1() {
        return nazwa1;
    }
    public static String getNazwa2() {
        return nazwa2;
    }

    public void setTable(TableView<DruzynaModel> table) {
        this.table = table;
    }

    public static void setNazwa1(String nazwa1) {
        SzukajDruzynyController.nazwa1 = nazwa1;
    }

    public static void setNazwa2(String nazwa2) {
        SzukajDruzynyController.nazwa2 = nazwa2;
    }

    private ObservableList<DruzynaModel> oblist = FXCollections.observableArrayList();
    private ObservableList<DruzynaModel> oblistFiltered = FXCollections.observableArrayList();
    @FXML
    public void initialize() throws SQLException {
        if(AddSpotkanieController.getHelper()==null){
           helper= EditSpotkanieController.getHelper();
        }else{
            helper=AddSpotkanieController.getHelper();
        }

        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Drużyna");
        while(rs.next()){
            String Liga_Id=rs.getString("Liga_Id");
            String Dyscyplina_Id=rs.getString("Dyscyplina_Id");
            String liga = getLigaNazwa(Liga_Id);
            String dyscyplina = getDyscyplinaNazwa(Dyscyplina_Id);
            oblist.add(new DruzynaModel(rs.getString("Nazwa"),liga,dyscyplina));
        }
        nazwaCol.setCellValueFactory(new PropertyValueFactory<DruzynaModel,String>("nazwa"));
        dyscyplinaCol.setCellValueFactory(new PropertyValueFactory<DruzynaModel,String>("dyscyplina"));
        ligaCol.setCellValueFactory(new PropertyValueFactory<DruzynaModel,String>("liga"));
        table.setItems(oblist);
    }
    public String getLigaNazwa(String Liga_Id) throws SQLException {
        String nazwa = null;
        ResultSet ligaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Liga WHERE Id="+"'"+Liga_Id+"'");
        while(ligaSet.next()){
            nazwa=ligaSet.getString("Nazwa");
        }
        return nazwa;
    }
    public String getDyscyplinaNazwa(String Dyscyplina_Id) throws SQLException {
        String nazwa = null;
        ResultSet dyscyplinaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Dyscyplina WHERE Id="+"'"+Dyscyplina_Id+"'");
        while(dyscyplinaSet.next()){
            nazwa=dyscyplinaSet.getString("Nazwa");
        }
        return nazwa;
    }
    public void dodajDruzyne(ActionEvent event) throws IOException {
        if(helper.equals("1")) nazwa1=table.getSelectionModel().getSelectedItem().getNazwa();
        else nazwa2=table.getSelectionModel().getSelectedItem().getNazwa();
        if(AddSpotkanieController.getHelper()==null){
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/EdytujSpotkanie.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.setResizable(false);
            window.show();
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }else{
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/DodajSpotkanie.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.setResizable(false);
            window.show();
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }
}
