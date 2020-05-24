package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainController {

    @FXML
    private BarChart wykresPoziom;

    @FXML
    private CategoryAxis X;

    @FXML
    private CategoryAxis Y;

    @FXML
    public void initialize() throws SQLException {
        XYChart.Series set1= new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("Młodzik",getCount("Młodzik")));
        set1.getData().add(new XYChart.Data("Junior",getCount("Junior")));
        set1.getData().add(new XYChart.Data("Senior",getCount("Senior")));
        wykresPoziom.getData().addAll(set1);
    }

    public void odswiez() throws SQLException {
        XYChart.Series set1= new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("Młodzik",getCount("Młodzik")));
        set1.getData().add(new XYChart.Data("Junior",getCount("Junior")));
        set1.getData().add(new XYChart.Data("Senior",getCount("Senior")));
        wykresPoziom.getData().clear();
        wykresPoziom.getData().addAll(set1);

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
