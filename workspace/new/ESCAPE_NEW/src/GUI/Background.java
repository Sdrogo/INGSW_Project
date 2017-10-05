package GUI;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Background {
	
	private BufferedImage image;			//immagine di background
	
	private double x;						//posizione dei background
	private double y;
	private double dx;						//velocità del background
	private double dy;
	
	private int width;						//dimensioni
	private int height;

	
	//////////////////////COSTRUTTORI//////////////////
	
	public Background(String s) {
							//il background viene messo al livello 0.1
		this(s, 0.1);
	}
	
	public Background(String s, double d) {	//costruttore che specifica la profondità del background
		try {							//carica l'immagine
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			//setta  le dimensioni
			width = image.getWidth();
			height = image.getHeight();
		
		}
		catch(Exception e) {

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL BG");
		}
	//richiama quello scalato ma con le stesse dimensioni
	}

	
	/////////////////////////SETTER/////////////////////////////
	
	
	public void setPosition(double x, double y) {
		this.x = x  % width;
		this.y = y % height;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setDimensions(int i1, int i2) {
		width = i1;
		height = i2;
	}
	
	/////////////////GETTER/////////////////////////////
	
	public double getx() { return x; }
	public double gety() { return y; }
	
	//////////////////UPDATE///////////////////////////
	
	public void update() {
		x += dx;
		while(x <= -width) x += width;
		while(x >= width) x -= width;
		y += dy;
		while(y <= -height) y += height;
		while(y >= height) y -= height;
	}
	
	////////////////////////RENDER///////////////////////
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, null);
		
		if(x < 0) {
			g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
		}
		if(x > 0) {
			g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
		if(y < 0) {
			g.drawImage(image, (int)x, (int)y + GamePanel.HEIGHT, null);
		}
		if(y > 0) {
			g.drawImage(image, (int)x, (int)y - GamePanel.HEIGHT, null);
		}
	}
}



