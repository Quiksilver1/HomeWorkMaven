package Chat.serverside.service;

import Chat.serverside.interf.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class MyServer {
    private static final Logger LOGGER = LogManager.getLogger(MyServer.class);
    private final int PORT = 8081;

    public List<ClientHandler> getClients() {
        return clients;
    }

    private List<ClientHandler> clients;

    private AuthService authService;

    public MyServer() {
        try (ServerSocket server = new ServerSocket(PORT)){

            authService = new BaseAuthService();
            authService.start();

            clients = new ArrayList<>();

            while (true) {
                LOGGER.info("Server wait connection");
                Socket socket = server.accept();
                System.out.println(socket.getInetAddress().getCanonicalHostName());
                LOGGER.info("Client is joined");
                new ClientHandler(this, socket);
            }
        } catch (IOException e){
            LOGGER.info("Server is down");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized void broadcastMessage(String message) {
        try {
            for (ClientHandler c : clients) {
                c.sendMessage(message);
            }
        }catch(ConcurrentModificationException ignored){
        }
    }

    public synchronized void sendMessageToCertainClient(ClientHandler from, String toName, String message) {
        try {
            for (ClientHandler c : clients) {
                if (c.getName().equals(toName)) {
                    c.sendMessage(message);
                    from.sendMessage(message);
                    LOGGER.info(from.getName()+" send message for " +c.getName()+": " + message);
                }
            }
        }catch (ConcurrentModificationException ignored){
        }
    }

    public synchronized void getOnlineUsersList(ClientHandler clientHandler) {
        StringBuilder sb = new StringBuilder("");
        for (ClientHandler c : clients) {
            if (!c.equals(clientHandler)) {
                sb.append(c.getName()).append(", ");
            }
        }
        int size = sb.length();
        sb.deleteCharAt(size - 1);
        sb.deleteCharAt(size - 2);
        clientHandler.sendMessage(sb.toString());
    }

    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }
}
