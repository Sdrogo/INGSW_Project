package Sort;

import java.util.List;

public class Client {

	public static void main(String[]args) {
		ElementList contextElements = new ElementList();
		contextElements.addElement(new int[] {3,2,4,3,6,5});
		List unsortedList =  contextElements.getSortedList();
		
		contextElements.setSortAlgorithm(new BubbleSort());
		
		
		List sortedList = contextElements.getSortedList();
		
		for(int i=0; i< unsortedList.size();i++){
			
			System.out.println(unsortedList.get(i));
		}
		System.out.println();
		System.out.println("////////////");
		
		for(int i=0; i< sortedList.size();i++){
			
			System.out.println(sortedList.get(i));
		}
	
	}
}
