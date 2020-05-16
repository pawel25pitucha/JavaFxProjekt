package sample;

import java.sql.*;

public class ConnectionDB {
    public static boolean ConnectionDB(String user, String password) throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String dbURL = "jdbc:sqlserver://localhost:52801;";
        try {
            Connection conn = DriverManager.getConnection(dbURL, user, password);
                return true;

        }catch(Exception e){
            return false;
        }

    }

}
