package CORE.gameState;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import GUI.Background;
import GUI.Button;
import GUI.GamePanel;
import audio.AudioPlayer;

public class MenuState extends GameState {
	
	
	

	private Background bg;	// background
	

	private HashMap<String, AudioPlayer> sfx;
	private AudioPlayer bgMusic; //musica di background
	
	private int currentChoice = 0; //pulsante attualmente selezionato

	private Button[] options; //pulsanti
	private BufferedImage title;	//immagine del titolo

	public MenuState(GameStateManager GameStateManager) {
		
		this.GameStateManager = GameStateManager;
		//inizializza i pulsanti, non è necessario il try catch perchè lo fa la classe Button
			options = new Button[2];
		    options[0] = new Button("play", 200 + 0 * 100);
		  //  options[1] = new Button("option", 200 + 1 * 80);
		    options[1] = new Button("exit", 200 + 1 * 100);
		    
		try {
			//prova a caricare l'immagine del titolo
		    title = ImageIO.read(
					getClass().getResourceAsStream("/Buttons/title.png")
					);
		    //prova a caricare i background
			bg = new Background("/Backgrounds/bg.gif");
			//setta la direzione e la velocità in cui far muovere il background
			bg.setVector(-2.2, 0);
			
		}
		catch(Exception e) {
			System.err.println("ERRORE NEL CARICAMENTO DEL MENU'");
			e.printStackTrace();
		}
		
		 bgMusic = new AudioPlayer("/Music/menu.mp3");
	 	 bgMusic.setLoop();	
		// bgMusic.play();
		 
		 sfx = new HashMap<String,AudioPlayer>();
		 sfx.put("switch", new AudioPlayer("/SFX/menuoption.mp3"));
		 sfx.put("select",new AudioPlayer( "/SFX/menuselect.mp3"));
		
		
	}
	
	public void init() {}
	
	public void update() {
		
		bg.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		// disegna il background
		bg.draw(g);
		
		// disegna il titolo
		
		g.drawImage(title, (GamePanel.WIDTH-title.getWidth())/2, 0, null);
		
		// seleziona uno dei pulsanti cambiandone l'immagine

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
		
		//scrive l'autore
		Font font = new Font("Arial", Font.PLAIN, 14);
		g.setFont(font);
        g.drawString("2017 - Andrea Partenope", GamePanel.WIDTH-200, GamePanel.HEIGHT-20);
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			bgMusic.stop();
			GameStateManager.setState(CORE.gameState.GameStateManager.LEVELL1STATE);
			
		}
		if(currentChoice == 1) {
			bgMusic.stop();
			System.exit(0);
		}
	}
	
	/////////////////////////KEYLISTNER//////////////////////
	
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
	public void keyReleased(int k) {}

	
}







