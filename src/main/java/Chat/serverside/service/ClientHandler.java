package Chat.serverside.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String log;
    private String pas;
    private String name;
    private boolean isAuthorized;
    private  baseAS baseAS;
    long a=System.currentTimeMillis();


    public ClientHandler(MyServer myServer, Socket socket) {

        try {

            this.myServer = myServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.name = "";

            Thread t1 = new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }

            });

            Thread t2 = new Thread(() -> {
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isAuthorized) {
                    closeConnection();
                }
            });

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.execute(t1);
            executorService.execute(t2);
        } catch (IOException e) {
            closeConnection();
            throw new RuntimeException("Problem with ClientHandler");
        }
    }

    public void authentication() throws IOException, SQLException, ClassNotFoundException {

        while (true) {
            String str = dis.readUTF();
            if (str.startsWith("/auth")) {
                String nick=baseAS.auth(str);
                if (nick != "foul") {
                    if (!myServer.isNickBusy(nick)) {
                        isAuthorized = true;
                        sendMessage("/authok " + nick);
                        name = nick;
                        myServer.broadcastMessage("Hello " + name);
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMessage("Nick is busy");
                    }
                } else {
                    sendMessage("Wrong login and password");
                }
            } else {
                sendMessage("Your command will be need start with /auth");
            }
        }

    }

    public void changeNick(String nick) throws SQLException, ClassNotFoundException {
        Statement statement=null;
        statement= Singleton.getConnection().createStatement();
        String previousNick=name;
        statement.executeUpdate("UPDATE users SET nick='"+nick+"' WHERE login='"+getLog()+"' AND password='"+getPas()+"'");
        LOGGER.info(name + " changed his nick to " + nick);
        name=nick;
        myServer.broadcastMessage(previousNick+" changed his nick to " + name);

    }

    public void readMessage() throws IOException, SQLException, ClassNotFoundException {
        while (true) {
            String messageFromClient = dis.readUTF();
            LOGGER.info(name + " send message " + messageFromClient);
            if (messageFromClient.trim().startsWith("/")) {

                if (messageFromClient.startsWith("/w")) {
                    String [] arr = messageFromClient.split(" ", 3);
                    myServer.sendMessageToCertainClient(this, arr[1], name + ": " + arr[2]);
                }

                if (messageFromClient.trim().startsWith("/list")) {
                    myServer.getOnlineUsersList(this);
                }

                if (messageFromClient.trim().startsWith("/changeNick")) {
                    String [] arr = messageFromClient.split(" ", 3);
                    changeNick(arr[1]);
                }

                if (messageFromClient.trim().startsWith("/end")) {
                    return;
                }
            } else {
                myServer.broadcastMessage(name + ": " + messageFromClient);
            }
        }
    }


    public void sendMessage(String message) {
        try {
            long t=System.currentTimeMillis()-a;
            if(t>120000) {
                LOGGER.info(this.getName() + " leave");
                closeConnection();
            }else{
                dos.writeUTF(message);
                a = System.currentTimeMillis();
            }
        } catch (IOException ignored) {
        }
    }

    private void closeConnection() {
        myServer.unsubscribe(this);
        myServer.broadcastMessage(name + " Leave chat");
        try {
            dis.close();
            dos.close();
            socket.close();
        } catch (IOException ignored) {
        }
    }


    public String getName() {
        return name;
    }

    public String getLog() {
        return log;
    }

    public String getPas() {
        return pas;
    }

}
