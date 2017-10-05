package CORE.gameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import GUI.Background;
import GUI.Button;
import audio.AudioPlayer;

public class GameOverState extends GameState{

	private AudioPlayer bgMusic;

	private HashMap<String, AudioPlayer> sfx;
	private Background bg;	// background
	
	private int currentChoice = 0; //pulsante attualmente selezionato

	private Button[] options; //pulsanti
	
	public GameOverState(GameStateManager gameStateManager) {
		this.GameStateManager = gameStateManager;
		options = new Button[2];
	    options[0] = new Button("continue", 250 + 0 * 80);
	  //  options[1] = new Button("option", 200 + 1 * 80);
	    options[1] = new Button("exit", 250 + 1 * 80);
	    
	try {
	    //prova a caricare i background
		bg = new Background("/Backgrounds/gameOver.gif");
		//setta la direzione e la velocità in cui far muovere il background

		
	}
	catch(Exception e) {
		System.err.println("ERRORE NEL CARICAMENTO DEL GAMEOVER'");
		e.printStackTrace();
	}
	
	 bgMusic = new AudioPlayer("/Music/gameOver.mp3");
 	 bgMusic.setLoop();	
	// bgMusic.play();
	 
	 sfx = new HashMap<String,AudioPlayer>();
	 sfx.put("switch", new AudioPlayer("/SFX/menuoption.mp3"));
	 sfx.put("select",new AudioPlayer( "/SFX/menuselect.mp3"));
	}

	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public void draw(Graphics2D g) {
		// disegna il background
		bg.draw(g);
	
		
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				options[i].setSelected(true);
			}
			else {
				options[i].setSelected(false);
			}
			
		//renderizza i pulsanti
			
			for(Button b:options)
				b.draw(g);
		}
	}
	private void select() {
		if(currentChoice == 0) {
			bgMusic.stop();
			GameStateManager.setState(GameStateManager.getPreviousState());
			
		}
		if(currentChoice == 1) {
			bgMusic.stop();
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		
		if(k == KeyEvent.VK_ENTER){

			sfx.get("select").play();
			select();
			
		}
		if(k == KeyEvent.VK_UP) {
			sfx.get("switch").play();
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {

			sfx.get("switch").play();
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	@Override
	public void keyReleased(int k) {
		
	}

}
