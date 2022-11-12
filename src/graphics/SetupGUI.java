package graphics;

import Client.Client;
import Server.Server;
import Server.Setup;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public final class SetupGUI extends JFrame{
    JButton buttonServer = new JButton("server");
    JButton buttonClient = new JButton("client");
    JPanel panelButtons = new JPanel(new FlowLayout());
    public Setup setup;
    Client client;
    Server server;

    public SetupGUI() {
        this.setSize(320,320);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.initButtons();

        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.pack();
    }

    private void initButtons() {
        buttonClient.setFocusable(false);
        buttonServer.setFocusable(false);

        buttonClient.setPreferredSize(new Dimension(100,50));
        buttonServer.setPreferredSize(new Dimension(100,50));

        panelButtons.add(buttonClient);
        panelButtons.add(buttonServer);

        buttonClient.addActionListener(e -> {
            if(e.getSource() == buttonClient) {
                setup = new Setup(
                        JOptionPane.showInputDialog("введите IP"),
                        Integer.parseInt(JOptionPane.showInputDialog("введите port"))
                );
                try
                {
                    this.dispose();
                    client = new Client(setup);

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        buttonServer.addActionListener(e -> {
            if(e.getSource() == buttonServer) {
                try
                {
                    setup = new Setup("localhost"
                            /*new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine().trim()*/,
                            Integer.parseInt(JOptionPane.showInputDialog("введите port"))
                    );
                    this.dispose();
                    server = new Server(setup);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        this.add(panelButtons, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new SetupGUI();
    }
}
