package CORE.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CORE.World.TileMap;
import CORE.entity.enemies.Enemy;
import GUI.Animation;
import audio.AudioPlayer;

public class Player extends MapObject{

	//////////////Caratteristiche del Player///////////////
	
	private int health;       			//vita attuale
	private int maxHealth;			   //vita massima;
	private double mana;       		  //
	private int maxMana;			 //
	private boolean dead;          //se il Player è morto
	
	// Attacchi a distanza
	
	private boolean shooting;
	private int shootDamage;
	//proiettili
	private ArrayList<Projectile> projectiles;
	
	private HashMap<String, AudioPlayer> sfx;
	
	// attacchia a contatto
	private boolean attacking;
	private int attackDamage;
	private int attackRange;
	
	private boolean moving;
	////////////////////////////////////ANIMAZIONI/////////////////////////////
	
	
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {//l'array contiene il numero di frame di ogni a
			6, 8, 6, 3, 5, 9	
	};
	
	/////Enum per indicizzare le varie animazioni
	
	private static final int INACTIVE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int ATTACKING = 4;
	private static final int SHOOTING = 5;
	
	
	/////////////////COSTRUTTORE //////////////////////////
	
	
public Player(TileMap tm) {
		
		super(tm);

		moving = false;
		
		/////setta le dimensioni/////
		width = 80;
		height = 80;
		cwidth = 30;
		cheight = 60;

		////////settaggi della fisica di gioco///////
		moveSpeed = 0.6;
		maxSpeed = 3.2;
		stopSpeed = 0.8;
		fallSpeed = 0.3;
		maxFallSpeed = 8.0;
		jumpStart = -9.6;
		stopJumpSpeed = 0.6;
		
		facingRight = true;
		
		/////////setta le carateristiche del Player//////
		
		health = maxHealth = 5;
		mana = maxMana = 5;
		
		projectiles = new ArrayList<Projectile>();
		
		shootDamage = 5;
		
		attackDamage = 2;
		attackRange = 60;
		
		recoverTimer = 100000000;

		//carica gli sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/player2.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 6; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
				
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
				}
				sprites.add(bi);
			}	
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DEL PLAYER");
		}
		
		animation = new Animation();
		currentAction = INACTIVE;
		animation.setFrames(sprites.get(INACTIVE));
		animation.setDelay(100);
		
		sfx = new HashMap<String,AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/playerjump.mp3"));
		sfx.put("punch",new AudioPlayer( "/SFX/playerattack.mp3"));
		sfx.put("projectile", new AudioPlayer("/SFX/playercharge.mp3"));
		sfx.put("hit", new AudioPlayer("/SFX/playerhit.mp3"));
		
		
	}

///////////////////////GETTERS///////////////////////////////

	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public double getMana() { return mana; }
	public int getMaxMana() { return maxMana; }
	public boolean isFacingRight(){return facingRight;}
	public boolean isMoving() {	return moving;	}
	public boolean canShoot(){return mana >= 1;}
	public boolean isDead(){ return dead;}
	public boolean isOutOfScreen(){return playerOutOfScreen;}
	public boolean isAttacking() {return attacking;}

	public boolean isShooting() {
		return shooting;
	}
	public int getAttackRange() {
			return attackRange;
		}	
	public int getAttackDamage() {
			return attackDamage;
		}

	
////////////////////////SETTER//////////////////////////////
	
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setShooting() { 
		shooting = true;
	}
	public void setAttacking() {
		attacking = true;
	}
	public void setDead(){
		dead= true;
	}
	
	////////////////UPDATE///////////////
	
	public void update() {

		for(int i = 0; i<projectiles.size(); i++){
			projectiles.get(i).update();
			if(projectiles.get(i).shouldRemove())
				projectiles.remove(i);
				
		}
		
		if(isRecovering()) {
			long elapsed =
				(System.nanoTime() - getRecoverTimer()) / 1000000;
			if(elapsed > 400) {
				setRecover(false);
			}
		}
			
		if(playerOutOfScreen) dead = true;
		
		///aggiorna la posizione
		getNextPosition();
		checkTileMapCollision();//controlla le collisioni con le caselle
		setPosition(getXtemp(), getYtemp());//calcola quale sarà la prossima posizione

		if(currentAction == ATTACKING) //azzera l'aminazione dell'attacco se è già stato visualizzato una volta
			if(animation.isPlayedOnce()) attacking = false;

		if(currentAction == SHOOTING){
			if(animation.isPlayedOnce()) shooting= false;
			
								
			}
				mana += 0.0033; //ricarica la capacità di fuoco
				
				//attacco del proiettile
				if(mana> maxMana) mana = maxMana; //non può essere maggiore del massimo
				if(shooting && currentAction != SHOOTING){ //se si spara
					if(mana >= 1){ //se si ha abbastanza proiettili
						mana -= 1;//scala il proiettile
						Projectile b = new Projectile(tileMap, facingRight); //inizializza il proiettile
						b.setPosition(x, y); //ne setta la posizione di partenza
						projectiles.add(b); //lo aggiunge nell'array list di proiettili
				}
					
		}	
				
			// verifica il tempo di recuper dopo un colpo
				if(isRecovering()) {
					long elapsed =
						(System.nanoTime() - getRecoverTimer()) / 1000000;
					if(elapsed > 400) {
						setRecover(false);
					}
				}
		
		//Setta le animazioni
		if(attacking) {
			if(currentAction != ATTACKING) {
				moving=false;
				sfx.get("punch").play();
				currentAction = ATTACKING;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(50);
				
			}
		}
		else if(shooting) {
			if(currentAction != SHOOTING) {
				sfx.get("projectile").play();
				moving = false;
				currentAction = SHOOTING;
				animation.setFrames(sprites.get(SHOOTING));
				animation.setDelay(20);
				
			}
		}
		else if(dy > 0) {
			
			if(currentAction != FALLING) {
				if(left||right) moving = true;
					else moving = false;
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(60);
				
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				moving = true;
				sfx.get("jump").play();
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(100);
				
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				moving = true;
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(80);
				
			}
		}
		else {
			if(currentAction != INACTIVE) {
				moving = false;
				currentAction = INACTIVE;
				animation.setFrames(sprites.get(INACTIVE));
				animation.setDelay(150);
				
			}
			
			if(health <= 0) 
				dead = true;
		}
		
		animation.update();  //aggiorna l'animazione
		
		//setta direzione
		if(currentAction != ATTACKING && currentAction != SHOOTING) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	private void getNextPosition() {    //calcola la posizione successiva
		
		//movimento
		if(left) { //movimento verso sinistra
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {  //movimento verso destra
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) { //decellera quando ci si ferma, da destra e sinistra
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// non ci si può muovere mentre si attacca se non in aria
		if(
		(currentAction == ATTACKING || currentAction == SHOOTING) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// salto
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;	
			cheight = 30;
		}
		
		// caduta
		if(falling) {
			
			dy += fallSpeed;  
			
			if(dy > 0) {jumping = false;
				cheight = 60;
			}
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// scorre la lista dei nemici
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i); //crea una variabile di comodo
			
			// Attacco Pugno
			if(attacking) {
				if(facingRight) {//se siamo rivolti a destra
					if(
						e.getX() > x &&						//alla nostra destra
						e.getX() < x + attackRange && 		//enrto il nostro raggio d'azione
						e.getY() > y - height / 2 &&		// se troppo in alto
						e.getY() < y + height / 2			 //o troppo in basso
					) {
						e.hit(attackDamage);
						if(e.isFacingRight()) e.switchFacing();
						//allora fai il danno
						sfx.get("hit").play();
					}
				}
				else {
					if(										//se invece siamo rivolti a sinistra
						e.getX() < x &&						//controlla i nemici alla nostra sinistra
						e.getX() > x - attackRange &&
						e.getY() > y - height / 2 &&
						e.getY() < y + height / 2
					) {
						e.hit( attackDamage);
						sfx.get("hit").play();
					}
				}
			}
			
			// verifica se il proiettile colpisce un nemico
			for(int j = 0; j < projectiles.size(); j++) {
				if(projectiles.get(j).intersects(e)) {
					e.hit(shootDamage);		
					sfx.get("hit").play();
					projectiles.get(j).setHit();
					break;
				}
			}
			
			// controlla le collisioni con i nemici
			if(intersects(e)) {
				if(recover) return;
				if(!isRecovering())hit(e.getDamage());
				
				sfx.get("hit").play();
				setRecover(true);
			}
			
		}
		
	}
	
	public void hit(int damage) {  //subisce i danni se si verifica una connessione
		if(isRecovering()) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		setRecover(true);
		setRecoverTimer (System.nanoTime());
	}
	
	public void reloadMana(){
		mana+=1;
		if(mana > maxMana)
			mana = maxMana;
		else return;
	}

//////////////////////////RENDERING///////////////////////////////
	
	
	public void draw(Graphics2D g) {
	 
	 //renderizza i proiettili
	 
	 for(int i=0; i< projectiles.size(); i++){
		 projectiles.get(i).draw(g);
	 }
	 
	 // renderizza il player
	 super.draw(g);
	 
	}

	
}