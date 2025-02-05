package game.main;

import java.util.*;

public class PokerGame {
	
	public enum BettingRound {
	    PRE_FLOP,
	    FLOP,
	    TURN,
	    RIVER,
	    SHOWDOWN
	}
	
    private List<Player> players;
    private BettingRound currentRound;
    private CommunityCards communityCards;
    private int pot;
    private Map<Player, Integer> bets;
    private Deck deck;

    public PokerGame(List<Player> players) {
        this.players = players;
        this.currentRound = BettingRound.PRE_FLOP;
        this.communityCards = new CommunityCards();
        this.pot = 0;
        this.bets = new HashMap<>();
        this.deck = new Deck();
    }

    public void startRound() {
        switch (currentRound) {
            case PRE_FLOP:
                System.out.println("Starting Pre-Flop round...");
                break;
            case FLOP:
                System.out.println("Dealing the Flop...");
                break;
            case TURN:
                System.out.println("Dealing the Turn...");
                break;
            case RIVER:
                System.out.println("Dealing the River...");
                break;
            case SHOWDOWN:
                System.out.println("Time for the Showdown!");
                break;
        }
    }

    public void dealFlop(List<Card> flop) {
        for (Card card : flop) {
            communityCards.addCard(card);
        }
    }

    public void dealTurn(Card turn) {
        communityCards.addCard(turn);
    }

    public void dealRiver(Card river) {
        communityCards.addCard(river);
    }

    public void nextRound() {
        switch (currentRound) {
            case PRE_FLOP:
                currentRound = BettingRound.FLOP;
                break;
            case FLOP:
                currentRound = BettingRound.TURN;
                break;
            case TURN:
                currentRound = BettingRound.RIVER;
                break;
            case RIVER:
                currentRound = BettingRound.SHOWDOWN;
                break;
            case SHOWDOWN:
                // End game logic goes here
                break;
        }
    }

    public void placeBet(Player player, int amount) {
        bets.put(player, amount);
        pot += amount;
        System.out.println(player.getUsername() + " bet: " + amount);
    }

    public int getPot() {
        return pot;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public BettingRound getCurrentRound() {
        return currentRound;
    }

    public CommunityCards getCommunityCards() {
        return communityCards;
    }
}
