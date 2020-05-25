package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.DruzynaModel;
import sample.models.TrenerModel;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDruzynaController {
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
    ObservableList<TrenerModel> oblist = FXCollections.observableArrayList();
    ObservableList<TrenerModel> oblistFiltered = FXCollections.observableArrayList();

    private String dyscyplina;
    private String liga;
    private String nazwa;
    private String DruzynaId;

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
        liga=ligaTXT.getValue();
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
        String ligaId = null;
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new Date();

        String sql = "INSERT INTO Drużyna(Dyscyplina_Id,Liga_Id,Nazwa,Data_zalozenia)VALUES (?,?,?,?)";
        String sql2="INSERT INTO Trener_has_Drużyna(Trener_Id,Drużyna_Id,DataWstapienia)VALUES(?,?,?)";
        try {


            PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
            statement.setString(1, DyscyplinaId);
            statement.setString(2, ligaId);
            statement.setString(3, nazwa);
            statement.setString(4, formatter.format(date));
            statement.executeUpdate();

            ResultSet id=ConnectionDB.con.createStatement().executeQuery("SELECT MAX(Id) as 'MAX' FROM Drużyna");
            while(id.next()){
                DruzynaId=id.getString("MAX");
            }
            System.out.println(DruzynaId);

            PreparedStatement statement2 = ConnectionDB.con.prepareStatement(sql2);
            statement2.setString(1, trenerId);
            statement2.setString(2, DruzynaId);
            statement2.setString(3, formatter.format(date));
            int rowsInserted=statement2.executeUpdate();




            if (rowsInserted > 0) {
                System.out.println("A new team was inserted successfully!");

            }
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


