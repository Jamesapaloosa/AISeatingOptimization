import java.util.*;

public class Ext {
	private int max;
	Random random = new Random();
	Constr constr = new Constr();
	public Ext(LinkedList <State> list) {
		
	}
	
	public State breed (State state1, State state2) {
		
		State newState = state2;
		max = state2.timeSlots.size();
		int randomNum = random.nextInt(max);
		
		Timeslot t = state1.timeSlots.get(randomNum);
		newState.timeSlots.set(randomNum, t);
		
		if (Constr.final(newState)) {
			return newState;
		}else {
			return null;
		}
	}
}
