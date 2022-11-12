package Client;

import Connection.Message;
import Connection.MessageType;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChatGUI extends JFrame {
    private static final int GUI_WIDTH = 500;
    private static final int GUI_HEIGHT = 500;

    JTextArea textOutput = new JTextArea();
    public JTextField textInput = new JTextField();
    JTextArea textMembers = new JTextArea();
    JPanel panelSouth = new JPanel(new GridLayout(2, 1));
    JPanel panelSouthHelp = new JPanel(new FlowLayout());
    JButton buttonSendMessage = new JButton("send");
    JButton buttonChangeName = new JButton("name");
    JButton buttonStopChatting = new JButton("stop");
    Client client;

    public ChatGUI(Client client) {
        super(client.toString());
        this.client = client;
        this.initChat();
    }
    public ChatGUI() {
        super("tesst");
        this.initChat();
    }
    private void initChat() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setResizable(false);

        this.initBorders();
        this.initActionListeners();

        this.setVisible(true);
    }
    private void initBorders() {
        textOutput.setLineWrap(true);
        textOutput.setBackground(Color.green);
        textOutput.setEditable(false);

        textMembers.setLineWrap(true);
        textMembers.setBackground(Color.gray);
        textMembers.setEditable(false);

        panelSouth.add(textInput);
        panelSouthHelp.add(buttonSendMessage);
        panelSouthHelp.add(buttonChangeName);
        panelSouthHelp.add(buttonStopChatting);
        panelSouth.add(panelSouthHelp);

        this.add(new JScrollPane(textOutput), BorderLayout.CENTER);
        this.add(new JScrollPane(textMembers), BorderLayout.EAST);
        this.add(panelSouth, BorderLayout.SOUTH);
    }

    private void initActionListeners() {
        buttonSendMessage.addActionListener(e -> {
            if(e.getSource() == buttonSendMessage) {
                try {
                    client.connection.send(new Message(MessageType.TEXT_MESSAGE, textInput.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonChangeName.addActionListener(e -> {
            if(e.getSource() == buttonChangeName) {
                try {
                    client.connection.send(new Message(MessageType.NAME_CHANGE_MESSAGE, JOptionPane.showInputDialog("¬ведите им€")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonStopChatting.addActionListener(e -> {
            if(e.getSource() == buttonStopChatting) {
                try {
                    client.connection.send(new Message(MessageType.USER_HAS_LEFT_MESSAGE));
                    client.connection.close();
                    System.exit(-13);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void refreshDialog(Message message) {
        this.textOutput.append(message.message);
    }

    public static void main(String[] args) throws IOException {
        new ChatGUI();
    }
}
