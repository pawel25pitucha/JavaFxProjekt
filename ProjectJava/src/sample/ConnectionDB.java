package sample;


import sample.models.UserModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ConnectionDB {
    public static Connection con;
    private static ArrayList<UserModel> users=new ArrayList<>();
    private static ArrayList<String> datas =new ArrayList<String>(3);
    private static String Login;
    private static String haslo;

    public static String getLogin() {
        return Login;
    }

    public static String getHaslo() {
        return haslo;
    }

    public static ArrayList<UserModel> getUsers() {
        return users;
    }

    public static boolean ConnectionDB(String login, String password) throws ClassNotFoundException, SQLException {
        Login=login;
        haslo=password;
        String data1 = null;
        String data2 = null;

        //ladowanie danych z pliku
        try {
            File myObj = new File("dane.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
               datas.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String dbURL = datas.get(0);
        con=DriverManager.getConnection(dbURL, datas.get(1), datas.get(2));
        ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Uzytkownik");
        while(rs.next()){
            System.out.println(rs.getString("Login"));
            users.add(new UserModel(rs.getString("Login"),rs.getString("Hasło")));
        }
        for(UserModel user : users){
            if(user.getLogin().equals(login)){
                if(user.getHaslo().equals(password)){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = new Date();
                    con.createStatement().executeUpdate("UPDATE Uzytkownik SET Data_logowania="+"'"+formatter.format(date)+"'"+" WHERE Login="+"'"+login+"'");
                    con.createStatement().executeUpdate("UPDATE Uzytkownik SET Godzina_logowania="+"'"+java.time.LocalTime.now()+"'"+" WHERE Login="+"'"+login+"'");
                    return true;
                }
            }
        }
        return false;

    }
}
