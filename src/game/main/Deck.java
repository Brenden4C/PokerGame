package game.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    private static final String[] SUITS = {"S", "H", "D", "C"}; // Spades, Hearts, Diamonds, Clubs

    //Constructor
    //Adds all cards possible to deck and shuffles it.
    public Deck() {
        cards = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    //takes the deck and shuffles it into a random order
    public void shuffle() {
        Collections.shuffle(cards);
    }

    //returns a card from the deck and removes it from the list
    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size() - 1);
        }
        return null; // No cards left
    }
    
    //returns the number of cards in the deck
    public int size() {
        return cards.size();
    }

    //removes all cards from the deck and then readds all 52 possible cards, then shuffles it
    public void reset() {
        cards.clear();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    //override for printing to console
    @Override public String toString() {
        return cards.toString();
    }
}

