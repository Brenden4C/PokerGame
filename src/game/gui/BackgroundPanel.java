package game.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private ArrayList<CardGUI> communityCards;

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
        
        communityCards = new ArrayList<>();
        addCommunityCards(); //SAMPLE CARDS
    }

    private void addCommunityCards() {
    	// Sample card images - you should replace this with your actual logic to deal cards
        communityCards.add(new CardGUI("/cards/2_of_hearts.png", 500, 200));
        communityCards.add(new CardGUI("/cards/3_of_spades.png", 620, 200));
        communityCards.add(new CardGUI("/cards/4_of_clubs.png", 740, 200));
        communityCards.add(new CardGUI("/cards/5_of_diamonds.png", 860, 200));
        communityCards.add(new CardGUI("/cards/6_of_hearts.png", 980, 200));
		
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
}
