
public class Player extends Thread{

	Chair[] chairs;
	
	Player(final Chair[] c){
		chairs = c;
		System.out.println("Player n° "+(getId()-10)+" join the game");
	}
	
	public void run(){
		try {
		
				Thread.sleep((int) (250+Math.random() * 750));
				for(int i=0;i< chairs.length; i++){
					if(chairs[i].occupy()){
						System.out.println("The player n° "+(getId()-10)+" sit on a chair");
						return;
					}
				}System.err.println("The player n° "+(getId()-10)+ " lost");	
		//	}	
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
