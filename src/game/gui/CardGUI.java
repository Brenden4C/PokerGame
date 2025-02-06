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
}
