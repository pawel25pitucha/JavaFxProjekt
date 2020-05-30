package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditAdresSedziaController {
    //Dane Adres
    private static String miejscowosc;
    private static String ulica;
    private static String nr;
    private static String kod;
    private static String pesel = SedziowieController.getPesel();
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


    @FXML
    public void initialize() throws SQLException {
        loadData();
    }

    public void loadData() throws SQLException {
        Statement stmt = ConnectionDB.con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Adres INNER JOIN Sędzia ON Adres.Id=Sędzia.Adres_Id WHERE Pesel=" + "'" + pesel + "'");
        while (result.next()) {
            miejscowosc = result.getString("Miejscowość");
            ulica = result.getString("Ulica");
            nr = result.getString("Nr_domu");
            kod = result.getString("Kod_pocztowy");
        }
        miejscowoscTXT.setText(miejscowosc);
        ulicaTXT.setText(ulica);
        nrDomuTXT.setText(nr);
        kodTXT.setText(kod);
    }

    private String getId() throws SQLException {
        String id = null;
        ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Adres_Id FROM Sędzia WHERE Pesel=" + "'" + pesel + "'");
        while (result.next()) {
            id = result.getString("Adres_Id");
        }
        return id;
    }

    public void updateAdres(ActionEvent event) throws SQLException {
        miejscowosc = miejscowoscTXT.getText();
        ulica = ulicaTXT.getText();
        nr = nrDomuTXT.getText();
        kod = kodTXT.getText();
        String id=getId();

        String sql1 = "UPDATE Adres SET Miejscowość= ? WHERE Id=?  ";
        String sql2 = "UPDATE Adres SET Ulica=? WHERE Id=?";
        String sql3 = "UPDATE Adres SET Nr_domu=? WHERE Id=?";
        String sql4 = "UPDATE Adres SET Kod_pocztowy=? WHERE Id=?";

        if (checkDaneAdres()) {
            PreparedStatement stmt = ConnectionDB.con.prepareStatement(sql1);
            PreparedStatement stmt2 = ConnectionDB.con.prepareStatement(sql2);
            PreparedStatement stmt3 = ConnectionDB.con.prepareStatement(sql3);
            PreparedStatement stmt4 = ConnectionDB.con.prepareStatement(sql4);
            stmt.setString(1,miejscowosc);
            stmt.setString(2,id);
            stmt2.setString(1,ulica);
            stmt2.setString(2,id);
            stmt3.setString(1,nr);
            stmt3.setString(2,id);
            stmt4.setString(1,kod);
            stmt4.setString(2,id);

            stmt.executeUpdate();
            stmt2.executeUpdate();
            stmt3.executeUpdate();
            stmt4.executeUpdate();

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            EditSedziaController.updateSedzia();

        }else System.out.println("bledne dane adresu");
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
}
