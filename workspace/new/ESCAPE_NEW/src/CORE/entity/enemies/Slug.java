package CORE.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CORE.World.TileMap;
import CORE.entity.Player;
import GUI.Animation;

public class Slug extends Enemy{	

	private BufferedImage[] sprites;
	
	
	public Slug(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 1.1;		//accelerazione ad ogni tick 
		maxSpeed = 2.2;			//velocità massima
		fallSpeed = 0.4;		//accelerazione di caduta (gravità)
		maxFallSpeed = 20.0;	//velocità limite di caduta
		
		width = 60;			//dimensioni sprite
		height = 60;
		cwidth = 30;		//dimensione hitbox
		cheight = 30;
		
		health = maxHealth = 2;		//vita
		damage = 1;					//danno
		
		// carica gli sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/robot_60.gif"
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

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL NEMICO SLUG");
		}
		//inizializza l'animazione
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(90);
		//di default si dirige a destra
		if(System.nanoTime() % 2 ==0){
			right = facingRight = true;
		}else{
			right = facingRight = false;
			left = true;
		}
		
		
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
		}	if(!notOnScreen()){ //cambia la direzione a seconda delle interazioni con il player
			if(isFacingRight()&&Math.abs(player.getY()-y)<cheight&&(x - player.getX()) <= 200){
				if(x > player.getX()+60 &&bottomLeft){
					left = true;
				right = facingRight = false;
				}
			}else
				if(!isFacingRight()&&Math.abs(player.getY()-y)<cheight&&(player.getX()-x) <= 200)
					if(x < player.getX()-60&&bottomRight){
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
