package game.main;
import java.util.*;

public class HandEvaluator {

    //Check for a flush
    public static boolean isFlush(List<Card> hand) {
        String suit = hand.get(0).getSuit();
        for (Card card : hand) {
            if (!card.getSuit().equals(suit)) {
                return false;
            }
        }
        return true;
    }

    //Check for a straight
    public static boolean isStraight(List<Card> hand) {
        List<Integer> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRankValue());
        }
        Collections.sort(ranks);
        for (int i = 0; i < ranks.size() - 1; i++) {
            if (ranks.get(i) + 1 != ranks.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    //Check for four of a kind
    public static boolean isFourOfAKind(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCount.containsValue(4);
    }

    //Check for three of a kind
    private static boolean isThreeOfAKind(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCount.containsValue(3);  // Returns true if any rank appears 3 times
    }

    //Check for one pair
    private static boolean isOnePair(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCount.containsValue(2);  // Returns true if any rank appears exactly 2 times
    }

    //Check for two pair
    private static boolean isTwoPair(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        
        int pairCount = 0;
        for (int count : rankCount.values()) {
            if (count == 2) {
                pairCount++;
            }
        }
        return pairCount == 2;  // Returns true if there are exactly two pairs
    }

    //Check for full house
    private static boolean isFullHouse(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        
        boolean hasThreeOfAKind = false;
        boolean hasPair = false;
        
        for (int count : rankCount.values()) {
            if (count == 3) {
                hasThreeOfAKind = true;
            } else if (count == 2) {
                hasPair = true;
            }
        }
        
        return hasThreeOfAKind && hasPair;  // Returns true if there's a three of a kind and a pair
    }

    //Compare two hands that have the same value, -1 for error, 0 for tie, 1 for first hand, 2 for second hand
    //Only accepts 5 card hands
    public static int compareEqualHands(List<Card> hand1, List<Card> hand2) {
        // Make sure sorted high → low
        sortCardsByRank(hand1);
        sortCardsByRank(hand2);

        for (int i = 0; i < 5; i++) {
            int r1 = hand1.get(i).getRankValue();
            int r2 = hand2.get(i).getRankValue();
            if (r1 > r2) return 1;
            if (r2 > r1) return 2;
        }
        return 0; // Tie
    }
    
    //Turn a seven card hand into best possible 5 card hand
    public static List<Card> convert5Card(List<Card> hand){
    	if(hand.size() != 7)
    		return null;
    	
    	String handType = bestHand(hand);
    	
    	switch(handType) {
	    	case "Straight Flush": return convertStraightFlush(hand);
	    	case "Four of a Kind": return convertFourOfAKind(hand);
	    	case "Full House": return convertFullHouse(hand);
	    	case "Flush": return convertFlush(hand);
	    	case "Straight": return convertStraight(hand);
	    	case "Three of a Kind": return convertThreeOfAKind(hand);
	    	case "Two Pair": return convertTwoPair(hand);
	    	case "One Pair": return convertOnePair(hand);
	    	case "High Card": return convertHighCard(hand);
    	}
    	
    	return null;
    }
    
    private static List<Card> convertHighCard(List<Card> hand) {
    	sortCardsByRank(hand);
    	return hand.subList(0, 5);
	}

	private static List<Card> convertOnePair(List<Card> hand) {
		sortCardsByRank(hand);
		List<Card> bestHand = new ArrayList<>();
	    Map<Integer, List<Card>> rankMap = groupByRank(hand);

	    for (List<Card> cards : rankMap.values()) {
	        if (cards.size() == 2) {
	            bestHand.addAll(cards);
	            break;
	        }
	    }

	    for (Card card : hand) {
	        if (bestHand.size() < 5 && !bestHand.contains(card)) {
	            bestHand.add(card);
	        }
	    }

	    return bestHand;
	}

	private static List<Card> convertTwoPair(List<Card> hand) {
		sortCardsByRank(hand);
		List<Card> bestHand = new ArrayList<>();
	    Map<Integer, List<Card>> rankMap = groupByRank(hand);
	    int pairsFound = 0;

	    for (List<Card> cards : rankMap.values()) {
	        if (cards.size() == 2) {
	            bestHand.addAll(cards);
	            pairsFound++;
	            if (pairsFound == 2) break;
	        }
	    }

	    for (Card card : hand) {
	        if (bestHand.size() < 5 && !bestHand.contains(card)) {
	            bestHand.add(card);
	        }
	    }

	    return bestHand;
	}

	private static List<Card> convertThreeOfAKind(List<Card> hand) {
		sortCardsByRank(hand);
		List<Card> bestHand = new ArrayList<>();
	    Map<Integer, List<Card>> rankMap = groupByRank(hand);

	    for (List<Card> cards : rankMap.values()) {
	        if (cards.size() == 3) {
	            bestHand.addAll(cards);
	            break;
	        }
	    }

	    for (Card card : hand) {
	        if (bestHand.size() < 5 && !bestHand.contains(card)) {
	            bestHand.add(card);
	        }
	    }

	    return bestHand;
	}

	private static List<Card> convertStraight(List<Card> hand) {
	    // Remove duplicate ranks & sort by descending rank
	    List<Card> sortedHand = new ArrayList<>(new TreeSet<>(Comparator.comparingInt(Card::getRankValue).reversed()));
	    sortedHand.addAll(hand);

	    List<Card> bestStraight = new ArrayList<>();
	    int lastRank = -1;

	    for (Card card : sortedHand) {
	        int currentRank = card.getRankValue();

	        // Skip duplicate ranks
	        if (currentRank == lastRank) continue;

	        // Add to straight if consecutive, otherwise reset
	        if (bestStraight.isEmpty() || lastRank - 1 == currentRank) {
	            bestStraight.add(card);
	        } else {
	            bestStraight.clear();
	            bestStraight.add(card);
	        }

	        lastRank = currentRank;

	        if (bestStraight.size() == 5) return bestStraight; // Found a straight!
	    }

	    return null; // No straight found
	}



	private static List<Card> convertFlush(List<Card> hand) {
	    sortCardsByRank(hand);
	    List<Card> clubs = new ArrayList<>();
	    List<Card> hearts = new ArrayList<>();;
	    List<Card> spades = new ArrayList<>();;
	    List<Card> diamonds = new ArrayList<>();;
	    		
	    for(Card card : hand) {
	    	switch(card.getSuit()){
		    	case "C": clubs.add(card); break;
		    	case "H": hearts.add(card); break;
		    	case "S": spades.add(card); break;
		    	case "D": diamonds.add(card); break;
	    	}
	    }
	    
	    if(clubs.size() >= 5)
	    	return clubs.subList(0, 5);
	    
	    if(hearts.size() >= 5)
	    	return hearts.subList(0, 5);
	    
	    if(spades.size() >= 5)
	    	return spades.subList(0, 5);
	    
	    if(diamonds.size() >= 5)
	    	return diamonds.subList(0, 5);
	    
	    
	    return null;
	}


	private static List<Card> convertFullHouse(List<Card> hand) {
		sortCardsByRank(hand);
		List<Card> bestHand = new ArrayList<>();
	    Map<Integer, List<Card>> rankMap = groupByRank(hand);
	    List<Card> threeOfAKind = new ArrayList<>();
	    List<Card> pair = new ArrayList<>();

	    for (List<Card> cards : rankMap.values()) {
	        if (cards.size() == 3 && threeOfAKind.isEmpty()) {
	            threeOfAKind.addAll(cards);
	        } else if (cards.size() >= 2 && pair.isEmpty()) {
	            pair.addAll(cards.subList(0, 2));
	        }
	    }

	    if (!threeOfAKind.isEmpty() && !pair.isEmpty()) {
	        bestHand.addAll(threeOfAKind);
	        bestHand.addAll(pair);
	        return bestHand;
	    }

	    return null;
	}
	
	private static List<Card> convertFourOfAKind(List<Card> hand) {
		sortCardsByRank(hand);
		List<Card> bestHand = new ArrayList<>();
	    Map<Integer, List<Card>> rankMap = groupByRank(hand);

	    for (List<Card> cards : rankMap.values()) {
	        if (cards.size() == 4) {
	            bestHand.addAll(cards);
	            break;
	        }
	    }

	    for (Card card : hand) {
	        if (bestHand.size() < 5 && !bestHand.contains(card)) {
	            bestHand.add(card);
	        }
	    }

	    return bestHand;
	}

	private static List<Card> convertStraightFlush(List<Card> hand) {
		sortCardsByRank(hand);
		Map<String, List<Card>> suitMap = groupBySuit(hand);

	    for (List<Card> cards : suitMap.values()) {
	        if (cards.size() >= 5) {
	            List<Card> straightFlush = convertStraight(cards);
	            if (straightFlush != null) return straightFlush;
	        }
	    }

	    return null;
	}

	//Sorts the hand by their card value
    public static void sortCardsByRank(List<Card> cards) {
        cards.sort((c1, c2) -> Integer.compare(c2.getRankValue(), c1.getRankValue()));
    }
    
    private static Map<Integer, List<Card>> groupByRank(List<Card> hand) {
        Map<Integer, List<Card>> rankGroups = new HashMap<>();

        for (Card card : hand) {
            rankGroups.putIfAbsent(card.getRankValue(), new ArrayList<>());
            rankGroups.get(card.getRankValue()).add(card);
        }

        return rankGroups;
    }


    private static Map<String, List<Card>> groupBySuit(List<Card> hand) {
        Map<String, List<Card>> suitGroups = new HashMap<>();

        for (Card card : hand) {
            suitGroups.putIfAbsent(card.getSuit(), new ArrayList<>());  // Use the suit string as the key
            suitGroups.get(card.getSuit()).add(card);  // Add the card to the corresponding suit list
        }

        return suitGroups;
    }


    
    // Combine checks for all hand ranks
    public static String evaluateHand(List<Card> hand) {
        if (isFlush(hand) && isStraight(hand)) {
            return "Straight Flush";
        }else if (isFourOfAKind(hand)){
        	return "Four of a Kind";
        }else if(isFullHouse(hand)) {
        	return "Full House";
        }else if(isFlush(hand)) {
        	return "Flush";
        }else if(isStraight(hand)) {
        	return "Straight";
        }else if(isThreeOfAKind(hand)) {
        	return "Three of a Kind";
        }else if(isTwoPair(hand)) {
        	return "Two Pair";
        }else if(isOnePair(hand)) {
        	return "One Pair";
        }else {
	        // Check for other hands like Four of a Kind, Full House, etc.
	        return "High Card";
        }
    }
    
	// Find the best hand from 7 cards
    public static String bestHand(List<Card> hand) {
    	
    	if(hand.size() == 2) {
    		if(hand.get(0).getRank().equals(hand.get(1).getRank())) {
    			return "One Pair";
    		}else
    			return "High Card";
    	}
    	
        List<List<Card>> hands = generateCombinations(hand);
        String bestHand = "";
        for (List<Card> combination : hands) {
            String currentHand = evaluateHand(combination);
            if (isBetterHand(currentHand, bestHand)) {
                bestHand = currentHand;
            }
        }
        return bestHand;
    }

    // Compare hands based on rankings
    public static boolean isBetterHand(String currentHand, String bestHand) {
        List<String> handRankings = Arrays.asList("High Card", "One Pair", "Two Pair", "Three of a Kind", "Straight", "Flush", "Full House", "Four of a Kind", "Straight Flush");
        return handRankings.indexOf(currentHand) > handRankings.indexOf(bestHand);
    }

    // Helper function to generate all 5-card combinations
    public static List<List<Card>> generateCombinations(List<Card> hand) {
        List<List<Card>> combinations = new ArrayList<>();
        for (int i = 0; i < hand.size() - 4; i++) {
            for (int j = i + 1; j < hand.size() - 3; j++) {
                for (int k = j + 1; k < hand.size() - 2; k++) {
                    for (int l = k + 1; l < hand.size() - 1; l++) {
                        for (int m = l + 1; m < hand.size(); m++) {
                            combinations.add(Arrays.asList(hand.get(i), hand.get(j), hand.get(k), hand.get(l), hand.get(m)));
                        }
                    }
                }
            }
        }
        return combinations;
    }
    
    public static int compareHands(List<Card> hand1, List<Card> hand2) {
        if (hand1.size() != 7 || hand2.size() != 7) {
            throw new IllegalArgumentException("Both hands must have exactly 7 cards");
        }

        //Find the best hand type for each
        String best1 = bestHand(hand1);
        String best2 = bestHand(hand2);

        //Compare category strength
        List<String> handRankings = Arrays.asList(
                "High Card", "One Pair", "Two Pair", "Three of a Kind", "Straight",
                "Flush", "Full House", "Four of a Kind", "Straight Flush");

        int rank1 = handRankings.indexOf(best1);
        int rank2 = handRankings.indexOf(best2);

        if (rank1 > rank2) return 1; // Hand 1 wins
        if (rank2 > rank1) return 2; // Hand 2 wins

        //If categories are equal, get the actual 5-card best hand and tie-break
        List<Card> five1 = convert5Card(hand1);
        List<Card> five2 = convert5Card(hand2);

        int cmp = compareEqualHands(five1, five2);
        if (cmp != 0) return cmp;
        return 0; // Tie
    }

}
