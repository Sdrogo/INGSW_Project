package CORE.gameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import CORE.World.TileMap;
import CORE.entity.Explosion;
import CORE.entity.Player;
import CORE.entity.Teleport;
import CORE.entity.enemies.Enemy;
import CORE.entity.enemies.Mecha;
import CORE.entity.enemies.Robot;
import CORE.entity.enemies.Slug;

import GUI.Background;
import GUI.GamePanel;
import GUI.HUD;
import audio.AudioPlayer;

public class Level1State  extends GameState {

	private TileMap tileMap;
	private Background sky, clouds, city;
	
	private Player player;
	private HUD hud;
	private AudioPlayer bgMusic;
	
	private Teleport teleport;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	
	////////////////////////////////////COSTRUTTORI////////////////////////////
	
	public Level1State(GameStateManager GameStateManager){
	
		this.GameStateManager = GameStateManager;
		init();
	}
	

	public void init() {
		
		//inizializza la mappa
		tileMap = new TileMap(60);
		tileMap.loadTiles("/TileSets/futuristicset_60.gif");
		tileMap.loadMap("/Maps/lvl1.map");
		tileMap.setPosition(0, 0);
		
		//inizializza il giocatore
		player = new Player(tileMap);
		player.setPosition(100, 390);
		
		//inizializza l'HUD
		hud = new HUD(player);
		
		//inizializza i nemici
		
		populateEnemies();
		
		//inizializza l'arraylist delle esplosioni
		
		explosions = new ArrayList<Explosion>();
			
		//inizializza il background
		sky = new Background("/Backgrounds/City_bg.gif", 0);
		clouds = new Background("/Backgrounds/City_clouds.gif", 0.1);
		city = new Background("/Backgrounds/City_buildings.gif", 0.2);
		clouds.setVector(1.1, 0);
		
		//inizializza l'audioPlayer
		 bgMusic = new AudioPlayer("/Music/level1.mp3");
	 	 bgMusic.setLoop();	
	 	 

		teleport = new Teleport(tileMap);
		teleport.setPosition(6330, 320);
		
	}
	
	private void populateEnemies() { //inizializza tutti i nemici nel livello
	
		Slug s;
		enemies = new ArrayList<Enemy>();
		int count = 0;
		
		Point[] spawnPoints_Slug = new Point[]{ //crea un array di punti dove far spownare i nemici
				new Point(650, 510),
				new Point(400, 510),
				new Point(1150, 120),
				new Point(3050, 420),
				new Point(3100, 420),
				new Point(3300, 420),
				new Point(3500, 420),
				new Point(4250, 420),
				new Point(4800, 420),
				new Point(5000, 420),
				new Point(4900, 420),
		};
		for(int i=0; i< spawnPoints_Slug.length;i++){
			s = new Slug(tileMap);
			s.setPosition(spawnPoints_Slug[i].getX(), spawnPoints_Slug[i].getY());
			count++;
			if(count%2 == 0)
				s.switchFacing();
				
			enemies.add(s);
		}
			Mecha m = new Mecha(tileMap);
			Point[] spawnPoints_Mecha = new Point[]{ //crea un array di punti dove far spownare i nemici
					new Point(600, 500),
				//	new Point(500, 300),
					new Point(2050, 220),
				//	new Point(2100, 300),
					new Point(2550, 200),
					new Point(3550, 220),
					new Point(5000, 320),
			};
			count=0;
			for(int i=0; i< spawnPoints_Mecha.length;i++){
				m = new Mecha(tileMap);
				m.setPosition(spawnPoints_Mecha[i].getX(), spawnPoints_Mecha[i].getY());
				count++;
				if(count%2 == 0)
					m.switchFacing();
				enemies.add(m);
			}
			Robot r = new Robot(tileMap);
			r.setPosition(6000, 420);
			enemies.add(r);
		
		
	}

	//////////////////////////////////UPDATE/////////////////

	public void update() {
		
		if(player.isDead()) {
			bgMusic.stop();
			GameStateManager.setState(CORE.gameState.GameStateManager.GAMEOVER);
		}
		
		if(teleport.intersects(player)) {
			bgMusic.stop();
			
			GameStateManager.setState(CORE.gameState.GameStateManager.LEVELL2STATE);
		}
		//update del player
		player.update();
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getX(),
				GamePanel.HEIGHT / 2 - player.getY()
			);
		//attacca un nemico
		player.checkAttack(enemies);
		
		//update dei nemici
		
		for(int i = 0; i < enemies.size(); i++) {
			if(!enemies.get(i).notOnScreen()){
				Enemy e = enemies.get(i);
				e.update(player);
				
				if(e.isDead()) {
					enemies.remove(i);
					i--;
					explosions.add(
						new Explosion(e.getX(), e.getY()));
					player.reloadMana();
				}
			}
		}
		// update delle esplosioni
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		//scorrimento di parti del background a secondo del movimento del player
		if(player.isMoving())
			if(player.isFacingRight())
				city.setVector(-0.3, 0);
			else
				city.setVector(0.3, 0);
		else
			city.setVector(0, 0);
				city.update();
			clouds.update();
			
		teleport.update();
	}

	/////////////////////////////////RENDERING//////////////////////////////////////
	
	public void draw(Graphics2D g) {

		// stampa il Background
		
		sky.draw(g);
		clouds.draw(g);
		city.draw(g);
		
		// rendering della mappa
		
		tileMap.draw(g);
		
		//render del player
		
		player.draw(g);
		
		
		
		teleport.draw(g);
		
		//renderizza i nemici
		
		for(int i = 0; i < enemies.size(); i++){
			
			enemies.get(i).draw(g);	
					
		}
		
		//renderizza le esplosioni dei nemici
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		//render del HUD
		
		hud.draw(g);
	}
	
//////////////////////////////////KEYLISTER////////////////////////////////////////

	public void keyPressed(int k) {
		if( k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A)	player.setLeft(true);
		if( k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D)   player.setRight(true);
		if( k == KeyEvent.VK_W || k == KeyEvent.VK_UP)		player.setJumping(true);
		if( k == KeyEvent.VK_R)								player.setAttacking();
		if( k == KeyEvent.VK_F&&player.canShoot())			player.setShooting();
		if( k == KeyEvent.VK_SPACE) player.setAttacking();
	}

	public void keyReleased(int k) {

		if( k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A)	player.setLeft(false);
		if( k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D)   player.setRight(false);
		if( k == KeyEvent.VK_W || k == KeyEvent.VK_UP)		player.setJumping(false);
			
	}

}
