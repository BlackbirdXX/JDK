import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final ServerWindow server;
    String userName;
    private boolean connected;

    JTextArea log;
    JPanel panelTop;
    JTextField fieldIP;
    JTextField fieldPort;
    JTextField fieldUserName;
    JPasswordField fieldPassword;
    JButton btnLogin;
    JPanel panelLog;
    JPanel panelBottom;
    JTextField fieldMessage;
    JButton btnSend;


    public  void connectServer(){
        if(server.connectClient(this)){
            panelTop.setVisible(false);
            connected = true;
            writeLogClient("Connect to server");
        }
        else {
            writeLogClient("The server is not available");
        }
    }

    public void disconnectServer(){
        if(connected){
            panelTop.setVisible(true);
            connected = false;
            server.disconnectClient(this);
            writeLogClient("The server is not available");
        }
    }

    private void writeLogClient(String text){
        log.append((text + "\n"));
    }

    public void sendMessage(){
        if(connected){
            String text = fieldMessage.getText();
            if(!text.isEmpty()){
                server.message(userName + ": " + text);
                fieldMessage.setText("");
            }
        }
        else {
            writeLogClient("The message has not been sent. Check the connection");
        }
    }

    public void receivingMessages(String text){
        writeLogClient(text);
    }

    private JPanel panelTop(){
        panelTop = new JPanel(new GridLayout(2, 3));
        fieldIP = new JTextField("192.168.0.1");
        fieldPort = new JTextField("8800");
        fieldUserName = new JTextField(userName);
        fieldPassword = new JPasswordField("qwerty");
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectServer();
            }
        });
        panelTop.add(fieldIP);
        panelTop.add(fieldPort);
        panelTop.add(fieldUserName);
        panelTop.add(fieldPassword);
        panelTop.add(btnLogin);

        return panelTop;
    }

    private  JPanel panelLog(){
        panelLog = new JPanel(new GridLayout(1,1));
        log = new JTextArea();
        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        panelLog.add(scrollLog);
        return panelLog;
    }

    private JPanel panelBottom(){
        panelBottom = new JPanel(new BorderLayout());
        fieldMessage = new JTextField();
        btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panelBottom.add(fieldMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);

        return panelBottom;
    }


    Client(ServerWindow server, String name){
        this.server = server;
        this.userName = name;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat client");
        add(panelTop(), BorderLayout.NORTH);
        add(panelLog(),BorderLayout.CENTER);
        add(panelBottom(), BorderLayout.SOUTH);
        setVisible(true);
    }
}
