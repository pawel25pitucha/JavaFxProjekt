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
import sample.models.Liga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LigaController {
    @FXML
    private TextField nazwaTXT;
    @FXML
    private Text errorMSG;
    @FXML
    private TableView<Liga> table;
    @FXML
    private TableColumn nazwaCol;

    private String dyscyplina;
    private ArrayList<String> lista =new ArrayList<String>();

    private ObservableList<Liga> oblist = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        table.getItems().clear();
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Liga");
        while(rs.next()){
            oblist.add(new Liga(rs.getString("Nazwa")));
        }
        nazwaCol.setCellValueFactory(new PropertyValueFactory<Liga,String>("nazwa"));
        table.setItems(oblist);
    }

    public void delete() throws SQLException {
        if(table.getSelectionModel().getSelectedItem()!=null){
            Liga selected=table.getSelectionModel().getSelectedItem();
            Statement stmt=ConnectionDB.con.createStatement();
            stmt.execute("DELETE Liga FROM Liga WHERE Nazwa="+"'"+selected.getNazwa()+"'");
            initialize();
        }
    }

    public void addDyscyplina(ActionEvent event) throws SQLException {
        dyscyplina=nazwaTXT.getText();

        if(checkDyscyplina()){
            System.out.println("hej");
            String sql="INSERT INTO Liga(Nazwa)VALUES(?)";
            try {
                PreparedStatement statement = ConnectionDB.con.prepareStatement(sql);
                statement.setString(1, dyscyplina);
                int rowsInserted=statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Nowa liga dodana");
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
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT Nazwa FROM Liga");
        while(rs.next()){
            lista.add(rs.getString("Nazwa"));
        }
        for(String nazwa : lista){
            if(nazwa.equals(dyscyplina)){
                errorMSG.setText("Taka liga już jest w bazie");
                return false;
            }
        }


        return true;
    }
}
