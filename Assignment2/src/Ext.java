import java.util.*;

public class Ext {
	private int max;
	private int randNum;
	private int randNum2;
	Random random = new Random();
	State newState = new State();
	State tempState1 = new State();
	State tempState2 = new State();
	State lowestEvalState = new State();
	Evaluator eval;
	long start = System.currentTimeMillis();
	long end = start + 120*1000;
	LinkedList <State> scedhules;
	
	public Ext(Evaluator eval){
		this.eval = eval;
	}

	public State getOptomized(LinkedList<State> factsSet, FileData FD){
		
		scedhules = factsSet;
		lowestEvalState = scedhules.get(0);
		for (int i = 1; i < scedhules.size(); i++) {
			if (eval.evaluateTimeslots(scedhules.get(i).timeSlots) < eval.evaluateTimeslots(lowestEvalState.timeSlots)) {
				lowestEvalState = scedhules.get(i);
			}
		}
		while (System.currentTimeMillis() < end) {
			if (eval.evaluateTimeslots(lowestEvalState.timeSlots) == 0) {
				return lowestEvalState;
			}
			
			randNum = random.nextInt(100);
			if (randNum < 50) {
				randNum = random.nextInt(scedhules.size());
				randNum2 = random.nextInt(scedhules.size());
				newState = breed(scedhules.get(randNum), scedhules.get(randNum2));
				
			}else {
				randNum = random.nextInt(scedhules.size());
				newState = mutate(scedhules.get(randNum));
			}
			
			if (Constr.finalCheck(newState, FD.incompatible)) {
				scedhules.add(newState);
				if (eval.evaluateTimeslots(newState.timeSlots) < eval.evaluateTimeslots(lowestEvalState.timeSlots)) {
					lowestEvalState = newState;
				}
			}
			
			if (scedhules.size() > 20) {
				scedhules = purge(scedhules);
			}
			
			
		}
		return lowestEvalState;
	}

	private State breed (State state1, State state2) {
		
		if (eval.evaluateTimeslots(state1.timeSlots) < eval.evaluateTimeslots(state2.timeSlots)) {
			tempState1 = state1;
			tempState2 = state2;
		}else {
			tempState1 = state2;
			tempState2 = state1;
		}
		max = tempState1.timeSlots.size();

		randNum = random.nextInt(max);
		Timeslot t = tempState2.timeSlots.get(randNum);

		for (int i = 0; i < tempState1.timeSlots.size(); i++) {
			for (courseItem course : tempState1.timeSlots.get(i).assignedItems) {
				if (t.assignedItems.contains(course))
					tempState1.timeSlots.get(i).assignedItems.remove(course);
			}
		}
		tempState1.timeSlots.set(randNum, t);
		return tempState1;
		
	}

	/*
	Mutate takes a state and returns a new state that has been mutated
	by taking an already assigned course and randomly re-assigning it to any empty slot
	Mutate does NOT ensure hard constraints are not violated, this must be done later!
	*/
	private State mutate(State state){
		try{
			State newState = state; //should be a deep copy
			Timeslot t = newState.timeSlots.get(random.nextInt(newState.timeSlots.size())); //gets a random timeSlot from the state
			courseItem c = t.assignedItems.remove(random.nextInt(t.assignedItems.size())); //removes a random item from the timeslot
			
			int x = 10000; //number of times to run the loop, to prevent infinite loops - can be changed
			while(x > 0){//repeatedly try to add the item that was removed back into an empty timeslot
				Timeslot slotToAdd = newState.timeSlots.get(random.nextInt(newState.timeSlots.size())); //randomly select a timeslot to add the item that was 	removed to
				if(slotToAdd.addItemToTimeslot(c)){ //break if the item was added successfully
					return newState;
				}
				x--;
			}

			return state; //if a mutated state could not be found, just return the one given as input
		}
		catch(Exception e){
			return state;
		}
		
	}

	public LinkedList <State> purge (LinkedList <State> states){

		int statesSize = states.size();
		int [][] evalValues = new int [statesSize][2];

		for (int i = 0; i < statesSize ; i++) {
			evalValues [i][0] = eval.evaluateTimeslots(states.get(i).timeSlots);
			evalValues [i][1] = i;
		}

		Arrays.sort(evalValues, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return Integer.compare(b[0], a[0]);
		    }
		});

		for (int i = statesSize/2; i < statesSize ; i++) {
			states.remove(evalValues[i][1]);
		}

		return states;
	}
}
