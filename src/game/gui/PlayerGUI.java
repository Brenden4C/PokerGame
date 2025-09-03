package game.gui;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import game.main.Card;

public class PlayerGUI extends JPanel {

	private int balance;
	private String name;
	private List<CardGUI> cards;
	
	private boolean isOccupied;
	private boolean isFolded;
	private boolean isActive;
	
	private JLabel nameLabel;
	private JLabel balanceLabel;
	
	public PlayerGUI() {
		clearPlayer();
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 100));
		setOpaque(false);
		
		nameLabel = new JLabel(name);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(nameLabel, BorderLayout.CENTER);
		
		JPanel cardsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // draw cards if we have them
                if (cards != null) {
                    int x = 0;
                    for (CardGUI card : cards) {
                        card.draw(g, x);
                        x += 70; // offset for spacing
                    }
                }
            }
        };
        cardsPanel.setPreferredSize(new Dimension(150, 70));
        this.add(cardsPanel, BorderLayout.CENTER);
		
		balanceLabel = new JLabel("balance: ");
		balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(nameLabel, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCards(List<CardGUI> cards) {
		this.cards = cards;
	}	
	
	public void clearPlayer() {
		this.balance = 0;
		this.name = "";
		this.cards = new ArrayList<CardGUI>();
	}
	
}
