import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private boolean isServerWorking;
    public static final String LOG_PATH = "HW01/src/log.txt";
    JTextArea log;
    JButton btnStart;
    JButton btnStop;
    JPanel startStopBtnPanel;
    JPanel textLogPanel;
    List<Client> clientList;

    public boolean connectClient(Client client) {
        if (isServerWorking) {
            clientList.add(client);
            writeLog(client.userName + " connected");
            readMessageLogFile(client);
            return true;
        } else {
            return false;
        }
    }

    public void disconnectClient(Client client) {
        clientList.remove(client);
        if (client != null) {
            client.disconnectServer();
        }
    }

    private void sending(String text) {
        for (Client client : clientList) {
            client.receivingMessages(text);
        }
    }

    private void writeLog(String text) {
        log.append(text + "\n");
    }

    public void message(String text) {
        if (isServerWorking) {
            writeLog(text);
            sending(text);
            writeMessageLogFile(text);
        } else {
            writeLog("Server problems");
        }
    }

    private void writeMessageLogFile(String text) {

        try (FileWriter fileWriter = new FileWriter(LOG_PATH, true)) {
            fileWriter.write(text);
            fileWriter.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readMessageLogFile(Client client){
        StringBuilder sb = new StringBuilder();
        try (FileReader fileReader = new FileReader(LOG_PATH)){
            int c;
            while ((c = fileReader.read()) != -1){
                sb.append((char)c);
            }
            sb.delete(sb.length()-1, sb.length());
            client.receivingMessages(sb.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            client.receivingMessages("Message logs are missing");
        }
    }

    private JPanel startStopBtnPanel() {
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                writeLog("Server stopped");
                while (!clientList.isEmpty()) {
                    disconnectClient(clientList.getLast());
                }
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                writeLog("Server started");
            }
        });

        startStopBtnPanel = new JPanel(new GridLayout(1, 2));
        startStopBtnPanel.add(btnStart);
        startStopBtnPanel.add(btnStop);
        startStopBtnPanel.setPreferredSize(new Dimension(WIDTH - 10, 20));
        return startStopBtnPanel;
    }

    JPanel textLogPanel() {
        log = new JTextArea();
        log.setLineWrap(true);
        textLogPanel = new JPanel(new GridLayout(1, 1));
        JScrollPane scrollLog = new JScrollPane(log);
        textLogPanel.add(scrollLog);
        return textLogPanel;
    }

    ServerWindow(){
        clientList = new ArrayList<>();
        isServerWorking = false;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        setLayout(new GridLayout(2,1));
        add(textLogPanel());
        add(startStopBtnPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }
}
