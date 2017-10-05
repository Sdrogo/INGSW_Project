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

public class Level3State  extends GameState {

	private TileMap tileMap;
	private Background sky, clouds, city;
	
	private Player player;
	private HUD hud;
	private AudioPlayer bgMusic;
	
	private Teleport teleport;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	
	////////////////////////////////////COSTRUTTORI////////////////////////////
	
	public Level3State(GameStateManager GameStateManager){
	
		this.GameStateManager = GameStateManager;
		init();
	}
	

	public void init() {
		
		//inizializza la mappa
		tileMap = new TileMap(60);
		tileMap.loadTiles("/TileSets/futuristicset_60_Y.gif");
		tileMap.loadMap("/Maps/lvl3.map");
		tileMap.setPosition(0, 0);
		
		//inizializza il giocatore
		player = new Player(tileMap);
		player.setPosition(150, 450);
		
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
		 bgMusic = new AudioPlayer("/Music/level3.mp3");
	 	 bgMusic.setLoop();	
	 	 

		teleport = new Teleport(tileMap);
		teleport.setPosition(270, 4102);
		
	}
	
	private void populateEnemies() { //inizializza tutti i nemici nel livello
	
		Slug s;
		enemies = new ArrayList<Enemy>();
		int count = 0;
		
		Point[] spawnPoints_Slug = new Point[]{ //crea un array di punti dove far spownare i nemici
				new Point(650, 710),
				new Point(780, 640),
				new Point(947, 580),
				new Point(2035, 1011),
				new Point(2450, 754),
				new Point(2179, 754),
				new Point(2284, 2929),
				new Point(2381, 2929),
				new Point(2480, 2929),
				new Point(2580, 2929),
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
					new Point(469, 910),
					new Point(990, 900),
					new Point(620, 1610),
					new Point(320, 1820),
					new Point(1230, 2020),
					new Point(505, 1682),
					new Point(1220, 1900),
					new Point(850, 1900),
					new Point(320, 2000),
					new Point(640, 2000),
					new Point(1680, 1200),
					new Point(2350, 1450),
					new Point(1450, 1720),
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
			r.setPosition(1966, 1753);
			enemies.add(r);
			r = new Robot(tileMap);
			r.setPosition(1149, 3693);
			enemies.add(r);
			r = new Robot(tileMap);
			r.setPosition(1621, 3690);
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
			
			GameStateManager.setState(CORE.gameState.GameStateManager.MENUSTATE);
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
