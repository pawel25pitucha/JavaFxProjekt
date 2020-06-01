package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainController {

    @FXML
    private BarChart wykresPoziom;
    @FXML
    private CategoryAxis X;
    @FXML
    private CategoryAxis Y;
    @FXML
    private BarChart wykresDruzyn;
    @FXML
    private LineChart wykresCen;
    @FXML
    private ScatterChart wykresLider;

    ArrayList<String> id=new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        id.clear();
        updateIds();

        XYChart.Series set1= new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("Młodzik",getCount("Młodzik")));
        set1.getData().add(new XYChart.Data("Junior",getCount("Junior")));
        set1.getData().add(new XYChart.Data("Senior",getCount("Senior")));
        wykresPoziom.getData().addAll(set1);


        XYChart.Series set2= new XYChart.Series<>();
        set2.getData().add(new XYChart.Data("Piłka nożna",getCount2("Piłka nożna")));
        set2.getData().add(new XYChart.Data("Koszykówka",getCount2("Koszykówka")));
        set2.getData().add(new XYChart.Data("Piłka ręczna",getCount2("Piłka ręczna")));
        set2.getData().add(new XYChart.Data("Siatkówka",getCount2("Siatkówka")));
        wykresDruzyn.getData().addAll(set2);


        XYChart.Series set3= new XYChart.Series<>();
        set3.getData().add(new XYChart.Data("Styczeń",getCount3("1")));
        set3.getData().add(new XYChart.Data("Luty",getCount3("2")));
        set3.getData().add(new XYChart.Data("Marzec",getCount3("3")));
        set3.getData().add(new XYChart.Data("Kwiecień",getCount3("4")));
        set3.getData().add(new XYChart.Data("Maj",getCount3("5")));
        set3.getData().add(new XYChart.Data("Czerwiec",getCount3("6")));
        set3.getData().add(new XYChart.Data("Lipiec",getCount3("7")));
        set3.getData().add(new XYChart.Data("Sierpień",getCount3("8")));
        set3.getData().add(new XYChart.Data("Wrzesień",getCount3("9")));
        set3.getData().add(new XYChart.Data("Październik",getCount3("10")));
        set3.getData().add(new XYChart.Data("Listopad",getCount3("11")));
        set3.getData().add(new XYChart.Data("grudźień",getCount3("12")));


        XYChart.Series set4= new XYChart.Series<>();
        for(String ID : id){
            System.out.println(ID);
            set4.getData().add(new XYChart.Data(getDruzynaNazwa(ID),getCount4(ID)));
        }


        wykresCen.getData().clear();
        wykresCen.getData().addAll(set3);
        wykresLider.getData().clear();
        wykresLider.getData().addAll(set4);
    }

    public void odswiez() throws SQLException {
        wykresPoziom.getData().clear();
        wykresDruzyn.getData().clear();
        initialize();
    }
    private String getDruzynaNazwa(String Liga_Id) throws SQLException {
        String nazwa = null;
        ResultSet ligaSet= ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM drużyna WHERE Id="+"'"+Liga_Id+"'");
        while(ligaSet.next()){
            nazwa=ligaSet.getString("Nazwa");
        }
        return nazwa;
    }

    public int getCount(String poziom) throws SQLException {
        int ilosc = 0;
        Statement stmt = ConnectionDB.con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM Zawodnik WHERE Poziom="+"'"+poziom+"'");
        if (result.next()) { // just in case
           ilosc = result.getInt(1); // note that indexes are one-based
        }
        return ilosc;
    }
    public int getCount2(String sport) throws SQLException {
        int ilosc = 0;
        Statement stmt = ConnectionDB.con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM Drużyna INNER JOIN Dyscyplina ON Drużyna.Dyscyplina_Id=Dyscyplina.Id WHERE Dyscyplina.Nazwa="+"'"+sport+"'");
        if (result.next()) { // just in case
            ilosc = result.getInt(1); // note that indexes are one-based
        }
        return ilosc;
    }
    public int getCount3(String miesiac) throws SQLException {
        int ilosc = 0;
        Statement stmt = ConnectionDB.con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT AVG(Cena) FROM Spotkanie WHERE MONTH(Data)="+"'"+miesiac+"'");
        if (result.next()) { // just in case
            ilosc = result.getInt(1); // note that indexes are one-based
        }
        return ilosc;
    }

    public void updateIds() throws SQLException {
        Statement stmt = ConnectionDB.con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Drużyna");
        while(result.next()) { // just in case
            id.add(result.getString("Id"));

        }
    }

    public int getCount4(String Id) throws SQLException {
        int ilosc = 0,ilosc2=0;
            Statement stmt = ConnectionDB.con.createStatement();
            ResultSet result2 = stmt.executeQuery("SELECT SUM(GospodarzPunkty) FROM Spotkanie WHERE GospodarzID="+"'"+Id+"'");
            if (result2.next()) { // just in case
                ilosc=result2.getInt(1);
            }
            ResultSet result3 = stmt.executeQuery("SELECT SUM(GośćPunkty) FROM Spotkanie WHERE GośćID="+"'"+Id+"'");
            if (result3.next()) { // just in case
                ilosc2=result3.getInt(1);
            }

        return ilosc+ilosc2;
    }



    public void changeViewZawodnik(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Zawodnik/Zawodnicy.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void changeViewTrener(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Trener/Trenerzy.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void changeViewDyscyplina(ActionEvent event) throws IOException {

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Dyscyplina/DodajDyscypline.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void changeViewLiga(ActionEvent event) throws IOException {

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Dyscyplina/Liga.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void usunEdytujAdmin(ActionEvent event) throws IOException {

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Admin/editDeleteAdmin.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void changeViewSpotkanie(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Spotkanie/Spotkania.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void changeViewSedzia(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Sedzia/Sedziowie.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void changeViewDruzyna(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Druzyna/Druzyna.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }

    public void addNewAdmin() throws IOException {
        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Admin/newAdmin.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }
    public void wyloguj(ActionEvent event) throws SQLException, IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        ConnectionDB.con.createStatement().executeUpdate("UPDATE Uzytkownik SET Data_wylogowania="+"'"+formatter.format(date)+"'"+" WHERE Login="+"'"+ConnectionDB.getLogin()+"'");
        ConnectionDB.con.createStatement().executeUpdate("UPDATE Uzytkownik SET Godzina_wylogowania="+"'"+java.time.LocalTime.now()+"'"+" WHERE Login="+"'"+ConnectionDB.getLogin()+"'");

        ConnectionDB.con.close();

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/login.fxml"));
        Scene scene2=new Scene(view2);
        Stage window=new Stage();
        window.setScene(scene2);
        window.setResizable(false);
        window.show();
    }

}
