package Chat.serverside.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Singleton {

    static final String DRIVER= "com.mysql.cj.jdbc.Driver";
    static final String DB="jdbc:mysql://localhost/lesson1";
    static final String USER="root";
    static final String PASSWORD="Quiksilver1994";
    static private Connection connection;



    private Singleton(){
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection==null){
            connection=initConnection();
        }
        return connection;
    }

    public static Connection initConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection connection= DriverManager.getConnection(DB,USER,PASSWORD);
        return connection;
    }
}

