import java.util.*;

public class Ext {
	private int max;
	private int randNum;
	Random random = new Random();
	Constr constr = new Constr();
	boolean breedValid = true;
	public Ext(LinkedList <State> list) {
		
	}
	
	public State getOptomized(LinkedList<State> FactsSet){
		return new State();
	}
	
	private State breed (State state1, State state2) {
		
		State newState = state2;
		max = state2.timeSlots.size();
		
		while (breedValid) {

			randNum = random.nextInt(max);
			Timeslot t = state1.timeSlots.get(randNum);
			
			for (int i = 0; i < state2.timeSlots.size(); i++) {
				for (courseItem course : state2.timeSlots.get(i).assignedItems) {
					if (t.assignedItems.contains(course)) 
						state2.timeSlots.get(i).assignedItems.remove(course);
				}
			}
			newState.timeSlots.set(randNum, t);
			if (Constr.finalCheck(newState))
				return newState;
		}
		return null;
	}
	
	private LinkedList <State> purge (LinkedList <State> states){
		
		int statesSize = states.size();
		int [][] evalValues = new int [statesSize][2];
		int eval = 0;
		
		for (int i = 0; i < statesSize ; i++) {
			//Get eval of state
			evalValues [i][0] = eval;
			evalValues [i][1] = i;
		}
		
		Arrays.sort(evalValues, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return Integer.compare(a[0], b[0]);
		    }
		});
		
		for (int i = 0; i < statesSize/2 ; i++) {
			states.remove(evalValues[i][1]);
		}
		
		return states;
	}
}
