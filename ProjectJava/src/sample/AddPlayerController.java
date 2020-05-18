package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;


public class AddPlayerController {
    @FXML
    private TextField PeselTXT;
    @FXML
    private TextField ImieTXT;
    @FXML
    private TextField NazwiskoTXT;
    @FXML
    private TextField DataTXT;
    @FXML
    private TextField PlecTXT;
    @FXML
    private TextField miejscowoscTXT;
    @FXML
    private TextField ulicaTXT;
    @FXML
    private TextField nrDomuTXT;
    @FXML
    private TextField kodTXT;

    static String pesel;
    static String imie;
    static String nazwisko;
    static String data;
    static String plec;






    public void addPlayer(ActionEvent event) throws SQLException, IOException {
        //zapisanie danych
        pesel=PeselTXT.getText();
        imie=ImieTXT.getText();
        nazwisko=NazwiskoTXT.getText();
        data=DataTXT.getText();
        plec=PlecTXT.getText();
        //zamkniecie okna
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

       //zmiana okna
        changeView("DodajAdres.fxml",event);
    }

    @FXML
    public void changeView(String nazwa,ActionEvent event) throws IOException {
        System.out.println("zmieniam scene");
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource(nazwa));
        Parent root1=fxmlLoader.load();
        Stage stage=new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }


    public void select() throws SQLException {

        String sql = "SELECT Imię FROM Zawodnik";
        String dbURL = "jdbc:sqlserver://localhost:52623;";
        String username = "admin";
        String password = "password";

        try {

            Connection conn = DriverManager.getConnection(dbURL, username, password);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()){
                String name = result.getString(1);
                System.out.println(name);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void insertZawodnik(int idAdres){


        System.out.println(pesel);
        String sql = "INSERT INTO Zawodnik(Adres_id,Pesel,Imię,Nazwisko,Data_urodzenia,Płeć)VALUES (?, ?, ?, ?,?,?)";

        String dbURL = "jdbc:sqlserver://localhost:52623;";
        String username = "admin";
        String password = "password";

        try {
            Connection conn = DriverManager.getConnection(dbURL, username, password);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, String.valueOf(idAdres));
            statement.setString(2, pesel);
            statement.setString(3, imie);
            statement.setString(4, nazwisko);
            statement.setString(5, data);
            statement.setString(6, plec);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    public void insertAdres(ActionEvent event){
        String miejscowosc=miejscowoscTXT.getText();
        String ulica=ulicaTXT.getText();
        String nr=nrDomuTXT.getText();
        String kod=kodTXT.getText();

        String sql = "INSERT INTO Adres(Miejscowość,Ulica,Nr_domu,Kod_pocztowy)VALUES (?, ?, ?, ?)";

        String dbURL = "jdbc:sqlserver://localhost:52623;";
        String username = "admin";
        String password = "password";
        int id = 0;
        try {
            Connection conn = DriverManager.getConnection(dbURL, username, password);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, miejscowosc);
            statement.setString(2, ulica);
            statement.setString(3, nr);
            statement.setString(4, kod);


            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new adress was inserted successfully!");
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery("SELECT MAX(Id) FROM Adres");
                System.out.println(result);
                if (result.next()) { // just in case
                    id= result.getInt(1); // note that indexes are one-based
                }
                insertZawodnik(id);

                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


}
