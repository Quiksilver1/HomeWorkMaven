package Chat.clientside.One;

import Chat.serverside.service.ClientHandler;
import Chat.serverside.service.Singleton;
import Chat.serverside.service.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EchoClient extends JFrame {

    private final Integer SERVER_PORT = 8081;
    private final String SERVER_ADDRESS = "localhost";
    private Socket socket;
    private String nick;
    private String log;
    DataInputStream dis;
    DataOutputStream dos;
    boolean isAuthorized = false;
    File file=null;
    File localHistory=new File("C:\\Program Files\\Java\\ExamLog4jProject\\src\\main\\java\\Chat\\clientside\\history.txt");

    ClientHandler clientHandler;

    private JTextField msgInputField;
    private JTextArea chatArea;
    FileInputStream fis;
    FileOutputStream fos;



    public EchoClient() {
        try {
            connection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareGUI();
    }

    public void connection() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        final int[] count = {0};
        new Thread(() -> {
            try {
                Statement statement=null;

                statement= Singleton.getConnection().createStatement();
                while (true) {
                    String messageFromServer = dis.readUTF();
                    if (messageFromServer.startsWith("/authok")) {
                        String[] arr = messageFromServer.split("\\s");
                        ResultSet rsNick = statement.executeQuery("SELECT * FROM users WHERE nick='" + arr[1] + "'");
                        while (rsNick.next()) {
                            User user1 = new User().userBuilder(rsNick);
                            log = user1.getLogin();
                            nick = user1.getNick();
                        }
                        file = new File("C:\\Program Files\\Java\\ExamLog4jProject\\src\\main\\java\\Chat\\clientside\\history_" + log + ".txt");
                        isAuthorized = true;
                    }
                    chatArea.append(messageFromServer + "\n");
                    break;
                }
                while (isAuthorized) {
                    if(count[0] ==0) {
                        count[0] =1;
                        try (BufferedReader reader = new BufferedReader(new FileReader(localHistory))) {
                            String str;
                            ArrayList<String> arr = new ArrayList<>();
                            while ((str = reader.readLine()) != null) {
                                arr.add(str);
                            }
                            if (arr.size() < 101) {
                                for (int i = 0; i < arr.size(); i++) {
                                    chatArea.append(arr.get(i) + "\n");
                                }
                            } else {
                                for (int i = arr.size() - 100; i < arr.size(); i++) {
                                    chatArea.append(arr.get(i) + "\n");
                                }
                            }
                            if (arr.size() > 301) {
                                localHistory.deleteOnExit();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    String messageFromServer = dis.readUTF();
                    chatArea.append(messageFromServer + "\n");

                    try(BufferedWriter writer=new BufferedWriter(new FileWriter(localHistory,true))) {
                        writer.write(messageFromServer + "\n");
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    try(BufferedWriter writer=new BufferedWriter(new FileWriter(file,true))) {
                        writer.write((messageFromServer + "\n"));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void send() {
        if (msgInputField.getText() != null && !msgInputField.getText().trim().isEmpty()) {
            try {
                dos.writeUTF(msgInputField.getText());
                if (msgInputField.getText().equals("/end")) {
                    isAuthorized = false;
                    closeConnection();
                }
                msgInputField.setText("");
            } catch (IOException ignored) {
            }
        }
    }

    private void closeConnection() {
        try {
            dis.close();
            dos.close();
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public void prepareGUI() {

        setBounds(600, 300, 500, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Send");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);


        btnSendMsg.addActionListener(e -> {
            send();
        });

        msgInputField.addActionListener(e -> {
            send();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Chat.clientside.Two.EchoClient();
        });
    }

}
