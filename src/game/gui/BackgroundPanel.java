package game.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private ArrayList<CardGUI> communityCards;
    private PlayerGUI playerGUI;
    private List<PlayerGUI> otherPlayers = new ArrayList<PlayerGUI>();

    public BackgroundPanel(String imagePath) {
        // Load the image from the classpath
        URL imageURL = getClass().getResource(imagePath);
        if (imageURL != null) {
            try {
                backgroundImage = ImageIO.read(imageURL);
            } catch (IOException e) {
                System.err.println("Error loading background image.");
                e.printStackTrace();
            }
        } else {
            System.err.println("Image not found: " + imagePath);
        }
        
        setLayout(null);
        playerGUI = new PlayerGUI();
        playerGUI.setBounds(100, 100, 100, 100);
        this.add(playerGUI);
        communityCards = new ArrayList<>();
    }

    protected void addCommunityCard(CardGUI card) {
    	communityCards.add(card);
		
	}

	@Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the image to cover the entire panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        for(CardGUI card : communityCards) {
        	card.draw(g);
        }
    }
	
	public void updateCommunityCards(ArrayList<CardGUI> newCards) {
		communityCards = newCards;
		repaint();
	}
	
	public ArrayList<CardGUI> getCommunityCards() {
		return this.communityCards;
	}
	
	public void resetCommunityCards() {
		this.communityCards = new ArrayList<>();
		repaint();
	}
}
