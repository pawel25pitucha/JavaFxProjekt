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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.PlayerModel;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DruzynaDodajZawodnikowController {

    @FXML
    private TableView<PlayerModel> table;
    @FXML
    private TableView<PlayerModel> table2;
    @FXML
    private TableColumn imieCol;
    @FXML
    private TableColumn nazwiskoCol;
    @FXML
    private TableColumn poziomCol;
    @FXML
    private TableColumn imieCol2;
    @FXML
    private TableColumn nazwiskoCol2;
    @FXML
    private TableColumn poziomCol2;
    @FXML
    private Text druzynaNazwa;
    @FXML
    private Text statusMSG;

    private String nazwaDruzyny=DruzynaController.getNazwa();

    ObservableList<PlayerModel> zawdonicyDruzyna = FXCollections.observableArrayList();
    ObservableList<PlayerModel> zawdonicyDostepni = FXCollections.observableArrayList();
    ObservableList<PlayerModel> oblistFiltered = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        zawdonicyDostepni.clear();
        zawdonicyDruzyna.clear();
        table.getItems().clear();
        table2.getItems().clear();


        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Zawodnik_has_Drużyna");
        while(rs.next()){
            if(rs.getString("Drużyna_Id").equals(getDruzynaId(nazwaDruzyny))){
                ResultSet zawodnik=ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Zawodnik WHERE Id="+"'"+rs.getString("Zawodnik_Id")+"'");
                while(zawodnik.next()){
                    zawdonicyDruzyna.add(new PlayerModel(zawodnik.getString("Imię"),zawodnik.getString("Nazwisko"),
                            zawodnik.getString("Pesel"),zawodnik.getString("Poziom")));
                }
            }
        }


        ResultSet rs2 = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Zawodnik");
        boolean status = true;
        while(rs2.next()){
            if(zawdonicyDruzyna.isEmpty()){
                zawdonicyDostepni.add(new PlayerModel(rs2.getString("Imię"),rs2.getString("Nazwisko"),
                        rs2.getString("Pesel"),rs2.getString("Poziom")));
            }else{
                for(PlayerModel player : zawdonicyDruzyna){
                    if(player.getPesel().equals(rs2.getString("Pesel"))){
                        status=false;
                    }
                }
                if(status){
                    zawdonicyDostepni.add(new PlayerModel(rs2.getString("Imię"),rs2.getString("Nazwisko"),
                            rs2.getString("Pesel"),rs2.getString("Poziom")));
                }
                status=true;
            }
        }

        imieCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("imie"));
        nazwiskoCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("nazwisko"));
        poziomCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("poziom"));

        imieCol2.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("imie"));
        nazwiskoCol2.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("nazwisko"));
        poziomCol2.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("poziom"));


        table.setItems(zawdonicyDruzyna);
        table2.setItems(zawdonicyDostepni);
        druzynaNazwa.setText(nazwaDruzyny);

    }
    public String getDruzynaId(String nazwa) throws SQLException {
        String id = null;
        ResultSet druzynaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Drużyna WHERE Nazwa="+"'"+nazwa+"'");
        while(druzynaSet.next()){
            id=druzynaSet.getString("Id");
        }
        return id;
    }
    public String getPlayerId(String pesel) throws SQLException {
        String id = null;
        ResultSet druzynaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Zawodnik WHERE Pesel="+"'"+pesel+"'");
        while(druzynaSet.next()){
            id=druzynaSet.getString("Id");
        }
        return id;
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
    public void dodajGracza() throws SQLException {
        if(table2.getSelectionModel().getSelectedItem()!=null){
            PlayerModel gracz=table2.getSelectionModel().getSelectedItem();
            String idGracz=getPlayerId(gracz.getPesel());
            String idDruzyna=getDruzynaId(nazwaDruzyny);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date date = new Date();
            String sql = "INSERT INTO Zawodnik_has_Drużyna(Zawodnik_Id,Drużyna_Id,DataWstapienia)VALUES (?, ?,?)";
            try {
                PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
                statement.setString(1, idGracz);
                statement.setString(2, idDruzyna);
                statement.setString(3, formatter.format(date));
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    statusMSG.setText("Sukces!");
                    initialize();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void usunGracza() throws SQLException {
        PlayerModel deleted = table.getSelectionModel().getSelectedItem();
        if(deleted!=null){
            String peselDeleted=deleted.getPesel();
            String graczId=getPlayerId(peselDeleted);
            Statement stmt = ConnectionDB.con.createStatement();
            stmt.execute("DELETE Zawodnik_has_Drużyna FROM Zawodnik_has_Drużyna WHERE Zawodnik_Id="+"'"+graczId+"'");
            System.out.println("Usunieto Zawodnika :(");
            statusMSG.setText("Sukces!");
            initialize();
        }
    }
}
