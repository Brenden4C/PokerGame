package game.main;

public class Card {
    private String rank; //(Value 2-A)
    private String suit; //(Heart, Spade, etc.)

    //Constructor 
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }
    
    private String suitString() {
    	if(this.suit.equals("H")) {
    		return "hearts";
    	}else if(this.suit.equals("S")) {
    		return "spades";
    	}else if(this.suit.equals("C")) {
    		return "clubs";
    	}else
    		return "diamonds";
    	
    }

    public int getRankValue() {
        switch (rank) {
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "T": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "A": return 14;
            default: return -1;
        }
    }
    
	//Makes it to when we print a card in console it will return the rank and suit
    @Override public String toString() {
        return "" + rank  + "_of_" + suitString();
    }
}
