
public class Game {

	final static int NUM_CHAIRS = 15;
		
	public static void main(final String[]args){
	
			final Chair chairs [] = new Chair[NUM_CHAIRS];
			for(int i=0; i<NUM_CHAIRS; i++){
				chairs[i] = new Chair();
			}
			
			Display d = new Display(chairs);
			d.start();
			
			Player players[] = new Player[NUM_CHAIRS+1];
		
			for(Player p:players){
				p= new Player(chairs);
				p.start();
			}
			
	}
}
