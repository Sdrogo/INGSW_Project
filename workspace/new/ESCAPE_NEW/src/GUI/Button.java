package GUI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;



public class Button {


    private boolean selected;   //il pulsante è selezionato
    private BufferedImage sprite, sprite_unsel, sprite_sel; //immagine da renderizzare, immagine del pulsante non selezionato e selezionato
    int x, y;
    
    ////////////////////////////COSTRUTTORE/////////////////////
    
    
    
    
    public Button(String name, int buttonY){
    	
    	try { //carica l'mmagine del pulsante non selezionato
			sprite_unsel = ImageIO.read(
				getClass().getResourceAsStream("/Buttons/"+name+"_unsel.png")
			); //carica l'immagine del pulsante selezionato
			sprite_sel = ImageIO.read(
					getClass().getResourceAsStream("/Buttons/"+name+"_sel.png")
				);
		}
		catch(Exception e) {
			System.err.println("ERRORE NEL CARICAMENTO DEL PULSANTE");
			e.printStackTrace();
		}
    	this.y = buttonY; //setta l'altezza del pulsante
   	  this.x = (GamePanel.WIDTH - sprite_sel.getWidth())/2 ; //renderizza al centro dello schermo

    }

    
    public Button(String name, int buttonX, int buttonY){
    	this(name, buttonY);
    	
    	this.x = buttonX;
      	
    }

    
    ///////////////////////////SETTER///////////////////////////////
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /////////////////////////RENDER//////////////////////////
    
    public void draw(Graphics g){
    	
    	if(selected)						//seleziona l'immagine da renderizzare
    		this.sprite = sprite_sel;
    	else
    		this.sprite = sprite_unsel;
 
        g.drawImage(sprite, x, y, null); //renderizza il pulsante
    }


}
