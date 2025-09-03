package game.gui;

import java.awt.Graphics;

public class PlayerCardGUI extends CardGUI{

	public PlayerCardGUI(String imagePath, int x, int y) {
		super(imagePath, x, y);
	}

	@Override
	void draw(Graphics g) {
		if(getCardImage() != null) {
			g.drawImage(getCardImage(), getX(), getY(), 50, 70, null); //TODO: ADJUST SIZE LATER
		}
	}
	
	public void draw(Graphics g, int x) {
		this.setX( x );
		draw(g);
	}

}
