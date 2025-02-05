package game;

import game.main.*;
import game.gui.*;
import game.net.*;

public class MainGame {
	
	
	private static String serverIP = "35.188.99.241";
	private static int port = 12345;
	private static NetworkController net;
	private static NetworkManager netManager;
	
	public static void main(String[] args) {
		StartGame();
	}
	
	private static void StartGame() {
		
		net = new NetworkController(serverIP, port);
		net.connect();
		GUIController gui = new GUIController(net);
		net.setGUIController(gui);
	}
	
}
