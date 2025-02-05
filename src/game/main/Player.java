package game.main;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String username;
	private List<Card> hand;
	private int chips;
	private int currentBet;
	private boolean isFolded;
	
	public Player(String username) {
		this.username = username;
		this.hand = new ArrayList<Card>();
		this.chips = 0;
	}
	
	public Player(String username, int chips) {
		this.username = username;
		this.hand = new ArrayList<Card>();
		this.chips = chips;
	}
	
	public String getUsername() {
        return username;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }
    
    public void setHand(List<Card> hand) {
    	this.hand = hand;
    }

    public void clearHand() {
        hand.clear();
    }
    
    public int getChips() {
        return chips;
    }

    public void deductChips(int amount) {
        chips -= amount;
    }

    public void addChips(int amount) {
        chips += amount;
    }
    
    public int getCurrentBet() {
    	return currentBet;
    }
    
    public void setCurrentBet(int bet) {
    	this.currentBet = bet;
    }
    
    public boolean isFolded() {
    	return isFolded;
    }
    
    public void fold() {
    	this.isFolded = true;
    }
    
    public void resetRoundBet() {
    	this.currentBet = 0;
    }

    @Override public String toString() {
        return username + "'s hand: " + hand;
    }
	
}
