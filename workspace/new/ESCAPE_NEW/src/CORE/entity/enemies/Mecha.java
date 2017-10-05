package CORE.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CORE.World.TileMap;
import CORE.entity.Player;
import GUI.Animation;

public class Mecha extends Enemy{

	private BufferedImage[] sprites;
	
	//////////////////////////////////////COSTRUTTORE///////////////////////////////////////////
	
	public Mecha(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 1.6;		//accelerazione ad ogni tick 
		maxSpeed = 10.6;			//velocità massima
		fallSpeed = 0.4;		//accelerazione di caduta (gravità)
		maxFallSpeed = 20.0;	//velocità limite di caduta
		
		width = 80;			//dimensioni sprite
		height = 80;
		cwidth = 40;		//dimensione hitbox
		cheight = 60;
		
		health = maxHealth = 4;		//vita
		damage = 1;					//danno
		
		// carica gli sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/mecha.gif"
				)
			);
			//estrapola le sottoimmagini
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL NEMICO MECHA");
		}
		//inizializza l'animazione
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(150);
		//di default si dirige a destra
		if(System.nanoTime() % 2 ==0){
			right = facingRight = true;
		}else{
			right = facingRight = false;
			left = true;
		}
		
		
	}
	
	
	/////////////////////////UPDATE//////////////////////////////////////
	
	public void update(Player player) {
				
		getNextPosition(); // controlla la posizione successiva
		checkTileMapCollision();	//controlla le collisioni con la mappa
		calculateCorners(x, ydest + 1);	//verifica  le collisioni con gli angoli sulla riga inferiore al mob 
		
		//verifica il tempo di recupero
		if(isRecovering()) {
			long elapsed =
				(System.nanoTime() - getRecoverTimer()) / 1000000;
			if(elapsed > 400) {
				setRecover(false);
			}
		}
		//spostamento in base alla posizione del player
		if(!notOnScreen()){ //fa il controllo solo se attualmente rientra nello schermo
			
			
			if(isFacingRight()&&y==player.getY()&&(x - player.getX()) <= 400){	//verifa la direzione e la posizione rispetto al player
				if(x > player.getX() + 60 && bottomLeft){ 						//e inverte la posizione, si ripete per destra
					left = true;
					right = facingRight = false;
				}
			}else
				if(!isFacingRight()&&y==player.getY()&&(player.getX()-x) <= 400)	//e sinistra
					if(x < player.getX() - 60 && bottomRight){
						left = false;
						right = facingRight = true;
					}		
		}
		
		//se la casella sotto di noi è vuora e non ci sono collisioni al nostro livello:
		if(!bottomLeft) {	//se andava a destra si gira a destra
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) { //se andava a destra si gira a sinistra
			left = true;
			right = facingRight = false;
		}
		setPosition(getXtemp(), getYtemp()); //aggiorna la posizione
		
		if(dx == 0) {  //se si verifica una collisione cambia il verso
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		// update dell'animazione
		animation.update();
				
	}	
	public void draw(Graphics2D g) {
	
		setMapPosition();
		
		//chiama il draw della superclasse
		super.draw(g);
		
	}
	
}