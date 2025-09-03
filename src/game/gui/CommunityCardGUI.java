package game.gui;

import java.awt.Graphics;

public class CommunityCardGUI extends CardGUI{

	public CommunityCardGUI(String imagePath, int x, int y) {
		super(imagePath, x, y);
	}

	@Override void draw(Graphics g) {
		if(getCardImage() != null) {
			g.drawImage(getCardImage(), getX(), getY(), 100, 140, null); //TODO: ADJUST SIZE LATER
		}
		
	}

	@Override
	void draw(Graphics g, int x) {
		this.setX(x);
		draw(g);		
	}

}
