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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditPlayerController {
    private String dateFormat="yyyy-MM-dd";

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



        //Dane zawodnika
        private static String pesel=ZawodnicyController.getPesel();
        private static String imie;
        private static String nazwisko;
        private static String data;
        private static String plec;
        private static String poziom;

        @FXML
        public void initialize() throws SQLException {
            loadData();
        }

        public void loadData() throws SQLException {
            Statement stmt = ConnectionDB.con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM Zawodnik WHERE Pesel="+"'"+pesel+"'");
            while(result.next()){
                imie= result.getString("Imię");
                nazwisko=result.getString("Nazwisko");
                data=result.getString("Data_urodzenia");
                poziom=result.getString("Poziom");
                plec=result.getString("Płeć");
            }
            PeselTXT.setText(pesel);
            ImieTXT.setText(imie);
            NazwiskoTXT.setText(nazwisko);
            DataTXT.setText(data);
            poziomTXT.setText(poziom);
            if(plec=="k"){
                kTXT.setSelected(true);
                mTXT.setSelected(false);
            }else {
                kTXT.setSelected(false);
                mTXT.setSelected(true);
            }
        }
    public void updatePlayer(ActionEvent event) throws IOException {
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
            changeView("viewsFXML/edytujAdres.fxml");
        }else{
            System.out.println("Bledne dane");
        }
    }
    public static void updateZawodnik(String id) throws SQLException {
        String sql1 = "UPDATE Zawodnik SET Pesel="+"'"+pesel+"'"+"WHERE Id="+"'"+id+"'";
        String sql2 = "UPDATE Zawodnik SET Imię="+"'"+imie+"'"+"WHERE Id="+"'"+id+"'";
        String sql3 = "UPDATE Zawodnik SET Nazwisko="+"'"+nazwisko+"'"+"WHERE Id="+"'"+id+"'";
        String sql4 = "UPDATE Zawodnik SET Data_urodzenia="+"'"+data+"'"+"WHERE Id="+"'"+id+"'";
        String sql5 = "UPDATE Zawodnik SET Poziom="+"'"+poziom+"'"+"WHERE Id="+"'"+id+"'";
        String sql6 = "UPDATE Zawodnik SET Płeć="+"'"+plec+"'"+"WHERE Id="+"'"+id+"'";

            Statement stmt = ConnectionDB.con.createStatement();
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
            stmt.executeUpdate(sql5);
            stmt.executeUpdate(sql6);

    }

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
                        if(poziom.equals("Senior") || poziom.equals("Młodzik") || poziom.equals("Junior")){
                            System.out.println("wszystko ok");
                            return true;
                        }else errorMSG.setText("Błedne dane poziom!");
                    } else errorMSG.setText("Niepoprawny format daty!");
                } else errorMSG.setText("Nazwisko nie może zawierać cyfr ");
            } else errorMSG.setText("Imię nie może zawierać cyfr ");
        } else errorMSG.setText("Niepoprawny pesel!");
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
