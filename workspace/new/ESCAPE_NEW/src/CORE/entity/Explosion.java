package CORE.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import GUI.Animation;
import audio.AudioPlayer;

public class Explosion {

	//posizione e posizione sulla mappa
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	//dimensioni
	private int width;
	private int height;
	
	private Animation animation; 		//animazione
	private BufferedImage[] sprites;	//immagine dell'animazione
	private AudioPlayer sfx;			//suono
	
	private boolean remove;				//boolean per quando deve essere rimosso
	
	///////////////////////COSTRUTTORI/////////////////////
	
	public Explosion(int x, int y) {
		
		this.x = x;
		this.y = y;
		
		width = 60;
		height = 60;
		
		try {//carica l'immagine
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Other/explosion.gif"
				)
			);
			//inizializza il suono
			sfx = new AudioPlayer("/SFX/explode.mp3");
			//inizializza l'array di buffered image
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {	//estrapola le sottoimmagini
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DELLE ESPLOSIONI");
		
		}
		//inizializza le animazioni
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(140);
		//fa partire l'effetto sonoro
		sfx.play();
		
	}
	
	//////////////////////////////UPDATE/////////////////////////////////
	
	public void update() {
		animation.update();						//lancia l'update dell'animazione
		if(animation.isPlayedOnce()) {			//se l'animazione è stata riprodotta una volta
			remove = true;						//eliminala
		}
	}
	
	
	
	//////////////////////////////GETTER & SETTER////////////////////////
	
	
	public boolean shouldRemove() { return remove; }
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	///////////////////////////////RENDER//////////////////////////////
	
	
	public void draw(Graphics2D g) {
		
		g.drawImage(
			animation.getImage(),
			x + xmap - width / 2,
			y + ymap - height / 2,
			null
		);
	}
	
	
}
