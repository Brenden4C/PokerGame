package game.main;

import java.util.Arrays;
import java.util.List;

public class GameController {

    public static void main(String[] args) {
    	String bestHand = "";
    	Deck deck = new Deck();
    	List<Card> hand = Arrays.asList(deck.drawCard(), deck.drawCard(), deck.drawCard(), deck.drawCard(), deck.drawCard(), deck.drawCard(), deck.drawCard());

    	HandEvaluator.sortCardsByRank(hand);
    	System.out.println(hand);
        bestHand = HandEvaluator.bestHand(hand);
    	
        System.out.println("Best hand: " + bestHand);
        List<Card> hand2 = HandEvaluator.convert5Card(hand);
        System.out.println(hand2);
        
        int runs = 0;
        
        
    	while(!bestHand.equals("Straight Flush") | hand.get(0).getRankValue() != 14 | hand.get(1).getRankValue() != 13 | !(hand.get(0).getSuit().equals("S"))) {
    		deck = new Deck();
	    	
	    	hand = Arrays.asList(deck.drawCard(), deck.drawCard(), deck.drawCard(), deck.drawCard(), deck.drawCard());
	
	    	HandEvaluator.sortCardsByRank(hand);
	    	System.out.println(hand);
	        bestHand = HandEvaluator.bestHand(hand);
	        System.out.println("Best hand: " + bestHand);
	        //hand2 = HandEvaluator.convert5Card(hand);
	        //System.out.println(hand2);
	        runs ++;
    	}
    	System.out.println(runs + "");
        
    }
	
}
