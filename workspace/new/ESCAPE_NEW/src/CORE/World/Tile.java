package CORE.World;

import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;  //texture della casella
	private int type;			//la casella è piena o no?
	
	public static final int NORMAL = 0;	//vouta
	public static final int BLOCKED = 1;//piena
	
	//////////////////////////COTRUTTORE/////////////////////
	
	public Tile(BufferedImage image, int type){
		this.image=image;
		this.type = type;
	}
	
	/////////////////////GETTER/////////////////////////////
	
	public BufferedImage getImage(){
		return image;}
	
	public int getType(){
		return type;}
}
