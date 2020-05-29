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
import sample.ConnectionDB;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private TextField poziomTXT;
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

    //Dane zawodnika
    private static String pesel;
    private static String imie;
    private static String nazwisko;
    private static String data;
    private static String plec;
    private static String poziom;

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

    public void addPlayer(ActionEvent event) throws IOException, ParseException {
        //zapisanie danych
        pesel=PeselTXT.getText();
        imie=ImieTXT.getText();
        nazwisko=NazwiskoTXT.getText();
        data=DataTXT.getText();
        poziom=poziomTXT.getText();

        if(checkDaneOsobowe()){
            //zamkniecie okna
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

            //zmiana okna
            changeView("viewsFXML/Zawodnik/DodajAdres.fxml");
        }else{
            System.out.println("Bledne dane");
        }
    }

    //funkcja dodajaca zawdonika

    public void insertZawodnik(int idAdres){
        String sql = "INSERT INTO Zawodnik(Adres_id,Pesel,Imię,Nazwisko,Data_urodzenia,Płeć,Poziom) VALUES (?, ?, ?, ?,?,?,?)";
        try {
            PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
            statement.setString(1, String.valueOf(idAdres));
            statement.setString(2, pesel);
            statement.setString(3, imie);
            statement.setString(4, nazwisko);
            statement.setString(5, data);
            statement.setString(6, plec);
            statement.setString(7, poziom);

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
                    insertZawodnik(id);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else System.out.println("bledne dane adresu");
    }

    ///--------------------Funkcje sprawdzajace wprowadzone dane-------------------------///

    public boolean checkDaneOsobowe() throws ParseException {
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
                if (imie.length() > 0 && imie.chars().allMatch(Character::isLetter) && Character.isUpperCase(imie.charAt(0))) {
                    System.out.println("imie ok");
                    if (nazwisko.length() > 0 && nazwisko.chars().allMatch(Character::isLetter) && Character.isUpperCase(nazwisko.charAt(0))) {
                        System.out.println("nazwisko ok");
                        if (isValid(data)) {
                            System.out.println("data ok");
                            if(poziom.equals("Senior") || poziom.equals("Młodzik") || poziom.equals("Junior")){
                                System.out.println("wszystko ok");
                                return true;
                            }else errorMSG.setText("Błedne dane poziom!");
                        } else errorMSG.setText("Niepoprawny format daty!");
                    } else errorMSG.setText("Nazwisko nie może zawierać cyfr oraz musi zaczynać się wielką literą");
                } else errorMSG.setText("Imię nie może zawierać cyfr oraz musi zaczynać się wielką literą");
            } else errorMSG.setText("Niepoprawny pesel!");
            return false;
    }
    private boolean checkDaneAdres(){
        if(miejscowosc.length()>0 && miejscowosc.chars().allMatch(Character::isLetter) && Character.isUpperCase(miejscowosc.charAt(0))){
            if(ulica.length()>0 && ulica.chars().allMatch(Character::isLetter) && Character.isUpperCase(ulica.charAt(0))){
                if(nr.length()>0 && nr.chars().allMatch(Character::isDigit)){
                    if(kod.matches("[0-9]{2}-[0-9]{3}")){
                        return true;
                    }else errorMSG2.setText("Niepoprawny kod pocztowy!");
                }else errorMSG2.setText("Numer domu nie moze być pusty oraz może zawierać tylko cyfry!");
            }else errorMSG2.setText("Niepoprawnie wprowadzona nazwa ulicy!");
        }else errorMSG2.setText("Niepoprawnie wprowadzona nazwa miejscowości!");
        return false;
    }

    //czy podana data jest zgodna z formatem bazy danych

    public boolean isValid(String dateStr) throws ParseException {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        if(!compareDate(dateStr)) return false;
        return true;
    }
    public boolean compareDate(String data) throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat(this.dateFormat);
        java.util.Date d1 = sdformat.parse(data);
        java.util.Date date = new java.util.Date();
        String dt=sdformat.format(date);
        Date d2=sdformat.parse(dt);
        if(d1.compareTo(d2) < 0) {
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
