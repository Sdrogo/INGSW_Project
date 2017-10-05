package GUI;


import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import CORE.entity.Player;

public class HUD {

	private Player player;
	
	private BufferedImage image;
	private Font font;
	
	public HUD(Player p){
		player = p;
		try{
			image = ImageIO.read(
					getClass().getResourceAsStream("/HUD/hud2.gif")
					);
			font = new Font("Arial", Font.PLAIN, 28);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERRORE NEL CARICAMENTO DEL HUD");
		}	
	}
	
	public void draw(Graphics2D g){
		g.drawImage(image, 0, 20, null);
		g.setFont(font);

		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 60, 50);

		g.drawString((int)player.getMana() + "/" + player.getMaxMana(), 60, 90);
	}
	
	
}
