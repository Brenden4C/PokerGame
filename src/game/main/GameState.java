package game.main;

public class GameState {
	
	public enum State {

		WAITING_FOR_PLAYERS,
		PRE_FLOP,
		FLOP,
		TURN,
		RIVER,
		SHOWDOWN
		
	}

	private State currentState;
	private int currentBet = 0;
	private int pot = 0;
	
	public GameState() {
		this.currentState = State.WAITING_FOR_PLAYERS;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void nextPhase() {
		switch(currentState) {
			case WAITING_FOR_PLAYERS -> currentState = State.PRE_FLOP;
			case PRE_FLOP -> currentState = State.FLOP;
			case FLOP -> currentState = State.TURN;
			case TURN -> currentState = State.RIVER;
			case RIVER -> currentState = State.SHOWDOWN;
			case SHOWDOWN -> currentState = State.WAITING_FOR_PLAYERS;
		}
	}
	
	public boolean isBettingRound() {
		return  currentState == State.PRE_FLOP ||
				currentState == State.FLOP ||
				currentState == State.TURN ||
				currentState == State.RIVER;
	}
	
	public void resetRound() {
        currentState = State.PRE_FLOP;
        currentBet = 0;  // Reset the bet for each round
        pot = 0;
    }

    public void setCurrentBet(int bet) {
        currentBet = bet;
    }

    public int getCurrentBet() {
        return currentBet;
    }
    
    public int getPot() {
    	return pot;
    }
    
    public void addToPot(int bet) {
    	pot += bet;
    }
    
    public void setPot(int bet) {
    	pot = bet;
    }
	
}
