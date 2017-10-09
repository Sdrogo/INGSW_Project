
public class Chair {

	boolean free;
	
	Chair(){free=true;}
	
	public synchronized  boolean isEmpty(){ return free;}
	
	public synchronized boolean occupy(){
		if(free){
			free=false;
			return true;
		}
		else return false;
	}
}
