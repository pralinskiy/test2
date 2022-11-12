package Client;

import Connection.*;
import Server.*;

import java.io.*;
import java.net.*;

public final class Client {

    Connection connection;
    ChatGUI chatGUI;

    public Client(Setup setup) throws IOException, ClassNotFoundException {
        chatGUI = new ChatGUI(this);
        connectToServer(setup);
        startChat(this.connection);
    }
    private void connectToServer(Setup setup) throws IOException, ClassNotFoundException {
        connection = new Connection(new Socket(setup.adressIP, setup.port));
        //return connection.receive().messageType.equals(MessageType.ACCEPTED);
    }

    private void startChat(Connection connection) throws IOException, ClassNotFoundException {

        while(true) {
            Message message = connection.receive();

            chatGUI.refreshDialog(message);
        }
    }






    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Client(new Setup("localhost", 8081));
    }
}
