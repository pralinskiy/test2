package Server;

import Connection.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private HashMap<Connection, String> clients = new HashMap<>();
    public Setup setup;

    public Server(Setup setup) {
        this.setup = setup;
        this.initServer();
    }
    private void initServer() {
        try
        {
            serverSocket = new ServerSocket(this.setup.port);
            enableConnections();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private void enableConnections() throws IOException, ClassNotFoundException {
        while(true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket);
        }
    }

    private void sendMessageToAllUsers(Message message) throws IOException {
        for(Map.Entry<Connection, String> client : clients.entrySet()) {
            client.getKey().send(new Message(MessageType.TEXT_MESSAGE, /*client.getValue() +*/ message.message + "\n"));
        }
    }

    class ServerThread extends Thread {
        Socket socketInThread;
        String name;
        String id;
        Connection connection;

        public ServerThread(Socket socket) throws IOException {
            this.socketInThread = socket;
            id = Arrays.toString(socketInThread.getInetAddress().getAddress());
            name = id;
            connection = new Connection(this.socketInThread);
            clients.put(connection, name);
            //connection.send(new Message(MessageType.ACCEPTED));
            connection.send(new Message(MessageType.TEXT_MESSAGE, name + " has joined\n"));
            this.start();
        }

        @Override
        public void run() {
            try
            {
                startClientsChat(connection);
            }
            catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        }

        private void startClientsChat(Connection connection) throws IOException, ClassNotFoundException {

            while(true) {
                Message message = connection.receive();

                if(message.messageType.equals(MessageType.TEXT_MESSAGE)) {
                    sendMessageToAllUsers(new Message(MessageType.TEXT_MESSAGE, this.name + ": " + message.message));
                }
                else if(message.messageType.equals(MessageType.USER_HAS_LEFT_MESSAGE)) {
                    sendMessageToAllUsers(new Message(MessageType.USER_HAS_LEFT_MESSAGE, this.name + " has left"));
                    clients.remove(connection);
                    connection.close();
                    break;
                }
                else if(message.messageType.equals(MessageType.NAME_CHANGE_MESSAGE)) {
                    clients.replace(connection, clients.get(connection), message.message);
                    this.name = clients.get(connection);
                }


            }
        }


    }

}
