package sample;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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



}
