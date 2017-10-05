package CORE.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CORE.World.TileMap;
import GUI.Animation;

public class Teleport extends MapObject {
	
	private BufferedImage[] sprites;
	
	public Teleport(TileMap tm) {
		super(tm);
		width = height = 60;
		cwidth = 30;
		cheight = 60;
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Other/Teleport2.gif")
			);
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width, 0, width, height
				);
			}
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(25);
		}
		catch(Exception e) {
e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL TELETRASPORTO");
		}
	}
	
	public void update() {
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
