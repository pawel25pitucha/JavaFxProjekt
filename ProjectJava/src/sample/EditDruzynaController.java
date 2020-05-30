package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.TrenerModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditDruzynaController {

        @FXML
        private ComboBox<String> dyscyplinaTXT;
        @FXML
        private ComboBox<String> ligaTXT;
        @FXML
        private TextField nazwaTXT;
        @FXML
        private TextField searchTXT;
        @FXML
        private TableView<TrenerModel> table;
        @FXML
        private TableColumn<TrenerModel, String> imieCol;
        @FXML
        private TableColumn<TrenerModel, String> nazwiskoCol;
        @FXML
        private TableColumn<TrenerModel, String> peselCol;
        private ObservableList<TrenerModel> oblist = FXCollections.observableArrayList();
        private ObservableList<TrenerModel> oblistFiltered = FXCollections.observableArrayList();

        private String dyscyplina;
        private String liga;
        private String nazwa;
        private String oldNazwa;
        private String peselTrenera;
        private String trenerId;
    @FXML
    private Text errorMSG;



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
            loadData();
        }

        public void loadData() throws SQLException {
            oldNazwa=DruzynaController.getNazwa();
            dyscyplina=DruzynaController.getDyscyplina();
            liga=DruzynaController.getLiga();
            nazwaTXT.setText(oldNazwa);
            dyscyplinaTXT.setPromptText(dyscyplina);
            ligaTXT.setPromptText(liga);
        }
        public void saveDruzyna(ActionEvent event) throws SQLException {
            dyscyplina=dyscyplinaTXT.getValue();
            if(dyscyplina==null){
                dyscyplina=DruzynaController.getDyscyplina();
            }
            nazwa=nazwaTXT.getText();
                peselTrenera=table.getSelectionModel().getSelectedItem().getPesel();
                trenerId=getIdTrenera(peselTrenera);
                liga=ligaTXT.getValue();
                if(liga==null){
                    liga=DruzynaController.getLiga();
                }
                dodajDoBazy(event);
            }


        public String getIdTrenera(String pesel) throws SQLException {
            String id = null;
            ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Trener WHERE Pesel=" + "'" + pesel + "'");
            while (result.next()) {
                id = result.getString("Id");
            }
            return id;
        }

        private void dodajDoBazy(ActionEvent event){
            String DyscyplinaId;
            String ligaId ;
            System.out.println(dyscyplina);
            if(dyscyplina.equals("Piłka nożna")) {
                DyscyplinaId="1";
            }else if(dyscyplina.equals("Piłka ręczna")) {
                DyscyplinaId="2";
            }else if(dyscyplina.equals("Koszykówka")) {
                DyscyplinaId="3";
            }else {
                DyscyplinaId="4";
            }

            if(liga.equals("SuperLiga")) {
                ligaId="1";
            }else if(liga.equals("1 Liga")) {
                ligaId="2";
            }else if(liga.equals("2 Liga")) {
                ligaId="3";
            }else{
                ligaId="4";
            }

            System.out.println(DyscyplinaId + ligaId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date date = new Date();

            String sql1 = "UPDATE Drużyna SET Nazwa= ? WHERE Nazwa= ?";
            String sql2 = "UPDATE Drużyna SET Liga_Id= ? WHERE Nazwa= ?";
            String sql3 = "UPDATE Drużyna SET Dyscyplina_Id= ? WHERE Nazwa= ?";
            String sql4 = "UPDATE Drużyna SET Data_zalozenia= ? WHERE Nazwa= ?";
            try {
                PreparedStatement stmt = ConnectionDB.con.prepareStatement(sql1);
                PreparedStatement stmt2 = ConnectionDB.con.prepareStatement(sql2);
                PreparedStatement stmt3 = ConnectionDB.con.prepareStatement(sql3);
                PreparedStatement stmt4 = ConnectionDB.con.prepareStatement(sql4);
                stmt.setString(1,nazwa);
                stmt.setString(2,oldNazwa);
                stmt2.setString(1,ligaId);
                stmt2.setString(2,nazwa);
                stmt3.setString(1,DyscyplinaId);
                stmt3.setString(2,nazwa);
                stmt4.setString(1,formatter.format(date));
                stmt4.setString(2,nazwa);


                stmt.executeUpdate();
                stmt2.executeUpdate();
                stmt3.executeUpdate();
                stmt4.executeUpdate();


            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }

        public void search() throws SQLException {
            System.out.println("Szukam");
            oblistFiltered.clear();
            String search=searchTXT.getText();
            if(!search.isEmpty()){
                for(TrenerModel player : oblist){
                    if(player.getImie().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                        oblistFiltered.add(player);
                    }else if(player.getNazwisko().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                        oblistFiltered.add(player);
                    }else if(player.getPesel().matches(search.toLowerCase()+"[0-9]*")){
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

