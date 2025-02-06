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
	
	public ServerPlayer(Player player, String username, Socket socket, BufferedReader in, PrintWriter out) {
		this.socket = socket;
		this.username = username;
		this.player = player;
		this.out = out;
		this.in = in;
	}
	
	public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
	
	public void setHoleCards(List<Card> holeCards) {
		this.player.setHand(holeCards);
	}
	
	public void sendHoleCards() {
	    try {
	    	out.println("HOLE_CARDS " + player.getHand());
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
		
	}

	
	
}
