package GUI;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import CORE.gameState.GameStateManager;

@SuppressWarnings("serial")

public class GamePanel extends JPanel 
	implements Runnable, KeyListener{
	
	// dimensioni
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	// Thread di gioco
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	//l'immagine su cui viene impressa la grafica 
	private BufferedImage image;
	//grafica
	private Graphics2D g;
	
	// manager degli stati
	private GameStateManager GameStateManager;
	
	public GamePanel() {
		super();
		setPreferredSize(
			new Dimension(WIDTH , HEIGHT ));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
	
		}
	}
	
//////////////////INIZIALIZZAZIONE////////////////////////////////
	
	private void init() { 
		
		//inizializza l'imagine su cui viene stampata la grafica
		image = new BufferedImage(
					WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);
		
		g = (Graphics2D) image.getGraphics(); //carica la grafica
		
		running = true;	//fa partire il loop
		
		GameStateManager = new GameStateManager();	//inizializza lo state manager
		
	}
	
	//////////////GAMELOOP//////////////
	
	public void run() {
		
		init();  //inizializza
		
		long start;		//tempo di partenza
		long elapsed;	//tempo passato
		long wait;		//attesa
		
		// loop
		while(running) {
			
			start = System.nanoTime(); //setta il tempo di patenza
			
			update();  			 //aggiorna
			draw();				//renderizza
			drawToScreen();		//renderizza sullo schermo
			
			elapsed = System.nanoTime() - start; //il tempo passato è quello attuale meno l'iniziale.
			
			wait = targetTime - elapsed / 1000000; //il tempo di attesa è in millisecondi
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {

				System.err.println("ERRORE NEL GAMELOOP");
				e.printStackTrace();
			}
			
		}
		
	}
	
	/////////////UPDATE//////////////////
	
	private void update() {
		
		GameStateManager.update();
	
	}
	
	//////////////////RENDERING////////////////
	
	private void draw() {
	
		GameStateManager.draw(g);
	
	}
	private void drawToScreen() {
	
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,
				WIDTH, HEIGHT,
				null);
		g2.dispose();
	
	}
	
	////////////////////////KEYLISTNER////////////////////////
	
	public void keyTyped(KeyEvent key) {}
	
	public void keyPressed(KeyEvent key) {
	
		if(GameStateManager.getCurrentState()==null) return;
	GameStateManager.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {

		if(GameStateManager.getCurrentState()==null) return;
		GameStateManager.keyReleased(key.getKeyCode());
	
	}

}





