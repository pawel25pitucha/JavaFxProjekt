package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.models.Dyscyplina;
import sample.models.UserModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddDyscyplinaController {
    @FXML
    private TextField nazwaTXT;
    @FXML
    private Text errorMSG;
    @FXML
    private TableView<Dyscyplina> table;
    @FXML
    private TableColumn nazwaCol;

    private String dyscyplina;
    private ArrayList<String> lista =new ArrayList<String>();

    private ObservableList<Dyscyplina> oblist = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Dyscyplina");
        while(rs.next()){
            oblist.add(new Dyscyplina(rs.getString("Nazwa")));
        }
        nazwaCol.setCellValueFactory(new PropertyValueFactory<Dyscyplina,String>("nazwa"));
        table.setItems(oblist);
    }

    public void delete() throws SQLException {
        if(table.getSelectionModel().getSelectedItem()!=null){
            Dyscyplina selected=table.getSelectionModel().getSelectedItem();
            Statement stmt=ConnectionDB.con.createStatement();
            stmt.execute("DELETE Dyscyplina FROM Dyscyplina WHERE Nazwa="+"'"+selected.getNazwa()+"'");
            initialize();
        }
    }

    public void addDyscyplina(ActionEvent event) throws SQLException {
        dyscyplina=nazwaTXT.getText();

        if(checkDyscyplina()){
            String sql="INSERT INTO Dyscyplina(Nazwa)VALUES(?)";
            try {
                PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
                statement.setString(1, dyscyplina);
                int rowsInserted=statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Nowa dyscyplina dodana");
                    Node source = (Node) event.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean checkDyscyplina() throws SQLException {
        if(dyscyplina.isEmpty()){
            errorMSG.setText("Podaj nazwę");
            return false;
        }
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Dyscyplina");
        while(rs.next()){
            lista.add(rs.getString("Nazwa"));
        }
        for(String nazwa : lista){
            if(nazwa.equals(dyscyplina)){
                errorMSG.setText("Taka dyscyplina już jest w bazie");
                return false;
            }
        }

        if(dyscyplina.chars().allMatch(Character::isLetter)){
            return true;
        }else{
            errorMSG.setText("Dyscyplina nie może zawierać cyfr");
        }

        return false;
    }

}
