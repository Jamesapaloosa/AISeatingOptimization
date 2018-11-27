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

	/*
	Mutate takes a state and returns a new state that has been mutated
	by taking an already assigned course and randomly re-assigning it to any empty slot
	Mutate does NOT ensure hard constraints are not violated, this must be done later!
	*/
	public State mutate(State state){
		State newState = state; //should be a deep copy
		Timeslot t = newState.timeSlots.get(random.nextInt(newState.timeSlots.size())); //gets a random timeSlot from the state
		courseItem c = t.assignedItems.remove(random.nextInt(t.assignedItems.size())); //removes a random item from the timeslot


		int x = 10000; //number of times to run the loop, to prevent infinite loops - can be changed
		while(x > 0){//repeatedly try to add the item that was removed back into an empty timeslot
			Timeslot slotToAdd = newState.timeSlots.get(random.nextInt(newState.timeSlots.size())); //randomly select a timeslot to add the item that was removed to
			if(slotToAdd.addItemToTimeslot(c)){ //break if the item was added successfully
				return newState;
			}
			x--;
		}

		return state; //if a mutated state could not be found, just return the one given as input
	}

	public LinkedList <State> purge (LinkedList <State> states){

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
