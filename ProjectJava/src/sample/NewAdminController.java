package sample;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.ConnectionDB;
import sample.models.UserModel;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewAdminController {
    @FXML
    private TextField LoginTXT;
    @FXML
    private PasswordField password1TXT;
    @FXML
    private PasswordField password2TXT;
    @FXML
    private Text errorMSG;

    private String login;
    private String password1;
    private String password2;



    public void addNewAdmin() throws SQLException {
       if(checkLogin()){
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
           java.util.Date date = new Date();
           String sql= "INSERT INTO Uzytkownik(Login,Hasło,Data_utworzenia,Stworzył) VALUES (?,?,?,?)";
           PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
           statement.setString(1, login);
           statement.setString(2, password1);
           statement.setString(3, formatter.format(date));
           statement.setString(4, ConnectionDB.getLogin());
           int result=statement.executeUpdate();
           if(result>0)System.out.println("Dodano admina");
       }
    }

    private boolean checkLogin(){
        login=LoginTXT.getText();
        password1=password1TXT.getText();
        password2=password2TXT.getText();

        if(password1.equals(password2)){
            ArrayList<UserModel> users = ConnectionDB.getUsers();
            for(UserModel user : users){
                if(user.getLogin().equals(login)){
                    errorMSG.setText("Login jest zajęty!");
                    return false;
                }
            }
            return true;
        }else{
            errorMSG.setText("Hasła nie są identyczne");
            return false;
        }
    }
}
