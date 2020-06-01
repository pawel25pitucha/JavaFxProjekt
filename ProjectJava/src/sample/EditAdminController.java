package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.UserModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditAdminController {
    @FXML
    private TextField LoginTXT;
    @FXML
    private PasswordField password1TXT;
    @FXML
    private PasswordField password2TXT;
    @FXML
    private Text errorMSG;


    private static ArrayList<UserModel> users = new ArrayList<>();

    private String login;
    private String password1;
    private String password2;

    public void initialize(){
        LoginTXT.setText(EditDeleteAdminController.getLogin());
    }

    public void addNewAdmin(ActionEvent event) throws SQLException {
       if(checkLogin()){
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
           Date date = new Date();
           String sql= "UPDATE Uzytkownik SET Login=? WHERE Login=?";
           String sql2= "UPDATE Uzytkownik SET Hasło=? WHERE Login=?";
           PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
           PreparedStatement statement2 = ConnectionDB.con.prepareStatement(sql2);
           String haslo=ConnectionDB.md5(password2);
           statement.setString(1, login);
           statement.setString(2, EditDeleteAdminController.getLogin());
           statement2.setString(1, haslo);
           statement2.setString(2, login);
           int result=statement.executeUpdate();
           statement2.executeUpdate();

           if(result>0)System.out.println("Dodano admina");
           Node source = (Node) event.getSource();
           Stage stage = (Stage) source.getScene().getWindow();
           stage.close();

       }
    }

    private boolean checkLogin() throws SQLException {
        login=LoginTXT.getText();
        password1=ConnectionDB.md5(password1TXT.getText());
        password2=password2TXT.getText();

        if(login.isEmpty()){
            errorMSG.setText("Podaj login");
            return false;
        }
        if(password1.isEmpty()){
            errorMSG.setText("Podaj haslo");
            return false;
        }
        if(password2.isEmpty()){
            errorMSG.setText("Podaj nowe haslo");
            return false;
        }

        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Uzytkownik");
        while (rs.next()) {
            users.add(new UserModel(rs.getString("Login"), rs.getString("Hasło")));
        }

        for(UserModel user : users){
            if(user.getLogin()==EditDeleteAdminController.getLogin()){
                if(user.getHaslo()!=password1){
                    errorMSG.setText("Złe stare hasło");
                    return false;
                }
            }
        }
        return true;


    }
}
