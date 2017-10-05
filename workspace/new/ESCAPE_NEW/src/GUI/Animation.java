package GUI;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage [] frames; //l'Array di buffered image contiene tutti i frame dell'animazione
	private int currentFrame;	//questo intero serve per scorrere l'array, è il frame attualmente renderizzato

	
	private long startTime;	//viene memorizzato il momento di partenta dell'animazione
	

	private long delay;		//il delay rappresenta la velocità che deve avere l'animazione
	
	private boolean playedOnce; //se l'animazione ha già fatto un giro completo


	//////////////COSTRUTTORE///////////////
	
	public Animation(){
	}
	////////////////////funzione di update ///////////////
	
	public void update(){
		if(delay == -1 ) return; ////////non dobbiamo renderizzare
		
		long elapsed = (System.nanoTime()-startTime)/1000000; //quanto tempo è passato dall'ultimo frame renderizzato
		
		if(elapsed> delay){ //se è più della nostra velocità
		
			currentFrame++; //passa al frame successivo
			startTime = System.nanoTime(); //resetta il tempo
		}
		if(currentFrame == frames.length){	//se il Frame che stiamo renderizzando è l'ultimo dell'Array 
			currentFrame = 0; //resetta l'iteratore alla posizione 0
			playedOnce = true; //è stata animata tutta la sequenza almeno una volta
		}
	}

	
	
	/////////////////////GETTERS/////////////////
	
	public BufferedImage getImage() {
		return frames[currentFrame];
	}
	public int getFrame() {
		return currentFrame;
	}
	public boolean isPlayedOnce() {
		return playedOnce;
	}
	
	/////////////////SETTERS//////////
	
	public void setFrames(BufferedImage[] frames){
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce= false;
	}
	public void setDelay(long d){
		delay = d;
	}
	public void setCurrentFrame(int i){
		currentFrame = i;
	}
}

