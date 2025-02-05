package game.main;

import java.util.ArrayList;
import java.util.List;

public class CommunityCards {
    private List<Card> cards;

    public CommunityCards() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void clear() {
        cards.clear();
    }

    @Override public String toString() {
        return "Community Cards: " + cards;
    }
}

