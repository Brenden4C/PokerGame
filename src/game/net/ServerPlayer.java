package game.net;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import game.main.Card;
import game.main.Player;

public class ServerPlayer{
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
	private Player player;
	private String username;
	private boolean hasPlayed;
	
	public ServerPlayer(Player player, String username, Socket socket, BufferedReader in, PrintWriter out) {
		this.socket = socket;
		this.username = username;
		this.player = player;
		this.out = out;
		this.in = in;
		this.hasPlayed = false;
	}
	
	public BufferedReader getIn() {
		return in;
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
	
	public List<Card> getHand(){
		return player.getHand();
	}
	
	public void resetHand() {
		player.getHand().clear();
	}
	
	public void resetBet() {
		player.resetRoundBet();
	}

	public void unfold() {
		this.player.unfold();
	}
	
	public void fold() {
		this.player.fold();
	}
	
	public boolean isFolded() {
		return player.isFolded();
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void bet(int bet) {
		this.player.setCurrentBet( bet );
	}
	
	public int getBet() {
		return this.player.getCurrentBet();
	}
	
	public boolean hasPlayed() {
		return hasPlayed;
	}
	
	public void setHasPlayed(boolean b) {
		hasPlayed = b;
	}
}
