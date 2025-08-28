package game.net;

import java.io.*;
import java.net.*;
import java.util.List;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void startListeningForMessages() {
    	// Start listening for messages from server
        new Thread(this::listenForMessages).start();
    }

    public void setGUIController(GUIController gui) {
        this.gui = gui;
        System.out.println("Gui is set");
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
                System.out.println(message);
                if (message.startsWith("HOLE_CARDS")) {
                    handleHoleCards(message);  
                }else if(message.startsWith("COMMUNITY_CARD")) {
                	handleCommunityCard(message);
                }else if (message.startsWith("PLAYER_LIST:")) {
                    String[] parts = message.substring(12).split(";");
                    String[] players = parts[0].split(",");
                    String activePlayer = parts.length > 1 ? parts[1].replace("ACTIVE:", "") : "";
                    
                    gui.updatePlayerList(List.of(players), activePlayer);
                }else if (message.startsWith("CHAT:")) {
                    gui.getChatPanel().addChatMessage(message.substring(5)); // Display chat message
                }
            }
            
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
        }
    }
    
    // Send a chat message to the server
    public void sendChatMessage(String message) {
        out.println("CHAT:" + message);
    }
    
    private void handleCommunityCard(String message) {
		String cardData = message.substring(message.indexOf(":") + 2);
		//System.out.println(cardData);
		this.gui.addCommunityCard(cardData);
	}

	private void handleHoleCards(String message) {
		this.gui.resetHoleCards();
    	String[] cardData = message.substring(11).split(", ");
        int index = cardData[1].indexOf("]");
        cardData[0] = cardData[0].substring(1);
        cardData[1] = cardData[1].substring(0, index);
        System.out.println(cardData[0] + "---" +cardData[1]);
        this.gui.addHoleCardsToGUI(cardData);
        System.out.println("Your hole cards: " + cardData[0] + " and " + cardData[1]); 
    }
    
    public PrintWriter getOut() {
    	return this.out;
    }
}
