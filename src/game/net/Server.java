package game.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import game.main.Player;
import game.main.Deck;
import game.main.Card;
import game.gui.*;

public class Server {
    private static final int PORT = 12345;
    private static final String USER_DATA_FILE = "users.txt";
    private static Map<Socket, String> userMap = new HashMap<>();
    private static List<PrintWriter> clients = new ArrayList<>();
    private static boolean running = true;
    
    private static Deck deck = new Deck();
    private static List<ServerPlayer> players = new ArrayList<>();
    

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
    
    //Method to deal two cards to a player.
    private static void dealHoleCards(ServerPlayer player) {
    	//Create a new list and add two cards from the deck to it.
    	List<Card> holeCards = new ArrayList<>();
    	holeCards.add(deck.drawCard());
    	holeCards.add(deck.drawCard());
    	
    	//Set the hole cards to the players instance
    	player.setHoleCards(holeCards);
    	
    	//Send the hole cards to the player
    	player.sendHoleCards();
    }
    
    private static void dealCommunityCard() {
    	
    }

    static class ClientHandler extends Thread {
    	private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ServerPlayer serverPlayer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // First message from client should be the username
                String username = in.readLine();
                System.out.println(username + " has joined.");

                // Create a new player and assign them to ServerPlayer
                Player player = new Player(username);
                serverPlayer = new ServerPlayer(player, username, socket, in, out);
                players.add(serverPlayer);

                // Deal hole cards
                dealHoleCards(serverPlayer);

                // Listen for messages
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(username + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Player disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                players.remove(serverPlayer);
            }
        }
    }
    
    
    
}
