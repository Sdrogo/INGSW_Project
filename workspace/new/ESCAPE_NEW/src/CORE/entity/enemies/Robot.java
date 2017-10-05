package CORE.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CORE.World.TileMap;
import CORE.entity.Player;
import GUI.Animation;

public class Robot extends Enemy{

	//array di buffered image contente gli sprites dell'animazione
	private BufferedImage[] sprites;
	
	/////////////////////////////COTRUTTORE///////////////////////////
	
	public Robot(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 1.4;  //accelerazione ad ogni tick 
		maxSpeed = 8.0; //velocità massima
		fallSpeed = 1.4;	//velocità di caduta
		maxFallSpeed = 20.0;	//massiam velocità di caduta
		
		width = 120; //dimensioni sprites
		height = 120;
		cwidth = 90; //dimensioni hitbox
		cheight = 70;
		
		health = maxHealth = 25; //vita e vita massima
		damage = 2;	//danno inflitto
		
		// carica gli sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/robot_120.gif"
				)
			);
			//ne estrapola le sottoimmagini
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

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL ROBOT");
		}
		//inizializza l'animazione, gli passa gli sprites da animare ed imposta la velovità di animazione
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(180);
		
		right = true;  //di default parte rivolto a destra
		facingRight = true;
		
	}

	
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
		if(!notOnScreen()){ //cambia la direzione a seconda delle interazioni con il player
			if(isFacingRight()&&(x - player.getX())<=300){
				if(x > player.getX()+120 &&bottomLeft){
					left = true;
				right = facingRight = false;
				}
			}else
				if(!isFacingRight()&&(player.getX()-x)<= 300+width)
					if(x < player.getX()-120&&bottomRight){
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
