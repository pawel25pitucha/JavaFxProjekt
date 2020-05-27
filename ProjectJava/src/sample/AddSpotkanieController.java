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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.SedziaModel;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddSpotkanieController {

    @FXML
    private TextField druzyna1;
    @FXML
    private TextField druzyna2;
    @FXML
    private TextField punkty1;
    @FXML
    private TextField punkty2;
    @FXML
    private Text errorMSG;
    @FXML
    private TableView<SedziaModel> table;
    @FXML
    private TableColumn<SedziaModel, String> imieCol;
    @FXML
    private TableColumn<SedziaModel, String> nazwiskoCol;
    @FXML
    private TextField cena;
    @FXML
    private TextField data;
    ObservableList<SedziaModel> oblist = FXCollections.observableArrayList();
    ObservableList<SedziaModel> oblistFiltered = FXCollections.observableArrayList();
    private static String helper;
    private static String nazwa1;
    private static String nazwa2;
    private String dateFormat="yyyy-MM-dd";

    public static void setNazwa1(String nazwa1) {
        AddSpotkanieController.nazwa1 = nazwa1;
    }

    public static void setNazwa2(String nazwa2) {
        AddSpotkanieController.nazwa2 = nazwa2;
    }

    public static String getHelper() {
        return helper;
    }

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
        table.setItems(oblist);

        nazwa1=SzukajDruzynyController.getNazwa1();
        nazwa2=SzukajDruzynyController.getNazwa2();
        if(nazwa1!=null) druzyna1.setText(nazwa1);
        if(nazwa2!=null) druzyna2.setText(nazwa2);

    }

    public void szukajDruzyny1(ActionEvent event) throws IOException {
        helper="1";
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/SzukajDruzyny.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void szukajDruzyny2(ActionEvent event) throws IOException {
        helper="2";
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/SzukajDruzyny.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void getBack(ActionEvent event) throws IOException {
        nazwa1=null;
        nazwa2=null;
        SzukajDruzynyController.setNazwa1(null);
        SzukajDruzynyController.setNazwa2(null);
        SzukajDruzynyController.setNazwa1(null);
        SzukajDruzynyController.setNazwa2(null);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void addSpotkanie(ActionEvent event) throws SQLException {
        String punktyGospodarz=punkty1.getText();
        String punktyGosc=punkty2.getText();
        System.out.println("!");
        SedziaModel sedzia=table.getSelectionModel().getSelectedItem();
        String finalCena=cena.getText();
        String data1=data.getText();

        if(checkDane(nazwa1,nazwa2,punktyGosc,punktyGospodarz,finalCena,data1,sedzia)){
            String peselSedzia=table.getSelectionModel().getSelectedItem().getPesel();
            if(punktyGosc==null ) {
                String sql = "INSERT INTO Spotkanie(Gospodarz_Id,Gość_Id,Sędzia_Id,Cena,Data)VALUES (?, ?, ?,?,?)";
                try {
                    PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
                    statement.setString(1,getDruzynaId(nazwa1));
                    statement.setString(2, getDruzynaId(nazwa2));
                    statement.setString(3, getSedziaId(peselSedzia));
                    statement.setString(4, finalCena);
                    statement.setString(5, data1);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A new match was inserted successfully!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }else{
                String sql = "INSERT INTO Spotkanie(GospodarzID,GośćID,Sędzia_Id,Cena,GospodarzPunkty,GośćPunkty,Data)VALUES (?, ?, ?,?,?,?,?)";
                try {
                    PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
                    statement.setString(1,getDruzynaId(nazwa1));
                    statement.setString(2, getDruzynaId(nazwa2));
                    statement.setString(3, getSedziaId(peselSedzia));
                    statement.setString(4, finalCena);
                    statement.setString(5, punktyGospodarz);
                    statement.setString(6, punktyGosc);
                    statement.setString(7, data1);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A new match was inserted successfully!");
                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        stage.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public String getDruzynaId(String nazwa) throws SQLException {
        String id = null;
        ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Drużyna WHERE Nazwa=" + "'" + nazwa + "'");
        while (result.next()) {
            id = result.getString("Id");
        }
        return id;
    }
    private static String getSedziaId(String pesel) throws SQLException {
        String id = null;
        ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Sędzia WHERE Pesel=" + "'" + pesel + "'");
        while (result.next()) {
            id = result.getString("Id");
        }
        return id;
    }

        public boolean checkDane(String nazwa1,String nazwa2,String punkty1 , String punkty2,String cena,String data,SedziaModel sedzia) throws SQLException {
            boolean nazwa1Ok=false;
            boolean nazwa2Ok=false;
            ResultSet result=ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Drużyna");
            while(result.next()){
                if(nazwa1.equals(result.getString("Nazwa")) && nazwa2.equals(result.getString("Nazwa"))){
                    nazwa1Ok=true;
                    nazwa2Ok=true;
                }else if(nazwa2.equals(result.getString("Nazwa"))){
                    nazwa2Ok=true;
                }else if(nazwa1.equals(result.getString("Nazwa"))){
                    nazwa1Ok=true;
                }
            }


            if(punkty1!=null && punkty2!=null){
                if(punkty1.chars().allMatch(Character::isDigit)&&punkty2.chars().allMatch(Character::isDigit)) {
                    if (cena.chars().allMatch(Character::isDigit)) {
                        if (isValid(data)) {
                            if(nazwa1Ok && nazwa2Ok){
                                if(!nazwa1.equals(nazwa2)){
                                    if(sedzia!=null){
                                        return true;
                                    }else errorMSG.setText("Zaznacz sędziego!");
                                }else errorMSG.setText("Nazwy drużyn nie mogą być takie same!");
                            }else errorMSG.setText("Nie ma takich drużyn w bazie!");
                        }else errorMSG.setText("Data musi być w formacie : yyyy/MM/dd");
                    }else errorMSG.setText("Cena musi być cyfrą!");
                }else errorMSG.setText("Punkty muszą być cyframi!");
            }else{
                if (cena.chars().allMatch(Character::isDigit)) {
                    if (isValid(data)) {
                        if(nazwa1Ok && nazwa2Ok){
                            if(!nazwa1.equals(nazwa2)){
                                if(sedzia!=null){
                                    return true;
                                }else errorMSG.setText("Zaznacz sędziego!");
                            }else errorMSG.setText("Nazwy drużyn nie mogą być takie same!");
                        }else errorMSG.setText("Nie ma takich drużyn w bazie!");
                    }else errorMSG.setText("Data musi być w formacie : yyyy/MM/dd");
                }else errorMSG.setText("Cena musi być cyfrą!");
            }
            return false;
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}