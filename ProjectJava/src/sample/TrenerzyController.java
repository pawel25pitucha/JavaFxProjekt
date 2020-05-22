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
import sample.models.TrenerModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TrenerzyController {


        private static String pesel;

        public static String getPesel() {
            return pesel;
        }

        @FXML
        private TableView<TrenerModel> table;
        @FXML
        private TableColumn<TrenerModel, String> imieCol;
        @FXML
        private TableColumn<TrenerModel, String> nazwiskoCol;
        @FXML
        private TableColumn<TrenerModel, String> peselCol;
        @FXML
        private TextField searchTXT;
        private String search;


        ObservableList<TrenerModel> oblist = FXCollections.observableArrayList();
        ObservableList<TrenerModel> oblistFiltered = FXCollections.observableArrayList();

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
        }

        public void changeViewAdd(ActionEvent event) throws IOException {
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/DodajTrenera.fxml"));
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


        public void deleteTrener(ActionEvent event) throws SQLException {
            TrenerModel deleted = table.getSelectionModel().getSelectedItem();
            String peselDeleted=deleted.getPesel();
            Statement stmt = ConnectionDB.con.createStatement();
            stmt.execute("DELETE Trener FROM Trener WHERE Pesel="+"'"+peselDeleted+"'");
            System.out.println("Usunieto Trenera :(");
            stmt.execute("DELETE Adres FROM Adres inner join Trener On Adres.Id=Trener.Adres_id WHERE Pesel="+"'"+peselDeleted+"'");
            System.out.println("Usunieto Adres Trenera");
            initialize();
        }

        public void editTrener(ActionEvent event) throws IOException {
            TrenerModel selected = table.getSelectionModel().getSelectedItem();
            pesel=selected.getPesel();
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/edytujTrenera.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.show();
        }



        //szukanie zawodnika po imieniu nazwisku i peselu
        public void searchTrener() throws SQLException {
            System.out.println("Szukam");
            oblistFiltered.clear();
            search=searchTXT.getText();
            if(!search.isEmpty()){
                for(TrenerModel trener : oblist){
                    System.out.println(trener.getImie());
                    if(trener.getPesel().equals(search)){
                        oblistFiltered.add(trener);
                    }else if(trener.getImie().toLowerCase().equals(search.toLowerCase())){
                        oblistFiltered.add(trener);
                    }else if(trener.getNazwisko().toLowerCase().equals(search.toLowerCase())) {
                        oblistFiltered.add(trener);
                    }
                }
                if(!oblistFiltered.isEmpty()){
                    table.getItems().clear();
                    table.setItems(oblistFiltered);
                }
            } else {
                initialize();
            }

        }


    }

