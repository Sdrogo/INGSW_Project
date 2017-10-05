package CORE.World;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import GUI.GamePanel;


public class TileMap {
	
	// posizione
	private double x;
	private double y;
	
	// bordi
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// mappa
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// controlli telecamera
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;  //numero di righe da stampare
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;	 //numero di colonne da stampare
		tween = 0.14;
	}
	
	public void loadTiles(String s) {
		
		try { //carica le caselle 

			tileset = ImageIO.read(   //carica il set di caselle
				getClass().getResourceAsStream(s)
			);
			numTilesAcross = tileset.getWidth() / tileSize; //calcola il numero di colonne dal tileset
			tiles = new Tile[2][numTilesAcross];	//crea una matrice di caselle, la prima riga sono quelle vuote, la seconda quelle piene
			
			BufferedImage subimage; //estrapola le sottoimagini dalla spritesheet
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
							col * tileSize,
							0,
							tileSize,
							tileSize
						);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);  //le caselle sulla riga 0 sono attraversabili
				subimage = tileset.getSubimage(
							col * tileSize,
							tileSize,
							tileSize,
							tileSize
						);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);	//Le caselle sulla riga 1 sono bloccate
			}
			
		}
		catch(Exception e) {

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DELLA CASELLA");
			
		}
		
	}
	
	public void loadMap(String s) {
		//carica la mappa.
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
		
			
			xmin = GamePanel.WIDTH -  numCols * tileSize;
			xmax = 0;
			ymin = GamePanel.HEIGHT - numRows * tileSize;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {

			JOptionPane.showMessageDialog(null, "ERRORE NEL CARICAMENTO DELLA MAPPA");
		}
		
	}
//////////////////////////////////////GETTER/////////////////////////
	
	public int getTileSize() { return tileSize; }
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return numCols * tileSize; }
	public int getHeight() { return numRows * tileSize; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public int getType(int row, int col) {
		
		//restituisce se la casella blocca o no
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
////////////////////////////////SETTER///////////////////////
	
	
	public void setTween(double d) { tween = d; }
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	

	public void setBounds(int x, int y, int w, int h){
		this.x=x;
		this.y=y;
		this.numCols = w/tileSize;
		this.numRows = h/tileSize;
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	////////////////////////////////RENDER////////////////////////////
	
	public void draw(Graphics2D g) {
		
		for(
			int row = rowOffset;
			row < rowOffset + numRowsToDraw;
			row++) {
			
			if(row >= numRows) break;
			
			for(
				int col = colOffset;
				col < colOffset + numColsToDraw;
				col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);	
			}	
		}
	}
}