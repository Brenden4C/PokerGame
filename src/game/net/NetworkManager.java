package game.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkManager {
    private static NetworkManager instance;
    private String serverIP;
    private int port;
    
    private NetworkController netController;

    private NetworkManager(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    // Singleton pattern to ensure only one instance of NetworkManager
    public static NetworkManager getInstance(String serverIP, int port) {
        if (instance == null) {
            instance = new NetworkManager(serverIP, port);
        }
        return instance;
    }

    // Create a new NetworkController for each player
    public NetworkController createNetworkController() {
    	//this.netController = new NetworkController(serverIP, port);
        return this.netController;
    }
    
    
}
