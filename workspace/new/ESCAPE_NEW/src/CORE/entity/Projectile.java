package CORE.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CORE.World.TileMap;
import GUI.Animation;

public class Projectile extends MapObject{

	private boolean hit;   					//il proiettile ha colpito qualcosa
	private boolean remove;					//il proiettile va rimosso dalla mappa
	private BufferedImage[] sprites;		//l'Array delle immagine dell'animazione del proiettile
	
	
	public Projectile(TileMap map, boolean rigth) {
		
		super(map);
		
		this.facingRight= rigth;
	
		//setta la velocità e lo muove verso destra o sinistra.
		moveSpeed = 7.6;
		if(rigth) dx = moveSpeed;
		else dx = -moveSpeed;
		
		//Dimensioni dello sprite e dell'hit box
		width = 60;
		height = 60;
		cwidth = 30;
		cheight = 30;
		
		//Caricamento dell'immagine;
	
		try{
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Other/fireball.gif"));
			
			sprites = new BufferedImage[20];
			//si prende le varie sottoimaggini
			for(int i=0; i< sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(
						i*width,
						0,
						width, 
						height);
			}
			
			//inizializza l'animazione;
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(40);
			
		}catch (Exception e) {
			

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL PROIETTILE");
			e.printStackTrace();
			System.err.println("ERRORE NEL CARICAMENTO DEL PROIETTILE");
		}
		
	}
	
	
	public void setHit(){
		if(hit) return;
		hit = true;
		dx = 0;
	}
	
	public boolean shouldRemove(){
		return remove;
	}
	
	public void update(){
		//verifica le collissioni e aggiorna la posizione
		checkTileMapCollision();
		setPosition(getXtemp(), getYtemp());
		
	
		if(dx == 0 && !hit) //se colpisce una casella prima di colpire un nemico si ferma
			setHit();
		
		animation.update(); //aggiorna l'animazione
			
		if(hit || animation.isPlayedOnce()){ //se colpisce o ha finito l'animazione
			remove = true;					// rimuove il proiettile
		}
	}
	
	public void draw(Graphics2D g){
		
		super.draw(g);
				
	}

}
