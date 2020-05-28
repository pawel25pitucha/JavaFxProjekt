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
import sample.models.DruzynaModel;

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
    private String getLigaNazwa(String Liga_Id) throws SQLException {
        String nazwa = null;
        ResultSet ligaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Liga WHERE Id="+"'"+Liga_Id+"'");
        while(ligaSet.next()){
              nazwa=ligaSet.getString("Nazwa");
        }
       return nazwa;
    }
    private String getDyscyplinaNazwa(String Dyscyplina_Id) throws SQLException {
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
        if(table.getSelectionModel().getSelectedItem()!=null){
        nazwa=table.getSelectionModel().getSelectedItem().getNazwa();
        liga=table.getSelectionModel().getSelectedItem().getLiga();
        dyscyplina=table.getSelectionModel().getSelectedItem().getDyscyplina();
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Druzyna/EdytujDruzyna.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.show();
        }
    }
    public void dodajZawodnikow() throws IOException {
        if(table.getSelectionModel().getSelectedItem()!=null){
            nazwa=table.getSelectionModel().getSelectedItem().getNazwa();
            System.out.println(nazwa);
            liga=table.getSelectionModel().getSelectedItem().getLiga();
            dyscyplina=table.getSelectionModel().getSelectedItem().getDyscyplina();
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Druzyna/DodajZawodnikow.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.show();
        }
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
    public String getDruzynaId(String nazwa) throws SQLException {
        String id = null;
        ResultSet druzynaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Drużyna WHERE Nazwa="+"'"+nazwa+"'");
        while(druzynaSet.next()){
            id=druzynaSet.getString("Id");
        }
        return id;
    }

    public void deleteTeam(ActionEvent event) throws SQLException {
        DruzynaModel deleted = table.getSelectionModel().getSelectedItem();
        if(deleted!=null){
            String nazwaDeleted=deleted.getNazwa();
            String druzynaId=getDruzynaId(nazwaDeleted);
            Statement stmt = ConnectionDB.con.createStatement();
            ResultSet result=stmt.executeQuery("SELECT * FROM Trener_has_Drużyna WHERE Drużyna_Id="+"'"+druzynaId+"'");
            if(result!=null){
                ConnectionDB.con.createStatement().execute("DELETE Trener_has_Drużyna FROM Trener_has_Drużyna WHERE Drużyna_Id="+"'"+druzynaId+"'");
            }
            ResultSet result2=stmt.executeQuery("SELECT GospodarzID FROM Spotkanie WHERE (GospodarzID="+"'"+druzynaId+"'"+"  or  GośćID="+"'"+druzynaId+"')");
            if(result2!=null){
                while(result2.next()){
                    String gospodarzId=result2.getString(1);
                    if(gospodarzId.equals(druzynaId)){
                        System.out.println("hej");
                        ConnectionDB.con.createStatement().execute("DELETE Spotkanie FROM Spotkanie WHERE GospodarzID="+"'"+druzynaId+"'");
                    }else{
                        System.out.println("hej2");
                        ConnectionDB.con.createStatement().execute("DELETE Spotkanie FROM Spotkanie WHERE GośćID="+"'"+druzynaId+"'");
                    }
                }
            }
            ResultSet result3=stmt.executeQuery("SELECT * FROM Zawodnik_has_Drużyna WHERE Drużyna_Id="+"'"+druzynaId+"'");
            if(result3!=null){
                ConnectionDB.con.createStatement().execute("DELETE Zawodnik_has_Drużyna FROM Zawodnik_has_Drużyna WHERE Drużyna_Id="+"'"+druzynaId+"'");
            }
            ConnectionDB.con.createStatement().execute("DELETE Drużyna FROM Drużyna WHERE Id="+"'"+druzynaId+"'");
            System.out.println("Usunieto Druzyne :(");
            initialize();
        }
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
