package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import sample.models.DruzynaModel;
import sample.models.PlayerModel;
import sample.models.TrenerModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DruzynaController {


    @FXML
    private TableView<DruzynaModel> table;
    @FXML
    private TableColumn nazwaCol;
    @FXML
    private TableColumn dyscyplinaCol;
    @FXML
    private TableColumn iloscZawodnikowCol;
    @FXML
    private Label liczbaTXT;
    @FXML
    private TextField searchTXT;
    private String ilosc;
    private static String nazwa;
    private static String dyscyplina;
    private static String liga;

    public static String getNazwa() {
        return nazwa;
    }

    public TableView<DruzynaModel> getTable() {
        return table;
    }

    public static String getDyscyplina() {
        return dyscyplina;
    }

    public static String getLiga() {
        return liga;
    }

    ObservableList<DruzynaModel> oblist = FXCollections.observableArrayList();
    ObservableList<DruzynaModel> oblistFiltered = FXCollections.observableArrayList();
    @FXML
    public void initialize() throws SQLException {
        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Drużyna");
        ResultSet liczbaSet = ConnectionDB.con.createStatement().executeQuery(" SELECT COUNT(*) as 'liczba' FROM Drużyna ");
        while(liczbaSet.next()){
            ilosc=liczbaSet.getString("liczba");
        }
        liczbaTXT.setText(ilosc);

        while(rs.next()){
            String Liga_Id=rs.getString("Liga_Id");
            String Dyscyplina_Id=rs.getString("Dyscyplina_Id");
            String liga = getLigaNazwa(Liga_Id);
            String dyscyplina = getDyscyplinaNazwa(Dyscyplina_Id);
            oblist.add(new DruzynaModel(rs.getString("Nazwa"),liga,dyscyplina));
        }
        nazwaCol.setCellValueFactory(new PropertyValueFactory<DruzynaModel,String>("nazwa"));
        dyscyplinaCol.setCellValueFactory(new PropertyValueFactory<DruzynaModel,String>("dyscyplina"));
        iloscZawodnikowCol.setCellValueFactory(new PropertyValueFactory<DruzynaModel,String>("liga"));
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
        System.out.println("4");
        String nazwa = null;
        ResultSet dyscyplinaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Dyscyplina WHERE Id="+"'"+Dyscyplina_Id+"'");
        while(dyscyplinaSet.next()){
            nazwa=dyscyplinaSet.getString("Nazwa");
        }
        return nazwa;
    }
    public void changeViewAddDruzyna() throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Druzyna/DodajDruzyna.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.show();
    }
    public void changeViewEditDruzyna() throws IOException {
        nazwa=table.getSelectionModel().getSelectedItem().getNazwa();
        liga=table.getSelectionModel().getSelectedItem().getLiga();
        dyscyplina=table.getSelectionModel().getSelectedItem().getDyscyplina();
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Druzyna/EdytujDruzyna.fxml"));
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

    public void deleteTeam(ActionEvent event) throws SQLException {
        DruzynaModel deleted = table.getSelectionModel().getSelectedItem();
        String nazwaDeleted=deleted.getNazwa();
        Statement stmt = ConnectionDB.con.createStatement();
        stmt.execute("DELETE Drużyna FROM Drużyna WHERE Nazwa="+"'"+nazwaDeleted+"'");
        System.out.println("Usunieto Druzyne :(");
        initialize();
    }
    public void searchTeam() throws SQLException {
        System.out.println("Szukam");
        oblistFiltered.clear();
        String search=searchTXT.getText();
        if(!search.isEmpty()){
            for(DruzynaModel player : oblist){
                if(player.getNazwa().matches(search+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getDyscyplina().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getLiga().toLowerCase().matches(search.toLowerCase()+".*")){
                    oblistFiltered.add(player);
                }
            }
            if(!oblistFiltered.isEmpty()){
                //table.getItems().clear();
                table.setItems(oblistFiltered);
            }
        } else {
            initialize();
        }

    }
}
