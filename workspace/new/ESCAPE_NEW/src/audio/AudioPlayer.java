package audio;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;

public class AudioPlayer {
	
	private Clip clip;
	
	public AudioPlayer(String s) {
		
		try {
			
			AudioInputStream ais = //prendiamo il file audio attraverso la path in input
				AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream(
						s
					)
				);
			AudioFormat baseFormat = ais.getFormat(); 	//formato di base mp3 
			AudioFormat decodeFormat = new AudioFormat(	//formato decodificato
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais =						//stream di audio decodificato
				AudioSystem.getAudioInputStream(
					decodeFormat, ais);
			
			clip = AudioSystem.getClip(); 	//clip audio
			clip.open(dais);	//viene riprodotta decodificata
		}
		catch(Exception e) {

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DELL'AUDIO");
		}
		
	}
	
	public void play() { //fa suonare la clip audio
		if(clip == null) return;	//se non esiste si interrompe
		stop();		//ferma se già stava suonado
		clip.setFramePosition(0);	//riavvolge la clip
		clip.start();				//la fa partire
	}
	
	public void stop() {	//ferma la clip
		if(clip.isRunning()) clip.stop();
	}
	
	public void close() {	//chiude la clip
		stop();
		clip.close();
	}
	
	public void setLoop(){ //setta la musica in loop
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
}