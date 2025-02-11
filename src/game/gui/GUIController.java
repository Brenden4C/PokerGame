package game.gui;

import javax.swing.*;

import game.net.NetworkController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIController {
    private NetworkController networkController;
    private JFrame frame;
    private BackgroundPanel bgPanel;
    private ChatPanel chatPanel;
    private List<CardGUI> playerHand = new ArrayList<CardGUI>();
    
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
        
        //Create the main panel that will hold the background image and the community cards
        bgPanel = new BackgroundPanel("/pokertable.jpg");
        chatPanel = new ChatPanel();
        
        //This label will display to the user how many chips they have.
        JLabel chipsLabel = new JLabel(username + " Chips: 1000"); // Assuming the player starts with 1000 chips TODO: MAKE PLAYERS CHIP COUNT GO HERE

        //Create the panel that will hold the label that shows the user the chips they have.
        JPanel statusPanel = new JPanel();
        statusPanel.add(chipsLabel);
        
        //Setup the panel that has the buttons the user will press to send action to the server
        //(Check, Raise, Fold, Bet, etc.)
        JPanel actionPanel = handleButtons(frame);        
        
        //Set the layout of the main frame and add the panels to the frame.
        frame.setLayout(new BorderLayout());
        frame.add(bgPanel, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.NORTH);
        frame.add(actionPanel, BorderLayout.SOUTH);
        frame.add(chatPanel, BorderLayout.EAST);
        
        //Set the frame to be visible for the client.
        frame.setVisible(true);
    }

    //Method creates and hands the use for the buttons that handle the player actions.
    //These actions include checking, raising, calling, folding, betting etc.
    private JPanel handleButtons(JFrame frame) {
    	
    	//Create panel for buttons and bet input
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());
    	
    	//Create a text field for the bet amount
        JTextField betAmountField = new JTextField(10);
    	
    	//Create buttons for actions
        JButton checkButton = new JButton("Check");
        JButton callButton = new JButton("Call");
        JButton foldButton = new JButton("Fold");
        JButton betButton = new JButton("Bet");

        //Add action listeners for the buttons
        checkButton.addActionListener(e -> sendActionToServer("check", 0));
        callButton.addActionListener(e -> sendActionToServer("call", 0));
        foldButton.addActionListener(e -> sendActionToServer("fold", 0));
        
        //Bet button needs to get the bet amount from the text field
        betButton.addActionListener(e -> {
            try {
                int betAmount = Integer.parseInt(betAmountField.getText());
                sendActionToServer("bet", betAmount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for the bet.");
            }
        });
    	
    	//Add buttons and text field to the panel
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
    
    //Takes in a list of CardGUI's (should be a 2 card list that represents the users hole cards) and then
    //creates a new card panel and adds the panel to the frame.
    public void displayHoleCards() {
    	// Create a custom panel for drawing the hole cards
        JPanel cardPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Draw each card at its respective position (you can adjust x, y based on the layout)
                int xPosition = 50;  // Starting X position for the first card
                for (CardGUI card : playerHand) {
                    card.setX(xPosition);
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

    //Takes in the card data from the server and then convers them into CardGUI objects for display,
    //then adds the cards to a list and sends them to the display method.
	public void addHoleCardsToGUI(String[] cardData) {
		
		CardGUI card1 = new CardGUI("/cards/" + cardData[0] + ".png", 0, 0);
		CardGUI card2 = new CardGUI("/cards/" + cardData[1] + ".png", 0, 0);
		
		playerHand.add(card1);
		playerHand.add(card2);
		
		displayHoleCards();
	}

	public void addCommunityCard(String cardData) {
		CardGUI card = new CardGUI("/cards/" + cardData + ".png", 0, 0);
		if(bgPanel.getCommunityCards().size() != 0)
			card.setX(bgPanel.getCommunityCards().get(bgPanel.getCommunityCards().size() - 1).getX() + 100);
		this.bgPanel.addCommunityCard(card);
		frame.revalidate();
    	frame.repaint();
	}

	public void resetHoleCards() {
		playerHand.clear();		
	}
	
	public void updatePlayerList(List<String> players, String activePlayer) {
        chatPanel.updatePlayerList(players, activePlayer);
    }
}
