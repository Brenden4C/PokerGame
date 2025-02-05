package game.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

import game.main.Card;
import game.main.Player;

public class ServerPlayer{
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
	private Player player;
	private String username;
	private boolean holeCardsDealt = false; // Track if hole cards have been dealt yet.
	
	public ServerPlayer(Player player, String username, Socket socket, BufferedReader in, PrintWriter out) {
		this.socket = socket;
		this.username = username;
		this.player = player;
	}
	
	
	
	public void dealHoleCards() {
        List<Card> holeCards = new ArrayList<>();
        holeCards.add(new Card("Hearts", "A")); // Replace with deck.drawCard()
        holeCards.add(new Card("Spades", "K")); // Replace with deck.drawCard()
        
        player.setHand(holeCards);
        sendMessage("HOLE_CARDS " + holeCards.get(0) + "," + holeCards.get(1));
    }
	
	public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
	
	public void closeConnection() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
	
	public void setHoleCards(List<Card> holeCards) {
		this.player.setHand(holeCards);
	}
	
	public void sendHoleCards() {
		if(!holeCardsDealt) {
		    try {
		        System.out.println("sendHoleCards invoked for player: " + player.getUsername());
		        //networkController.getOut().println("Your hole cards: " + player.getHand());
		        System.out.println("Player " + player.getUsername() + " was dealt: " + player.getHand());
	
		        List<Card> holeCards = player.getHand();
		        holeCardsDealt = true;
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		}
	}

	
	
}
