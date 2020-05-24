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

public class EditSedziaController {
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
    private CheckBox kTXT;
    @FXML
    private CheckBox mTXT;
    @FXML
    private Text errorMSG;



    //Dane sedziego
    private static String pesel=SedziowieController.getPesel();
    private static String imie;
    private static String nazwisko;
    private static String data;
    private static String plec;


    @FXML
    public void initialize() throws SQLException {
        loadData();
    }

    public void loadData() throws SQLException {
        Statement stmt = ConnectionDB.con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Sędzia WHERE Pesel="+"'"+pesel+"'");
        while(result.next()){
            imie= result.getString("Imię");
            nazwisko=result.getString("Nazwisko");
            data=result.getString("Data_urodzenia");
            plec=result.getString("Płeć");
        }
        PeselTXT.setText(pesel);
        ImieTXT.setText(imie);
        NazwiskoTXT.setText(nazwisko);
        DataTXT.setText(data);
        if(plec=="k"){
            kTXT.setSelected(true);
            mTXT.setSelected(false);
        }else {
            kTXT.setSelected(false);
            mTXT.setSelected(true);
        }
    }
    public void addSedzia(ActionEvent event) throws IOException {
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
            changeView("viewsFXML/Sedzia/edytujAdresSedzia.fxml");
        }else{
            System.out.println("Bledne dane");
        }
    }
    public static void updateSedzia() throws SQLException {
        String id=getId();
        String sql1 = "UPDATE Sędzia SET Pesel="+"'"+pesel+"'"+"WHERE Id="+"'"+id+"'";
        String sql2 = "UPDATE Sędzia SET Imię="+"'"+imie+"'"+"WHERE Id="+"'"+id+"'";
        String sql3 = "UPDATE Sędzia SET Nazwisko="+"'"+nazwisko+"'"+"WHERE Id="+"'"+id+"'";
        String sql4 = "UPDATE Sędzia SET Data_urodzenia="+"'"+data+"'"+"WHERE Id="+"'"+id+"'";
        String sql5 = "UPDATE Sędzia SET Płeć="+"'"+plec+"'"+"WHERE Id="+"'"+id+"'";

        Statement stmt = ConnectionDB.con.createStatement();
        stmt.executeUpdate(sql1);
        stmt.executeUpdate(sql2);
        stmt.executeUpdate(sql3);
        stmt.executeUpdate(sql4);
        stmt.executeUpdate(sql5);

        System.out.println("zmienioneeeeeeee");

    }
    private static String getId() throws SQLException {
        String id = null;
        ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Sędzia WHERE Pesel=" + "'" + pesel + "'");
        while (result.next()) {
            id = result.getString("Id");
        }
        return id;
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
                            System.out.println("wszystko ok");
                            return true;
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
