package Chat.serverside.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class baseAS {

    private static String log;
    private static String pas;
    private static String nick;
    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ClientHandler clientHandler;
    private ResultSet rsNick;


    public static String getLog() {
        return log;
    }

    public static  String getPas() {
        return pas;
    }

    public static  String getNick() {
        return nick;
    }


    public baseAS(MyServer myServer, Socket socket) throws SQLException, ClassNotFoundException, IOException {

        Statement statement = null;

        this.myServer = myServer;
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    public static String auth(String str) throws SQLException, ClassNotFoundException {
        Statement statement = null;

        statement = Singleton.getConnection().createStatement();

        String[] arr = str.split("\\s");
        ResultSet rsNick = statement.executeQuery("SELECT * FROM users WHERE login='" + arr[1] + "' AND password='" + arr[2] + "'");
        while (rsNick.next()) {
            User user1 = new User().userBuilder(rsNick);
            log = user1.getLogin();
            pas = user1.getPassword();
            nick = user1.getNick();
            return nick;
        }
        return "foul";
    }
}
