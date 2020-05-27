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
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditSpotkanieController {
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



    public static String getHelper() {
        return helper;
    }

    @FXML
    public void initialize() throws SQLException {
        loadData();
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
    public void loadData(){
        System.out.println(SpotkaniaController.selected.getCena());
        druzyna1.setText(SpotkaniaController.selected.getNazwaGospodarz());
        druzyna2.setText(SpotkaniaController.selected.getNazwaGosc());
        cena.setText(SpotkaniaController.selected.getCena());
        data.setText(SpotkaniaController.selected.getData());
        if(SpotkaniaController.selected.getGospodarzPunkty()!=null){
            punkty1.setText(SpotkaniaController.selected.getGospodarzPunkty());
            punkty2.setText(SpotkaniaController.selected.getGoscPunkty());
        }
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
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void addSpotkanie(ActionEvent event) throws SQLException {
        String punktyGospodarz = punkty1.getText();
        String punktyGosc = punkty2.getText();
        SedziaModel sedzia=table.getSelectionModel().getSelectedItem();
        String finalCena = cena.getText();
        String data1 = data.getText();
        String druzyna1TXT = druzyna1.getText();
        String druzyna2TXT = druzyna2.getText();
        String druzyna1ID = getDruzynaId(druzyna1TXT);
        System.out.println(druzyna1ID);
        String druzyna2ID = getDruzynaId(druzyna2TXT);
        String spotkanieID = getSpotkanieId(SpotkaniaController.selected.getGospodarzId(),SpotkaniaController.selected.getGoscId());
        System.out.println(spotkanieID);

        if (checkDane(druzyna1TXT,druzyna2TXT,punktyGosc, punktyGospodarz, finalCena, data1,sedzia)) {
            String peselSedzia = table.getSelectionModel().getSelectedItem().getPesel();
            String sedziaID = getSedziaId(peselSedzia);
            if (punktyGosc == null) {
                String sql1 = "UPDATE Spotkanie SET GospodarzID=" + "'" + druzyna1ID + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql2 = "UPDATE Spotkanie SET GośćID=" + "'" + druzyna2ID + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql5 = "UPDATE Spotkanie SET Sędzia_Id=" + "'" + sedziaID + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql6 = "UPDATE Spotkanie SET Cena=" + "'" + finalCena + "' WHERE Id=" + "'" + spotkanieID + "'";

                try {
                    Statement st = ConnectionDB.con.createStatement();
                    st.executeUpdate(sql1);
                    st.executeUpdate(sql2);
                    st.executeUpdate(sql5);
                    st.executeUpdate(sql6);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                String sql1 = "UPDATE Spotkanie SET GospodarzID=" + "'" + druzyna1ID + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql2 = "UPDATE Spotkanie SET GośćID=" + "'" + druzyna2ID + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql3 = "UPDATE Spotkanie SET GospodarzPunkty=" + "'" + punktyGospodarz + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql4 = "UPDATE Spotkanie SET GośćPunkty=" + "'" + punktyGosc + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql5 = "UPDATE Spotkanie SET Sędzia_Id=" + "'" + sedziaID + "' WHERE Id=" + "'" + spotkanieID + "'";
                String sql6 = "UPDATE Spotkanie SET Cena=" + "'" + finalCena + "' WHERE Id=" + "'" + spotkanieID + "'";

                try {
                    Statement st = ConnectionDB.con.createStatement();
                    st.executeUpdate(sql1);
                    st.executeUpdate(sql2);
                    st.executeUpdate(sql3);
                    st.executeUpdate(sql4);
                    st.executeUpdate(sql5);
                    st.executeUpdate(sql6);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

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
    public String getSpotkanieId(String gospodarzID,String goscID) throws SQLException {
        String id = null;
        ResultSet rs= ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Spotkanie WHERE (GospodarzID="+"'"+gospodarzID+"'"+" and GośćId="+"'"+goscID+"')");
        while(rs.next()){
            id=rs.getString("Id");
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
