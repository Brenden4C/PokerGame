package game.net;

import java.io.*;
import java.net.*;
import game.gui.GUIController;

public class NetworkController {
    private String serverIP;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GUIController gui;

    public NetworkController(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    // Explicitly connect to the server
    public void connect() {
        try {
            socket = new Socket(serverIP, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start listening for messages from server
            new Thread(this::listenForMessages).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGUIController(GUIController gui) {
        this.gui = gui;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
                if (message.startsWith("HOLE_CARDS")) {
                    String[] cardData = message.substring(11).split(",");
                    System.out.println("Your hole cards: " + cardData[0] + " and " + cardData[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
        }
    }
    
    public PrintWriter getOut() {
    	return this.out;
    }
}
