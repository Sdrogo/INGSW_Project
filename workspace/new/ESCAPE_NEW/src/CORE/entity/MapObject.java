package CORE.entity;


import java.awt.Graphics2D;
import java.awt.Rectangle;

import CORE.World.Tile;
import CORE.World.TileMap;
import GUI.Animation;
import GUI.GamePanel;

public abstract class MapObject {

	//tile map e altre correlate
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//posizione e vettore di movimento
	
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimensioni
	
	protected int width;
	protected int height;
	
	//box collisione;
	
	protected int cwidth;
	protected int cheight;
	
	//collisioni
	
	protected int currentRow; //su quale riga ci troviamo
	protected int currentCol; //su quale colonna ci troviamo
	protected double xdest;   //la nostra destinazione sulle x
	protected double ydest;	//e sulle y
	private double xtemp;	//posizioni temporanee
	private double ytemp;
	protected boolean topLeft;  //i quattro angoli del boz con cui si può collidere
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//animazioni :  od ogni diversa azione viene associato un intero, teniamo memoria 
	//azione corrente e di quella precedente e la direzione (se verso destra o sinistra)
	
	protected Animation animation; 
	protected int currentAction;
	//protected int previousAction;
	protected boolean facingRight;
	
		
	//boolean per i moviementi
	
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean jumping;
	protected boolean falling;
	protected boolean playerOutOfScreen;

	//attributi fisici
	
	protected double moveSpeed; //velocità di movimento
	protected double maxSpeed; //velocità massima
	protected double stopSpeed; //velocità di decellerazione
	protected double fallSpeed; //gravità;
	protected double maxFallSpeed; //velocità massima di caduta
	protected double jumpStart; //velocità iniziale del saldo
	protected double stopJumpSpeed; //per dare modularità al salto;
	protected boolean recover;
	protected long recoverTimer;

	protected long recoverCount;
	
///////////////////COSTRUTTORE///////////////////
	
	public MapObject(TileMap map){
		tileMap= map;
		tileSize = map.getTileSize();
	}
	
///////////////////////////UPDATE///////////////////////////
	
	public boolean intersects(MapObject o){
			
			//verifica se due oggetti collidono tra di loro;
			Rectangle r1 = this.getBounds();
			Rectangle r2 = o.getBounds();
			return r1.intersects(r2);
	}

	public void calculateCorners(double x, double y){
		//trova gli angoli
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		//controlla se l'oggetto è uscito dalla mappa.
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
				leftTile < 0 || rightTile >= tileMap.getNumCols()) {
				topLeft = topRight = bottomLeft = bottomRight = false;
				playerOutOfScreen = true;
				return;
			}
		
		int tl = tileMap.getType(topTile, leftTile);//angolo in alto a sinistra
		int tr = tileMap.getType(topTile, rightTile);//angolo in alto a destra
		int bl = tileMap.getType(bottomTile, leftTile);//angolo in basso a sinistra
		int br = tileMap.getType(bottomTile, rightTile);;//angolo in basso a destra
				
		topLeft = tl == Tile.BLOCKED;   	 //c'è un blocco solido in alto a sinistra
		topRight = tr == Tile.BLOCKED;		//c'è un blocco solido in alto a destra
		bottomLeft = bl == Tile.BLOCKED;	//c'è un blocco solido in basso a sinistra
		bottomRight = br == Tile.BLOCKED;	//c'è un blocco solido in basso a destra
        
	}
	
	public void checkTileMapCollision(){
		
		//verica le collissioni con le caselle del mondo
		
		//prima verifica la casella su cui ci troviamo in questo istante
		currentCol = (int) x / tileSize;
		currentRow = (int) y / tileSize;
		//poi vede qual'è la nostra destinazione
		xdest = x + dx;
		ydest = y + dy;
		
		//tiene memoria della vecchia posizione;
		setXtemp(x);
		setYtemp(y);
		
		//verifica le collisioni sui 4 angoli per le collisioni verticali
		calculateCorners(x, ydest);
		
		if(dy < 0){ //verifica se possiamo andare verso l'alto
			if(topLeft||topRight){
				dy = 0; //se colpiamo qualcosa con la parte top allora non possiamo più salire
				setYtemp(currentRow * tileSize + cheight / 2);
			}
			else{
				setYtemp(getYtemp() + dy); //altrimenti muoviti
			}
		}
		if(dy > 0) {//serifica se possiamo andare verso il  basso verso il basso
			if(bottomLeft || bottomRight){
				dy = 0;
				falling = false; //se abbiamo qualcosa sotto non stiamo più cadendo
				setYtemp((currentRow + 1 )*tileSize -cheight / 2);
			}
			else{
				setYtemp(getYtemp() + dy); //altrimenti muoviti
			}
		}
			//verifica i 4 angoli per le collisioni orizzontali
		calculateCorners(xdest, y);
		
		if(dx<0){ //verifica se è bloccata la strada a sinistra
			if(topLeft || bottomLeft){
				dx = 0;
				setXtemp(currentCol * tileSize + cwidth / 2);
			}
			else{
				setXtemp(getXtemp() + dx);
			}
		}
		//verifica se è bloccata la strada a destra
			if(dx > 0) {
				if(topRight || bottomRight) {
					dx = 0;
					setXtemp((currentCol + 1) * tileSize - cwidth / 2);
				}
				else {
					setXtemp(getXtemp() + dx);
				}
			
			
		}
		if(!falling){ 
			// se stiamo cadento enon troviamo niente di solido sotto di noi, continuiamo a cadere
			calculateCorners(x, ydest+1);
			if(!bottomLeft && !bottomRight){
				falling = true;
			}
		}
	}
	
	public boolean notOnScreen(){
		
		//verifica se l'obj è all'interno della porzione di mappa da renderizzare in quel momento
		//in modo che non vengano renderizzate più cose del necessario
		
		return x + xmap + width < 0 || //l'obj è  più a sinistra della parte in schermo
				x + xmap - width > GamePanel.WIDTH || //o più a destra
				y + ymap + height < 0 || //o più in basso
				y + ymap - height > GamePanel.HEIGHT;//o più in alto

	
	}

////////////////////////// GETTERS //////////////////////////
	
	public int getX() {
		return (int) x;
	}
	public int getY() {
		return (int) y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getCwidth() {
		return cwidth;
	}
	public int getCheight() {
		return cheight;
	}
	public double getXtemp() {
		return xtemp;
	}

	

	public double getYtemp() {
		return ytemp;
	}

	

	
	public Rectangle getBounds(){
			
			//restutuisce il rettangolo della hitBox
			return new Rectangle(
					(int)x - cwidth,
					(int)y - cheight,
					cwidth,
					cheight);
		}
	
	public boolean isRecovering(){return recover;}
	
	public long getRecoverTimer(){return recoverTimer;}
	
	//////////////////////SETTERS/////////////////////////
	
	public void setXtemp(double xtemp) {
		this.xtemp = xtemp;
	}
	public void setYtemp(double ytemp) {
		this.ytemp = ytemp;
	}
	
	public void setRecover(boolean b){recover = b;}
	
	public void setRecoverTimer(long t){ recoverTimer = t;}
	
	public void setPosition(double x, double y){ //setta la posizione dell'obj
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy){//setta il vetore movimento dell'obj
		
		this.dx = dx;
		this.dy = dy;
		
	}
	
	public void setMapPosition(){	//setta la posizione sulla mappa dell'Obj
		
		xmap = tileMap.getx();
		ymap = tileMap.gety();
		
	}

	public void setLeft(boolean left) {
		this.left = left;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
		
	////////////////////RENDERING////////////////////

	public void draw(Graphics2D g) {
		
		if(recover) {
			 	long elapsed =
					(System.nanoTime() - recoverTimer) / 1000000;
				if(elapsed / 100 % 2 == 0) {
					return;
				}
			}

		setMapPosition();
		//specchia l'imagine da renderizzare se ci si muove a destra o sinistra
		if(facingRight) {//Destra, render normale
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width/2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(	//Sinistri, render specchiata
				animation.getImage(),
				(int)(x + xmap  + width/2),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
			
		}
		// renderizza le hitbox per debug
	//	Rectangle r = getRectangle();
	//	g.setColor(Color.orange);
	//	r.x += xmap;
	//	r.y += ymap;
		//g.draw(r);
	}

}
