package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddTrenerController {
    @FXML
    private TextField PeselTXT;
    @FXML
    private TextField ImieTXT;
    @FXML
    private TextField NazwiskoTXT;
    @FXML
    private TextField DataTXT;
    @FXML
    private CheckBox kTXT;
    @FXML
    private CheckBox mTXT;
    @FXML
    private Text errorMSG;

    @FXML
    private TextField miejscowoscTXT;
    @FXML
    private TextField ulicaTXT;
    @FXML
    private TextField nrDomuTXT;
    @FXML
    private TextField kodTXT;
    @FXML
    private Text errorMSG2;

    //Dane Trenera
    private static String pesel;
    private static String imie;
    private static String nazwisko;
    private static String data;
    private static String plec;


    //Dane Adres
    private static String miejscowosc;
    private static String ulica;
    private static String nr;
    private static String kod;
    private String dateFormat="yyyy-MM-dd";

///gettery

    public static String getImie(){
        return imie;
    }
    public static String getNazwisko(){
        return nazwisko;
    }
    public static String getMiejscowosc(){
        return miejscowosc;
    }
    public static String getUlica() {
        return ulica;
    }
    public static String getNr() {
        return nr;
    }
    public static String getKod() {
        return kod;
    }

//Funkcja posredniczaca dodajaca zawodnika

    public void addTrener(ActionEvent event) throws IOException {
        //zapisanie danych
        pesel=PeselTXT.getText();
        imie=ImieTXT.getText();
        nazwisko=NazwiskoTXT.getText();
        data=DataTXT.getText();


        if(checkDaneOsobowe()){
            //zamkniecie okna
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

            //zmiana okna
            changeView("viewsFXML/DodajAdresTrener.fxml");
        }else{
            System.out.println("Bledne dane");
        }
    }

    //funkcja dodajaca zawdonika

    public void insertTrener(int idAdres){
        String sql = "INSERT INTO Trener(Adres_id,Pesel,Imię,Nazwisko,Data_urodzenia,Płeć)VALUES (?, ?, ?, ?,?,?)";
        try {
            PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
            statement.setString(1, String.valueOf(idAdres));
            statement.setString(2, pesel);
            statement.setString(3, imie);
            statement.setString(4, nazwisko);
            statement.setString(5, data);
            statement.setString(6, plec);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
                changeView("viewsFXML/sukcesAdd.fxml");
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
    //Funkcja dodajaca adres powiazany z zawodnikiem

    public void insertAdres(ActionEvent event){
        miejscowosc=miejscowoscTXT.getText();
        ulica=ulicaTXT.getText();
        nr=nrDomuTXT.getText();
        kod=kodTXT.getText();

        String sql = "INSERT INTO Adres(Miejscowość,Ulica,Nr_domu,Kod_pocztowy)VALUES (?, ?, ?, ?)";

        int id = 0;
        if(checkDaneAdres()){
            try {

                PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
                statement.setString(1, miejscowosc);
                statement.setString(2, ulica);
                statement.setString(3, nr);
                statement.setString(4, kod);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new adress was inserted successfully!");
                    Statement stmt = ConnectionDB.con.createStatement();
                    ResultSet result = stmt.executeQuery("SELECT MAX(Id) FROM Adres");
                    System.out.println(result);
                    if (result.next()) { // just in case
                        id= result.getInt(1); // note that indexes are one-based
                    }
                    Node source = (Node) event.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                    insertTrener(id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else System.out.println("bledne dane adresu");
    }

    ///--------------------Funkcje sprawdzajace wprowadzone dane-------------------------///

    public boolean checkDaneOsobowe() {
        if (kTXT.isSelected() && mTXT.isSelected() == false) {
            plec = "k";
        } else if (mTXT.isSelected() && kTXT.isSelected() == false) {
            plec = "m";
        } else {
            errorMSG.setText("Złe dane dotyczące płci!");
            return false;
        }
        if (pesel.length() == 11 && pesel.chars().allMatch(Character::isDigit)) {
            System.out.println("pesel ok");
            if (imie.length() > 0 && imie.chars().allMatch(Character::isLetter)) {
                System.out.println("imie ok");
                if (nazwisko.length() > 0 && nazwisko.chars().allMatch(Character::isLetter)) {
                    System.out.println("nazwisko ok");
                    if (isValid(data)) {
                        System.out.println("data ok");
                        return true;
                    } else errorMSG.setText("Niepoprawny format daty!");
                } else errorMSG.setText("Nazwisko nie może zawierać cyfr ");
            } else errorMSG.setText("Imię nie może zawierać cyfr ");
        } else errorMSG.setText("Niepoprawny pesel!");
        return false;
    }
    private boolean checkDaneAdres(){
        if(miejscowosc.length()>0 && miejscowosc.chars().allMatch(Character::isLetter)){
            if(ulica.length()>0 && ulica.chars().allMatch(Character::isLetter)){
                if(nr.length()>0 && nr.chars().allMatch(Character::isDigit)){
                    if(kod.length()==6){
                        return true;
                    }else errorMSG2.setText("Niepoprawny kod pocztowy!");
                }else errorMSG2.setText("Numer domu nie moze być pusty!");
            }else errorMSG2.setText("Niepoprawnie wprowadzona nazwa ulicy!");
        }else errorMSG2.setText("Niepoprawnie wprowadzona nazwa miejscowości!");
        return false;
    }

    //czy podana data jest zgodna z formatem bazy danych

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

    //funkcja zmieniajaca okno

    @FXML
    public void changeView(String nazwa) throws IOException {
        System.out.println("zmieniam scene");
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource(nazwa));
        Parent root1=fxmlLoader.load();
        Stage stage=new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }
}
