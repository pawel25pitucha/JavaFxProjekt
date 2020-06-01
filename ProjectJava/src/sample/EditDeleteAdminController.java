package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.models.PlayerModel;
import sample.models.UserModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EditDeleteAdminController {

    @FXML
    private TableView<UserModel> table;
    @FXML
    private TableColumn loginCol;
    private static String login;

    public static String getLogin() {
        return login;
    }

    private ObservableList<UserModel> oblist = FXCollections.observableArrayList();
    public void initialize() throws SQLException {
        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Uzytkownik");
        while(rs.next()){
            oblist.add(new UserModel(rs.getString("Login"),rs.getString("Hasło")));
        }

        loginCol.setCellValueFactory(new PropertyValueFactory<PlayerModel,String>("login"));
        table.setItems(oblist);
    }

    public void delete() throws SQLException {

        if(table.getSelectionModel().getSelectedItem()!=null){
            UserModel selected=table.getSelectionModel().getSelectedItem();
            Statement stmt=ConnectionDB.con.createStatement();
            stmt.execute("DELETE Uzytkownik FROM Uzytkownik WHERE Login="+"'"+selected.getLogin()+"'");
            initialize();
        }
    }
    public void edit() throws IOException {
       UserModel selected = table.getSelectionModel().getSelectedItem();
        if(selected!=null){
            login=selected.getLogin();
            Parent view2 = FXMLLoader.load(getClass().getResource("viewsFXML/Admin/Edytuj.fxml"));
            Scene scene2=new Scene(view2);
            Stage window=new Stage();
            window.setScene(scene2);
            window.show();
        }
    }

}
