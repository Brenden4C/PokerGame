package game.gui;

import javax.swing.*;

import game.main.Card;
import game.net.NetworkController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIController {
    private NetworkController networkController;
    private JFrame frame;
    private ArrayList<CardGUI> playerCards = new ArrayList<>();
    
    public GUIController(NetworkController net) {
    	this.networkController = net;
    	setUpGUI();
    	}
    
    private void setUpGUI() {
    	
    	// Ask for username when the app starts
        String username = getUsername();
    	
    	// Set up the frame
        this.frame = new JFrame("Poker Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        
        BackgroundPanel bgPanel = new BackgroundPanel("/pokertable.jpg");
        
        // Create labels to show chips
        JLabel chipsLabel = new JLabel("Chips: 1000"); // Assuming the player starts with 1000 chips TODO: MAKE PLAYERS CHIP COUNT GO HERE

        JPanel actionPanel = handleButtons(frame);
       
        JPanel statusPanel = new JPanel();
        statusPanel.add(chipsLabel);
        
        // Add the action panel to the frame
        frame.setLayout(new BorderLayout());
        frame.add(bgPanel, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.NORTH);
        frame.add(actionPanel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }

    private JPanel handleButtons(JFrame frame) {
    	
    	// Create panel for buttons and bet input
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());
    	
    	// Create a text field for the bet amount
        JTextField betAmountField = new JTextField(10);
    	
    	// Create buttons for actions
        JButton checkButton = new JButton("Check");
        JButton callButton = new JButton("Call");
        JButton foldButton = new JButton("Fold");
        JButton betButton = new JButton("Bet");

        // Add action listeners for the buttons
        checkButton.addActionListener(e -> sendActionToServer("check", 0));
        callButton.addActionListener(e -> sendActionToServer("call", 0));
        foldButton.addActionListener(e -> sendActionToServer("fold", 0));
        
        // Bet button needs to get the bet amount from the text field
        betButton.addActionListener(e -> {
            try {
                int betAmount = Integer.parseInt(betAmountField.getText());
                sendActionToServer("bet", betAmount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for the bet.");
            }
        });
    	
    	// Add buttons and text field to the panel
        actionPanel.add(checkButton);
        actionPanel.add(callButton);
        actionPanel.add(foldButton);
        actionPanel.add(betButton);
        actionPanel.add(new JLabel("Bet Amount:"));
        actionPanel.add(betAmountField);
        
        return actionPanel;
    }
    
    //Send the players message to the server TODO: MIGHT GET RID OF LATER
    private void sendMessageToServer(String message) {
        if (networkController.getOut() != null) {
            networkController.getOut().println(message);
            System.out.println("Message sent to the server: " + message);
        }
    }
    
    
    // Send the player's action to the server (check, call, fold, bet)
    private void sendActionToServer(String action, int betAmount) {
        if (networkController.getOut() != null) {
            String message = action.toUpperCase();
            if (betAmount > 0) {
                message += " " + betAmount;
            }
            networkController.sendMessage(message);
            System.out.println("Action sent: " + message);
        }
    }
    
    private String getUsername() {
    	// Ask for username when the app starts
        String username = JOptionPane.showInputDialog("Enter your username:");
        
        //while the username is empty, 
        while(username == null || username.trim().isEmpty()) {
        	username = JOptionPane.showInputDialog("Enter your username, it cannot be blank:");
        }
        
        networkController.sendMessage("" + username);
        
        return username;
        
    }
    
    public void displayHoleCards(ArrayList<CardGUI> holeCards) {
    	// Create a custom panel for drawing the hole cards
        JPanel cardPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Draw each card at its respective position (you can adjust x, y based on the layout)
                int xPosition = 50;  // Starting X position for the first card
                for (CardGUI card : holeCards) {
                    card.draw(g);  // Call the draw method from the Card class to render the image
                    xPosition += 120;  // Adjust X position for the next card (you can tweak this value)
                }
            }
        };
        
        // Set the panel size based on the number of cards and space between them
        cardPanel.setPreferredSize(new Dimension(600, 150));  // Adjust the height and width to fit the cards

    	
    	frame.add(cardPanel, BorderLayout.SOUTH);
    	
    	frame.revalidate();
    	frame.repaint();
    	
    }

	public void addHoleCardsToGUI(List<Card> holeCards) {
		// TODO Auto-generated method stub
		
	}
}
