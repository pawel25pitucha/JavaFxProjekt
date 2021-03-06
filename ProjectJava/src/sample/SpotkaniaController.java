package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.SedziaModel;
import sample.models.SpotkanieModel;
import sample.models.TrenerModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class SpotkaniaController {
    @FXML
    private TableView<SpotkanieModel> table;
    @FXML
    private TableColumn gospodarzCol;
    @FXML
    private TableColumn goscCol;
    @FXML
    private TableColumn dataCol;
    @FXML
    private Text nazwa1TXT;
    @FXML
    private Text nazwa2TXT;
    @FXML
    private Text dataTXT;
    @FXML
    private Text cenaTXT;
    @FXML
    private Text sedziaTXT;
    @FXML
    private Text dataInfo;
    @FXML
    private Text cenaInfo;
    @FXML
    private Text sedziaInfo;
    @FXML
    private Text vs;
    @FXML
    private TextField cena1TXT;
    @FXML
    private TextField cena2TXT;
    @FXML
    private TextField searchTXT;
    @FXML
    private DatePicker data1TXT;
    @FXML
    private DatePicker data2TXT;
    private String dateFormat="yyyy-MM-dd";
    public static SpotkanieModel selected;
    private String search;

    private ObservableList<SpotkanieModel> oblist = FXCollections.observableArrayList();
    private ObservableList<SpotkanieModel> oblistFiltered = FXCollections.observableArrayList();
    public void initialize() throws SQLException {
        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Spotkanie");
        while(rs.next()){
            oblist.add(new SpotkanieModel(rs.getString("GospodarzID"),rs.getString("GośćID"),
                    rs.getString("Sędzia_Id"),rs.getString("GospodarzPunkty"),rs.getString("GośćPunkty"),rs.getString("Cena"),rs.getString("Data"),getNazwaGospodarz(rs.getString("GospodarzID")),getNazwaGosc(rs.getString("GośćID"))));
        }


        gospodarzCol.setCellValueFactory(new PropertyValueFactory<SpotkanieModel,String>("nazwaGospodarz"));
        goscCol.setCellValueFactory(new PropertyValueFactory<SpotkanieModel,String>("nazwaGosc"));
        dataCol.setCellValueFactory(new PropertyValueFactory<SpotkanieModel,String>("data"));
        table.setItems(oblist);
    }
    public String getNazwaGospodarz(String gospodarzId) throws SQLException {
        String nazwa = null;
        ResultSet rs= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Drużyna WHERE Id="+"'"+gospodarzId+"'");
        while(rs.next()){
            nazwa=rs.getString("Nazwa");
        }
        return nazwa;
    }
    public String getNazwaGosc(String goscId) throws SQLException {
        String nazwa = null;
        ResultSet rs= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Drużyna WHERE Id="+"'"+goscId+"'");
        while(rs.next()){
            nazwa=rs.getString("Nazwa");
        }
        return nazwa;
    }

    public void dodajSpotkanie() throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/DodajSpotkanie.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void edytujSpotkanie() throws IOException {
        selected=table.getSelectionModel().getSelectedItem();
        if(selected!=null){
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/EdytujSpotkanie.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.setResizable(false);
            window.show();
        }
    }
    public void getBack(ActionEvent event) throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Main.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void refresh() throws SQLException {
        initialize();
    }
    public void deleteSpotkanie(ActionEvent event) throws SQLException {
        SpotkanieModel deleted = table.getSelectionModel().getSelectedItem();
        if(deleted!=null){
            String id=getSpotkanieId(deleted.getGospodarzId(),deleted.getGoscId());
            Statement stmt = ConnectionDB.con.createStatement();
            stmt.execute("DELETE Spotkanie FROM Spotkanie WHERE Id="+"'"+id+"'");
            System.out.println("Usunieto Spotkanie :(");
            initialize();
        }
    }
    public String getSpotkanieId(String gospodarzID,String goscID) throws SQLException {
        String id = null;
        ResultSet rs= ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Spotkanie WHERE (GospodarzID="+"'"+gospodarzID+"'"+" and GośćId="+"'"+goscID+"')");
        while(rs.next()){
            id=rs.getString("Id");
        }
        return id;
    }
    public String getSedziaImie(String id) throws SQLException {
        String imie = null;
        ResultSet rs= ConnectionDB.con.createStatement().executeQuery("SELECT Imię FROM Sędzia WHERE Id="+"'"+id+"'");
        while(rs.next()){
            imie=rs.getString("Imię");
        }
        return imie;
    }
    public String getSedziaNazwisko(String id) throws SQLException {
        String nazwisko = null;
        ResultSet rs= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwisko FROM Sędzia WHERE Id="+"'"+id+"'");
        while(rs.next()){
            nazwisko=rs.getString("Nazwisko");
        }
        return nazwisko;
    }
   public void szczegoly() throws SQLException {

        vs.setText("VS");
        dataInfo.setText("Data:");
        cenaInfo.setText("Cena wejściówki:");
        sedziaInfo.setText("Sedzia:");
        nazwa1TXT.setText(table.getSelectionModel().getSelectedItem().getNazwaGospodarz());
        nazwa2TXT.setText(table.getSelectionModel().getSelectedItem().getNazwaGosc());
        dataTXT.setText(table.getSelectionModel().getSelectedItem().getData());
        if(table.getSelectionModel().getSelectedItem().getCena()!=null){
            cenaTXT.setText(table.getSelectionModel().getSelectedItem().getCena()+"zł");
        }
        String imie=getSedziaImie(table.getSelectionModel().getSelectedItem().getSedziaId());
        String nazwisko=getSedziaNazwisko(table.getSelectionModel().getSelectedItem().getSedziaId());
        sedziaTXT.setText(imie+"  "+nazwisko);
    }

    public void searchSpotkanie() throws SQLException {
        System.out.println("Szukam");
        oblistFiltered.clear();
        search=searchTXT.getText();
        if(!search.isEmpty()){
            for(SpotkanieModel player : oblist){
                if(player.getNazwaGospodarz().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getNazwaGosc().toLowerCase().matches(search.toLowerCase()+"[^0-9]*")){
                    oblistFiltered.add(player);
                }else if(player.getData().toLowerCase().matches(search.toLowerCase()+".*")){
                    oblistFiltered.add(player);
                }
            }
            if(!oblistFiltered.isEmpty()){
                //table.getItems().clear();
                table.setItems(oblistFiltered);;
            }
        } else {
            initialize();
        }
    }
    public void filtruj() throws ParseException {
        DateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd");

        int cena1 = 0,cena2 = 0;
        Date data1 = null,data2= null;

        if(cena1TXT.getText()!=null && !(cena1TXT.getText().isEmpty())){
            cena1=Integer.parseInt(cena1TXT.getText());
        }
        if(cena2TXT.getText()!=null && !(cena2TXT.getText().isEmpty())){
            cena2=Integer.parseInt(cena2TXT.getText());
        }
        if(data1TXT.getValue()!=null){
            data1=sdf.parse(data1TXT.getValue().toString());
        }
        if(data2TXT.getValue()!=null){
            data2=sdf.parse(data2TXT.getValue().toString());
        }

        if(oblistFiltered.size()==0){
            if(data1!=null && data2!=null){
                for(SpotkanieModel spotkanie : oblist){
                    Date data=sdf.parse(spotkanie.getData());
                    if(data.before(data2) && data.after(data1)){
                        oblistFiltered.add(spotkanie);
                    }
                }
            }else if(data1==null && data2!=null){
                for(SpotkanieModel spotkanie : oblist){
                    Date data=sdf.parse(spotkanie.getData());
                    if(data.before(data2)){
                        oblistFiltered.add(spotkanie);
                    }
                }
            }else if(data2==null && data1!=null){
                System.out.println("hejhejadmin");
                for(SpotkanieModel spotkanie : oblist){
                    Date data=sdf.parse(spotkanie.getData());
                    if(data.after(data1)){
                        oblistFiltered.add(spotkanie);
                    }
                }
            }else{
                for(SpotkanieModel spotkanie : oblist){
                        oblistFiltered.add(spotkanie);
                }
            }
        }else{
            if(data1!=null && data2!=null){
                for(SpotkanieModel spotkanie : oblist){
                    Date data=sdf.parse(spotkanie.getData());
                    if(data.after(data2) && data.before(data1)){
                        oblistFiltered.remove(spotkanie);
                    }
                }
            }else if(data1==null && data2!=null){
                for(SpotkanieModel spotkanie : oblist){
                    Date data=sdf.parse(spotkanie.getData());
                    if(data.after(data2)){
                        oblistFiltered.remove(spotkanie);
                    }
                }
            }else if(data2==null && data1!=null){
                for(SpotkanieModel spotkanie : oblist){
                    Date data=sdf.parse(spotkanie.getData());
                    if(data.before(data1)){
                        oblistFiltered.remove(spotkanie);
                    }
                }
            }
        }

        if(oblistFiltered.size()==0){
            if(cena1==0 && cena2==0){
                for(SpotkanieModel spotkanie : oblist){
                        oblistFiltered.add(spotkanie);
                }
            }else if(cena1==0){
                for(SpotkanieModel spotkanie : oblist){
                    if(Integer.parseInt(spotkanie.getCena())<=cena2){
                        oblistFiltered.add(spotkanie);
                    }
                }
            }else if(cena2==0){
                for(SpotkanieModel spotkanie : oblist){
                    if(Integer.parseInt(spotkanie.getCena())>=cena1){
                        oblistFiltered.add(spotkanie);
                    }
                }
            }else{
                for(SpotkanieModel spotkanie : oblist){
                    if(Integer.parseInt(spotkanie.getCena())>=cena1 && Integer.parseInt(spotkanie.getCena())<=cena2){
                        oblistFiltered.add(spotkanie);
                    }
                }
            }
        }else{
            if(cena1==0 && cena2==0){

            }else if(cena1==0){
                for(SpotkanieModel spotkanie : oblist){
                    if(Integer.parseInt(spotkanie.getCena())>cena2){
                        oblistFiltered.remove(spotkanie);
                    }
                }
            }else if(cena2==0){
                for(SpotkanieModel spotkanie : oblist){
                    if(Integer.parseInt(spotkanie.getCena())<cena1){
                        oblistFiltered.remove(spotkanie);
                    }
                }
            }else{
                for(SpotkanieModel spotkanie : oblist){
                    if(Integer.parseInt(spotkanie.getCena())<cena1 || Integer.parseInt(spotkanie.getCena())>cena2){
                        oblistFiltered.remove(spotkanie);
                    }
                }
            }

        }


        table.setItems(oblistFiltered);
    }



    public void clearFiltr(){
        cena1TXT.setText(null);
        cena2TXT.setText(null);
        data1TXT.setValue(null);
        data2TXT.setValue(null);
        table.getItems().clear();
        table.setItems(oblist);
        oblistFiltered.clear();
    }


}
