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
import sample.models.SedziaModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SedziowieController {

    private static String pesel;

    public static String getPesel() {
        return pesel;
    }

    @FXML
    private TableView<SedziaModel> table;
    @FXML
    private TableColumn<SedziaModel, String> imieCol;
    @FXML
    private TableColumn<SedziaModel,String> nazwiskoCol;
    @FXML
    private TableColumn<SedziaModel,String> peselCol;
    @FXML
    private TableColumn<SedziaModel,String> poziomCol;
    @FXML
    private TextField searchTXT;
    private String search;


    ObservableList<SedziaModel> oblist = FXCollections.observableArrayList();
    ObservableList<SedziaModel> oblistFiltered = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {

        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Sędzia");
        while(rs.next()){
            oblist.add(new SedziaModel(rs.getString("Imię"),rs.getString("Nazwisko"),
                    rs.getString("Pesel")));
        }

        imieCol.setCellValueFactory(new PropertyValueFactory<SedziaModel,String>("imie"));
        nazwiskoCol.setCellValueFactory(new PropertyValueFactory<SedziaModel,String>("nazwisko"));
        peselCol.setCellValueFactory(new PropertyValueFactory<SedziaModel,String>("pesel"));
        table.setItems(oblist);
    }

    public void changeViewAdd(ActionEvent event) throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Sedzia/DodajSedziego.fxml"));
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


    public void deleteSedzia(ActionEvent event) throws SQLException {
        SedziaModel deleted = table.getSelectionModel().getSelectedItem();
        String peselDeleted=deleted.getPesel();
        Statement stmt = ConnectionDB.con.createStatement();
        stmt.execute("DELETE Sędzia FROM Sędzia WHERE Pesel="+"'"+peselDeleted+"'");
        System.out.println("Usunieto Sędziego :(");
        stmt.execute("DELETE Adres FROM Adres inner join Sędzia On Adres.Id=Sędzia.Adres_id WHERE Pesel="+"'"+peselDeleted+"'");
        System.out.println("Usunieto Adres Sędziego");
        initialize();
    }

    public void editSedzia(ActionEvent event) throws IOException {
        SedziaModel selected = table.getSelectionModel().getSelectedItem();
        pesel=selected.getPesel();
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Sedzia/edytujSedziego.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.show();
    }



    //szukanie zawodnika po imieniu nazwisku i peselu
    public void searchSedzia() throws SQLException {
        System.out.println("Szukam");
        oblistFiltered.clear();
        search=searchTXT.getText();
        if(!search.isEmpty()){
            for(SedziaModel player : oblist){
                if(player.getPesel().matches(search+"[0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getImie().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getNazwisko().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
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
