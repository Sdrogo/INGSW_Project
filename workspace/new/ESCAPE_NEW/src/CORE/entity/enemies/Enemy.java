package CORE.entity.enemies;

import java.awt.Graphics2D;

import CORE.World.TileMap;
import CORE.entity.MapObject;
import CORE.entity.Player;

public class Enemy extends MapObject {

	//attributi dei nemici
	
	protected int health;				//vita del nemico
	protected int maxHealth;			//vita massima del nemico
	protected boolean dead;				//il nemico e vivo o morto?
	protected int damage;				////danno del nemico
	
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	///////////////////////////GETTERS////////////////////////
	
	public boolean isDead() { return dead; }
	public boolean isFacingRight(){return facingRight;}
	public int getDamage() { return damage; }

	//////////////////UPDATE//////////////////
	protected void getNextPosition() {//fa avanzare il mob
	
	//incrementa la velocità dell'accelerazione del mob
	if(left) dx = -moveSpeed; 
	else if(right) dx = moveSpeed;
	else dx = 0; 
	if(falling) { //se cade accellera fino alla massima velocità di caduta
		dy += fallSpeed;
		if(dy > maxFallSpeed) dy = maxFallSpeed;
	}
}
	
	public void switchFacing(){
		getNextPosition();
		checkTileMapCollision();
		
		if(facingRight){
		calculateCorners(xdest-1, y);
		if(topRight)return;
		else{
			right = facingRight = false;
			left= true;
		}
			
		}
		if(left){
			calculateCorners(xdest+1, y);
			if(topLeft)return;
			else{
			right = facingRight = true;
			left= false;
			}
		}
		
	}
	////////////////////////
	
	
	public void hit(int damage) {
		if(dead || isRecovering()) return;				//se è morto o in fase di recupero non subbisce danni
		health -= damage;	//subisce il danno	
		
		if(health < 0) health = 0;				//se la vita è <= a zero muore
		if(health == 0) dead = true;
		setRecover(true);							//se viene colpito va in fase di recupero
		setRecoverTimer(System.nanoTime());
	}

	public void draw(Graphics2D g){
		
		if(notOnScreen())	//renderizza il mob solo se è all'interno dello schermo
			return;
		else
			super.draw(g);
	}
	public void update(Player player) {}
	
	
	
}














