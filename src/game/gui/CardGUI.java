package game.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public abstract class CardGUI {

	private Image cardImage;
	private int x, y;
	
	public CardGUI(String imagePath, int x, int y) {
		this.x = x;
		this.y = y;
		this.setCardImage(new ImageIcon(getClass().getResource(imagePath)).getImage());
	}
	
	abstract void draw(Graphics g);
	abstract void draw(Graphics g, int x);
	
	
	public void setX(int newX) {
		x = newX;
	}
	
	public void incX(int incX) {
		x += incX;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setY(int newY) {
		y = newY;
	}
	
	public void incY(int incY) {
		y += incY;
	}
	
	public int getY() {
		return this.y;
	}

	public Image getCardImage() {
		return cardImage;
	}

	public void setCardImage(Image cardImage) {
		this.cardImage = cardImage;
	}

	

}
