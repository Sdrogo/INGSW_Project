package CORE.gameState;

import java.awt.Graphics2D;

public class GameStateManager {

	private GameState[] gameStates;//crea un array con tutti gli stati del gioco
	private int currentState; 				//lo in cui ci troviamo attualmente
	private int previousState;
	//enum degli stati
	public static final int NUMGAMESTATES = 5;
	public static final int MENUSTATE = 0; 
	public static final int LEVELL1STATE = 1;
	public static final int LEVELL2STATE = 2;
	public static final int LEVELL3STATE = 3;
	public static final int GAMEOVER = 4;
	
	
	//////////////////COSTRUTTORE/////////////////////////
	
	public GameStateManager(){
		gameStates = new GameState[NUMGAMESTATES]; //inizializza l'Array di stati
	
		currentState = MENUSTATE; 				//parte dal menu
		loadState(currentState);				//chiama il metodo per caicare lo stato
	}
	public void loadState(int state){ //carica ed inizializza lo stato
		
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		
		if(state == LEVELL1STATE){
			gameStates[state] = new Level1State(this);
		
		}
		if(state == LEVELL2STATE){
			gameStates[state] = new Level2State(this);
		
		}
		if(state == LEVELL3STATE){
			gameStates[state] = new Level3State(this);
		
		}
		if(state == GAMEOVER){
			gameStates[state] = new GameOverState(this);
		}
	}
	
	private void unloadState(int state){
		
		gameStates[state] = null;
	}
	
	//////////////////////GETTER//////////////////////
	
	public GameState getCurrentState(){return gameStates[currentState];}
	
	public int getPreviousState(){return previousState;}
	
	
	///////////////////////SETTER/////////////
	
	public void setState(int state){ 
		previousState = currentState;
		unloadState(currentState);
		currentState = state;
		loadState(state);
	//	gameStates[currentState].init();
	}
	
	
	///////////////UPDATE////////////////////
	
	public void update(){
		try {
			
			gameStates[currentState].update();
			
		} catch (Exception e) {
		
		}
	}
	
	////////////////////RENDER////////////////////////////
	
	public void draw(Graphics2D g){
		try {
				gameStates[currentState].draw(g);
				
		} catch (Exception e) {
		}
	}
	
	///////////////////KEYLISTNER////////////////////////
	
	public void keyPressed(int k){
		gameStates[currentState].keyPressed(k);
	}
	public void keyReleased(int k){
		gameStates[currentState].keyReleased(k);
	}
	
}

