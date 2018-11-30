import java.util.*;
import java.util.LinkedList;
import java.util.Dictionary;
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
	LinkedList <State> schedule;
	FileData fd;
	
	public Ext(Evaluator eval){
		this.eval = eval;
	}

	public State getOptomized(LinkedList<State> factsSet, FileData FD){
		start = System.currentTimeMillis();
		fd = FD;
		end = start + 120*100;
		schedule = factsSet;
		lowestEvalState = schedule.get(0);
		lowestEvalState.eval_Value = eval.evaluateTimeslots(lowestEvalState.timeSlots);
		for (int i = 1; i < schedule.size(); i++) {
			schedule.get(i).eval_Value = eval.evaluateTimeslots(schedule.get(i).timeSlots);
			if (schedule.get(i).eval_Value < lowestEvalState.eval_Value) {
				lowestEvalState = schedule.get(i);
			}
		}
		while (System.currentTimeMillis() < end) {
			if (lowestEvalState.eval_Value == 0) {
				return lowestEvalState;
			}
			for(int i = 0; i < DataParser.generationSize * DataParser.generationMultiplier; i++){
				randNum = random.nextInt(100);
				if (randNum < 50) {
					randNum = random.nextInt(schedule.size());
					randNum2 = random.nextInt(schedule.size());
					newState = breed(schedule.get(randNum), schedule.get(randNum2));
					
				}else {
					randNum = random.nextInt(schedule.size());
					newState = mutate(schedule.get(randNum));
				}
				
				if (Constr.finalCheck(newState, FD.incompatible)) {
					schedule.add(newState);
					newState.eval_Value = eval.evaluateTimeslots(newState.timeSlots);
					if (eval.evaluateTimeslots(newState.timeSlots) < eval.evaluateTimeslots(lowestEvalState.timeSlots))
						lowestEvalState = newState;
				}
			}
			schedule = purge(schedule);
		}
		return new State(lowestEvalState);
	}

	private State breed (State state1, State state2) {
		
		if (eval.evaluateTimeslots(state1.timeSlots) < eval.evaluateTimeslots(state2.timeSlots)) {
			tempState1 = state1;
			tempState2 = state2;
		}else {
			tempState1 = state2;
			tempState2 = state1;
		}
		LinkedList<Integer> altern = new LinkedList<Integer>();
		for(int i = 0; i < tempState2.timeSlots.size(); i++){
			altern.add(i);
		}
		Timeslot timeslot2 = null;
		boolean cont = true;
		while(altern.size() > 0){
			randNum = random.nextInt(altern.size());
			timeslot2 = tempState2.timeSlots.get(altern.get(randNum));
			if(timeslot2.assignedItems.size() > 0){
				cont = false;
				break;
			}
			altern.remove(randNum);
		}
		
		if(cont == true)
			return state2;
		int courseIndex = random.nextInt(timeslot2.assignedItems.size());
		courseItem courseToMove = timeslot2.getAssignedItems().get(courseIndex);
		List<courseItem> temp;
		boolean found;
		
		for(int i = 0; i < tempState1.timeSlots.size(); i++){
			found = false;
			if(tempState1.timeSlots.get(i).equals(timeslot2))
				tempState1.timeSlots.get(i).addItemToTimeslot(courseToMove);
			 temp = tempState1.timeSlots.get(i).getAssignedItems();
			if(temp.size() > 0){
				for(int j = 0; j < temp.size(); j++){
					if(temp.get(j).isSameCourseItems(courseToMove)){
						temp.remove(j);
						found = true;
						break;
					}
				}
				if(found)
					break;
			}
		}
		return new State(tempState1);
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
		//Dictionary statesEval = new Hashtable();
		int statesSize = states.size();
		int [][] evalValues = new int [statesSize][2];

		for (int i = 0; i < statesSize ; i++) {
			evalValues [i][0] = eval.evaluateTimeslots(states.get(i).timeSlots);
			evalValues [i][1] = i;
			//statesEval.put(states.get(i), eval.evaluateTimeslots(states.get(i).timeSlots));
		}

		Arrays.sort(evalValues, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return Integer.compare(b[0], a[0]);
		    }
		});
		LinkedList <State> output = new LinkedList<State>();
		for (int i = 0; i <= DataParser.generationSize ; i++) {
			output.add(states.get(evalValues[i][1]));
		}
		states.clear();
		return output;
	}
}
