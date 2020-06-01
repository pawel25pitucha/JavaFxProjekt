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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.models.PlayerModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ZawodnicyController {

    private static String pesel;
    public static String getPesel() {
        return pesel;
    }

    @FXML
    private TableView<PlayerModel> table;
    @FXML
    private TableColumn<PlayerModel, String> imieCol;
    @FXML
    private TableColumn<PlayerModel,String> nazwiskoCol;
    @FXML
    private TableColumn<PlayerModel,String> peselCol;
    @FXML
    private TableColumn<PlayerModel,String> poziomCol;
    @FXML
    private TextField searchTXT;
    private String search;


    private ObservableList<PlayerModel> oblist = FXCollections.observableArrayList();
    private ObservableList<PlayerModel> oblistFiltered = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {

        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Zawodnik");
        while(rs.next()){
            oblist.add(new PlayerModel(rs.getString("Imię"),rs.getString("Nazwisko"),
                    rs.getString("Pesel"),rs.getString("Poziom")));
        }

        imieCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("imie"));
        nazwiskoCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("nazwisko"));
        peselCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("pesel"));
        poziomCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("poziom"));
        table.setItems(oblist);
    }

    public void changeViewAdd(ActionEvent event) throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Zawodnik/DodajZawodnika.fxml"));
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
    public String getPlayerId(String pesel) throws SQLException {
        String id = null;
        ResultSet druzynaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Zawodnik WHERE Pesel="+"'"+pesel+"'");
        while(druzynaSet.next()){
            id=druzynaSet.getString("Id");
        }
        return id;
    }
    public void deletePlayer(ActionEvent event) throws SQLException {
        PlayerModel deleted = table.getSelectionModel().getSelectedItem();
        if(deleted!=null){
            String peselDeleted=deleted.getPesel();
            String idplayer=getPlayerId(peselDeleted);
            Statement stmt = ConnectionDB.con.createStatement();
            ResultSet st=stmt.executeQuery("SELECT * FROM Zawodnik_has_Drużyna WHERE Zawodnik_Id="+"'"+idplayer+"'");
            if(st!=null){
                stmt.executeUpdate("DELETE Zawodnik_has_Drużyna FROM Zawodnik_has_Drużyna WHERE Zawodnik_Id="+"'"+idplayer+"'");
            }
            stmt.execute("DELETE Zawodnik FROM Zawodnik WHERE Pesel="+"'"+peselDeleted+"'");
            System.out.println("Usunieto Zawodnika :(");
            stmt.execute("DELETE Adres FROM Adres inner join Zawodnik On Adres.Id=Zawodnik.Adres_id WHERE Pesel="+"'"+peselDeleted+"'");
            System.out.println("Usunieto Adres Zawodnika");
            initialize();
        }
    }

    public void szczegoly() throws IOException {
        PlayerModel selected = table.getSelectionModel().getSelectedItem();
        if(selected!=null){
            pesel=selected.getPesel();
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Zawodnik/szczegoly.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.show();
        }
    }
    public void editPlayer(ActionEvent event) throws IOException {
        PlayerModel selected = table.getSelectionModel().getSelectedItem();
        if(selected!=null){
            pesel=selected.getPesel();
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Zawodnik/edytujZawodnika.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.show();
        }
    }

    //szukanie zawodnika po imieniu nazwisku i peselu
    public void searchPlayer() throws SQLException {
        System.out.println("Szukam");
        oblistFiltered.clear();
        search=searchTXT.getText();
        if(!search.isEmpty()){
            for(PlayerModel player : oblist){
                if(player.getPesel().matches(search+"[0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getImie().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getNazwisko().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getPoziom().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
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
