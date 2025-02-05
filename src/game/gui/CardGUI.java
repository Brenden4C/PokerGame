package game.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class CardGUI {

	private Image cardImage;
	private int x, y;
	
	public CardGUI(String imagePath, int x, int y) {
		this.x = x;
		this.y = y;
		this.cardImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
	}
	
	public void draw(Graphics g) {
		if(cardImage != null) {
			g.drawImage(cardImage, x, y, 100, 140, null); //TODO: ADJUST SIZE LATER
		}
	}
	
}
