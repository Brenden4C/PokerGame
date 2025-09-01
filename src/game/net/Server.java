package game.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import game.main.Player;
import game.main.Deck;
import game.main.Card;
import game.main.GameState;
import game.main.HandEvaluator;

public class Server {
    private static final int PORT = 12345;
    private static List<PrintWriter> clients = new ArrayList<>();
    private static boolean running = true;
    private static List<ServerPlayer> players = new ArrayList<>();
    private static List<String> playerNames = new ArrayList<>();
    
    private static Deck deck = new Deck();
    private static GameState gameState = new GameState();
    private static int activePlayers = 0;
    private static int playersTurn = 0;
    private static String activePlayer = "";
    private static List<Card> communityCards = new ArrayList<>();
    

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            //Allows me to stop the server by typing "STOP" in console.
            startStopListener();
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to listen for "STOP" in a separate thread
    private static void startStopListener() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (running) {
                String command = scanner.nextLine();
                if ("STOP".equalsIgnoreCase(command)) {
                    System.out.println("Stopping server...");
                    running = false;
                    shutdownServer();
                }
                if("DEAL".equalsIgnoreCase(command)) {
                	System.out.println("Dealing community card");
                	dealCommunityCard();
                }
            }
            scanner.close();
        }).start();
    }
    
    //Method to shutdown the server
    private static void shutdownServer() {
        try {
            for (PrintWriter client : clients) {
                client.println("Server is shutting down...");
                client.close();
            }
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error during server shutdown.");
        }
    }
 
    private static void startNewRound() {
    	if(players.size() < 2) {
    		System.out.println("Not enough players to start a round");
    		return;
    	}
    	
    	playersTurn = 0;
    	
    	System.out.println("Starting a new round...");
    	
    	broadcastMessage("NEWROUND");
    	deck.reset();
    	gameState = new GameState(); //reset the gamestate
    	activePlayers = players.size();
    	communityCards.clear();
    	
    	for(ServerPlayer player : players) {
    		player.resetHand();
    		dealHoleCards(player);
    	}
    	
    	transitionToNextPhase();
    	broadcastGameState();
    }
    
    
    // Transition to the next phase
    private static void transitionToNextPhase() {
        gameState.nextPhase(); //Increment to next phase

        switch(gameState.getCurrentState()){
        case PRE_FLOP: handlePreFlop(); break;
        case FLOP: handleFlop(); break;
        case TURN: handleTurn(); break;
        case RIVER: handleRiver(); break;
        case SHOWDOWN: handleShowdown(); break;
        }
    }
    
    //Uses game logic to take players cards that aren't folded to decide who has the best hand
    //it will then send a message to each user about who won the hand and then will reset the board
    private static void handleShowdown() {
		ArrayList<List<Card>> activePlayerHands = new ArrayList<List<Card>>();
		ArrayList<ServerPlayer> activePlayers = new ArrayList<ServerPlayer>();
		int i = 0;
		//Loop through each player in the game
		while(i < players.size()) {
			//If player is not folded get their cards and add them to the active player hands
			if(!players.get(i).isFolded()) {
				
				activePlayerHands.add(players.get(i).getHand());
				activePlayers.add(players.get(i));
			}
			i++;
		}
		i = 0;
		//Add the community cards to each players hands to each hand for evaluation
		while(i < activePlayerHands.size()) {
			activePlayerHands.get(i).addAll(communityCards);
			i++;
		}
		
		//We now have the players hands, we evaluate a winner here
		int winningIndex = 0;
	    List<Card> bestHand = activePlayerHands.get(0);

	    for (i = 1; i < activePlayerHands.size(); i++) {
	        int result = HandEvaluator.compareHands(bestHand, activePlayerHands.get(i));
	        if (result == 2) { // Player i beats current best
	            winningIndex = i;
	            bestHand = activePlayerHands.get(i);
	        } else if (result == 0) {
	            // Tie handling → keep track of multiple winners if needed
	            System.out.println("Tie detected between " + activePlayers.get(winningIndex).getUsername()
	                               + " and " + activePlayers.get(i).getUsername());
	        }
	    }

	    // 3. Announce winner
	    ServerPlayer winner = activePlayers.get(winningIndex);
	    System.out.println("Winner: " + winner.getUsername() 
	                       + " with " + HandEvaluator.bestHand(bestHand));
	    broadcastMessage("WINNER: " + winner.getUsername() + " with " + HandEvaluator.bestHand(bestHand));
	    
	    try {
            // Pause for 3000 milliseconds (3 seconds)
	    	System.out.println("Waiting for next round to begin");
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            // Handle the case where the thread is interrupted while sleeping
            e.printStackTrace();
        }
		
	    startNewRound();
	}

	private static void handleRiver() {
		dealCommunityCard();
	}

	private static void handleTurn() {
		dealCommunityCard();	
	}

	private static void handleFlop() {
		dealCommunityCard();
		dealCommunityCard();
		dealCommunityCard();
	}

	private static void handlePreFlop() {
		// TODO Auto-generated method stub
		
	}
	
	private static void handlePlayerAction(ServerPlayer player, String action) {
		
		if(!players.get(playersTurn).equals(player)) {
			System.out.println("Its not" + player.getUsername() + "'s turn!");
			return;
		}
		
		if (action.startsWith("BET")) {
	        int amount = Integer.parseInt(action.split(" ")[1]);
	        player.bet(amount);
	        gameState.setCurrentBet(amount);

	        // A bet means other players get another chance to act
	        setAllPlayersNotPlayed();
	        player.setHasPlayed(true);
	    } else if (action.equals("CHECK")) {
	        player.setHasPlayed(true);
	    } else if (action.equals("FOLD")) {
	        activePlayers--;
	        player.fold();
	        player.setHasPlayed(true);
	    }
		
	    // Move to next player's turn
	    do {
	        playersTurn = (playersTurn + 1) % players.size();
	    } while (players.get(playersTurn).isFolded());

	    updateActivePlayer();
	    
	    
	    // Check if all players have completed the round
	    if (allPlayersPlayed()) {
	        setAllPlayersNotPlayed();  // Reset play state for the next phase
	        transitionToNextPhase();   // Move to the next game phase
	    }
		
	}
	
	//Sends to the clients the current game state.
    private static void broadcastGameState() {
		String message = "GAME_STATE: " + gameState.getCurrentState().name();
		broadcastMessage(message);		
	}

    //Sends to the clients the given message as a String.
	private static void broadcastMessage(String message) {
		for(PrintWriter client : clients)
			client.println(message);
	}
	
	// === Add a Player to the List ===
    public static synchronized void addPlayer(String playerName) {
        playerNames.add(playerName);
        broadcastPlayerList();
    }

    // === Remove a Player from the List ===
    public static synchronized void removePlayer(String playerName) {
        playerNames.remove(playerName);
        broadcastPlayerList();
    }

    // === Broadcast Updated Player List to All Clients ===
    private static void broadcastPlayerList() {
        String playerListMessage = "PLAYER_LIST:" + String.join(",", playerNames) + ";ACTIVE:" + activePlayer;
        broadcastMessage(playerListMessage);
    }
    
    private static void updateActivePlayer() {
        activePlayer = players.get(playersTurn).getUsername(); // Get player's name
        broadcastPlayerList(); // Broadcast updated player list with active indicator
    }

	//Method to deal two cards to a player.
    private static void dealHoleCards(ServerPlayer player) {
    	//Get the players hand, empty it and add two new cards to it
    	player.resetHand();
    	player.getHand().add(deck.drawCard());
    	player.getHand().add(deck.drawCard());
    	
    	//Send the hole cards to the player
    	player.sendHoleCards();
    }
    
    //Deals a single card to the community cards.
    private static void dealCommunityCard() {
    	Card card = deck.drawCard();
    	communityCards.add(card);
    	
    	for(PrintWriter out : clients) {
    		out.println("COMMUNITY_CARD: " + card);
    	}
    }

    //Checks whether or not all the players in the game have played yet.
    private static boolean allPlayersPlayed() {
    	boolean allPlayed = true;
    	for(ServerPlayer p : players) {
    		if(p.hasPlayed() == false) {
    			allPlayed = false;
    			continue;
    		}
    	}
    	return allPlayed;
    }
    
    //Sets all players hasPlayed var to false
    private static void setAllPlayersNotPlayed() {
    	for(ServerPlayer p : players) {
    		p.setHasPlayed( false );
    	}
    }
    
    
    
    static class ClientHandler extends Thread {
    	private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ServerPlayer serverPlayer;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
            	if(players.size() >= 7) {
            		System.out.println("Socket denied, max size reached");
            		return; //TODO ADD QUEUE/SPECTATOR SYSTEM
            	}
            	
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                clients.add(out);

                // First message from client should be the username
                username = in.readLine(); //TODO: USER SHOULD NOT BE ABLE TO USE SAME USERNAME AS SOMEONE ELSE
                System.out.println(username + " has joined.");
                addPlayer(username);
                broadcastPlayerList();

                // Create a new player and assign them to ServerPlayer
                Player player = new Player(username);
                serverPlayer = new ServerPlayer(player, username, socket, in, out);
                players.add(serverPlayer);

                if(players.size() >= 2) {
                	startNewRound();
                }

                // Listen for messages
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(username + ": " + message);
                    if(message.startsWith("CHAT:")) {
                    	broadcastMessage("CHAT:" + username + ": " + message.substring(5));
                    }
                    handlePlayerAction(serverPlayer, message);
                }
            } catch (IOException e) {
                System.out.println(username + " disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                players.remove(serverPlayer);
                removePlayer(username);
                //TODO: IF PLAYER DISCONNECTS, THEY SHOULD BE AUTO FOLDED SO GAME CAN CONTINUE.
            }
        }
    }
    
    
    
}
