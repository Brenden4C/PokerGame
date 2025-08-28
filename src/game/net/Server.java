package game.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import game.main.Player;
import game.main.Deck;
import game.main.Card;
import game.main.GameState;

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
    	
    	deck.reset();
    	gameState = new GameState(); //reset the gamestate
    	activePlayers = players.size();
    	
    	for(ServerPlayer player : players) {
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
    
    private static void handleShowdown() {
		// TODO Auto-generated method stub
		
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
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                clients.add(out);

                // First message from client should be the username
                username = in.readLine();
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
