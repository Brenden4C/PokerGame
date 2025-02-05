package game.main;

import java.util.*;

public class BettingRound {
    private List<Player> players;
    private int pot;
    private int currentBet;
    private int round;
    private boolean roundComplete;

    public BettingRound(List<Player> players) {
        this.players = players;
        this.pot = 0;
        this.currentBet = 0;
        this.round = 1;
        this.roundComplete = false;
    }

    // Start a new round of betting
    public void startRound() {
        roundComplete = false;
        for (Player player : players) {
            player.resetRoundBet();
        }
    }

    // Process a player's action during the round (call, raise, fold)
    public void processPlayerAction(Player player, String action, int betAmount) {
        if (player.isFolded()) return;

        switch (action.toLowerCase()) {
            case "call":
                call(player, betAmount);
                break;
            case "raise":
                raise(player, betAmount);
                break;
            case "fold":
                fold(player);
                break;
            case "check":
                check(player);
                break;
            default:
                System.out.println("Invalid action.");
                break;
        }
    }

    // Handle a player calling (matching the current bet)
    private void call(Player player, int betAmount) {
        if (betAmount > player.getChips()) {
            System.out.println(player.getUsername() + " doesn't have enough chips to call!");
            return;
        }

        int callAmount = betAmount - player.getCurrentBet();
        player.deductChips(callAmount);
        player.setCurrentBet(betAmount);
        pot += callAmount;

        System.out.println(player.getUsername() + " calls with " + callAmount + " chips.");
    }

    // Handle a player raising (increasing the bet)
    private void raise(Player player, int raiseAmount) {
        if (raiseAmount > player.getChips()) {
            System.out.println(player.getUsername() + " doesn't have enough chips to raise!");
            return;
        }

        int totalBet = currentBet + raiseAmount;
        player.deductChips(player.getChips() - totalBet);
        player.setCurrentBet(totalBet);
        pot += totalBet;

        currentBet = totalBet;

        System.out.println(player.getUsername() + " raises to " + totalBet + " chips.");
    }

    // Handle a player folding (removes them from the round)
    private void fold(Player player) {
        player.fold();
        System.out.println(player.getUsername() + " folds.");
    }

    // Handle a player checking (pass without betting)
    private void check(Player player) {
        if (player.getCurrentBet() < currentBet) {
            System.out.println(player.getUsername() + " cannot check. The current bet is higher than their current bet.");
        } else {
            System.out.println(player.getUsername() + " checks.");
        }
    }

    // Check if the round is complete (all players have called or folded)
    public boolean isRoundComplete() {
        int playersRemaining = 0;
        for (Player player : players) {
            if (!player.isFolded()) {
                playersRemaining++;
            }
        }
        return playersRemaining <= 1; // If only one player remains, round is complete
    }

    public int getPot() {
        return pot;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void nextRound() {
        round++;
    }

    public int getRound() {
        return round;
    }
}

