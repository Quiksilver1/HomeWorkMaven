package Chat.serverside.service;

import Chat.serverside.interf.AuthService;

import java.sql.SQLException;
import java.sql.Statement;


public class BaseAuthService implements AuthService {


    public BaseAuthService() throws SQLException, ClassNotFoundException {
        Statement statement=null;
        statement= Singleton.getConnection().createStatement();

        statement.executeUpdate("DELETE FROM users");

        statement.executeUpdate("INSERT INTO users (login,password,nick) VALUES ('D','1','One')");
        statement.executeUpdate("INSERT INTO users (login,password,nick) VALUES ('V','1','Two')");
        statement.executeUpdate("INSERT INTO users (login,password,nick) VALUES ('VL','1','Three')");
    }

    @Override
    public void start() {
        System.out.println("AuthService start");
    }

    @Override
    public void stop() {
        System.out.println("AuthService stop");
    }


}
