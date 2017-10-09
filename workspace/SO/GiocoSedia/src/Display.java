
public class Display extends Thread{
	
	Chair [] chairs = new Chair[Game.NUM_CHAIRS];
	
	Display(final Chair[]c){
		this.chairs = c;
	}
	
	public void run(){
		boolean run = true;
		
		try {
				while(run){
					
						int count=0;			
						Thread.sleep(200+(int) (100));
						for(final Chair c:chairs)
							if(c.isEmpty()) 
								System.err.print('O');
							else{
								count++;
								System.err.print('X');
							}
							System.out.println();
							
						if(count == Game.NUM_CHAIRS){
							run=false;
						}
					}
				
			} catch (final InterruptedException e) {
				
				throw new RuntimeException(e);
			}
	}
}
