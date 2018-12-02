import java.util.*;
import java.util.LinkedList;
import java.util.Dictionary;
public class Ext {
	private int max;
	private int randNum2;
	Random random = new Random();
	State newState;
	State lowestEvalState = new State();
	Evaluator eval;
	long start = System.currentTimeMillis();
	long end = start + 120*1000;
	LinkedList <State> schedule;
	FileData fd;
	State blankState;
	
	public Ext(Evaluator eval, State blankState){
		this.eval = eval;
		this.blankState = blankState;
	}

	public State getOptomized(LinkedList<State> factsSet, FileData FD){
		start = System.currentTimeMillis();
		fd = FD;
		end = start + 120*100000;
		schedule = factsSet;
		OrTree newOr;
		int randNum;
		int genWithoutChange = 0;
		lowestEvalState = schedule.get(0);
		lowestEvalState.eval_Value = eval.evaluateTimeslots(lowestEvalState.timeSlots);
		for (int i = 1; i < schedule.size(); i++) {
			schedule.get(i).eval_Value = eval.evaluateTimeslots(schedule.get(i).timeSlots);
			if (schedule.get(i).eval_Value < lowestEvalState.eval_Value) {
				lowestEvalState = schedule.get(i);
			}
		}
		int genCount = 0;
		while (System.currentTimeMillis() < end) {
			if (lowestEvalState.eval_Value == 0) {
				return lowestEvalState;
			}
			System.out.println("Generation number: " + genCount + " Top eval value: " + lowestEvalState.eval_Value);
 			for(int i = 0; i < DataParser.generationSize * DataParser.generationMultiplier; i++){
				randNum = random.nextInt(100);
				if (randNum < 20) {
					randNum = random.nextInt(schedule.size());
					randNum2 = random.nextInt(schedule.size());
					newState = breed(schedule.get(randNum), schedule.get(randNum2), (int)Math.ceil(lowestEvalState.eval_Value/DataParser.generationMutationModifier));
					
				}else if(randNum < 40){
					randNum = random.nextInt(schedule.size());
					newState = mutate(schedule.get(randNum), (int)Math.ceil(lowestEvalState.eval_Value/DataParser.generationMutationModifier));
				}
				else if(randNum < 50){
					randNum = random.nextInt(schedule.size());
					newState = pairTwoItems(schedule.get(randNum), (int)Math.ceil(lowestEvalState.eval_Value/DataParser.generationMutationModifier));
				}
				else if(randNum < 70){
					randNum = random.nextInt(schedule.size());
					replaceUndesired(schedule.get(randNum), (int)Math.ceil(lowestEvalState.eval_Value/DataParser.generationMutationModifier));
				}
				else if(randNum < 90){
					randNum = random.nextInt(schedule.size());
					newState = placePreferredClass(schedule.get(randNum), (int)Math.ceil(lowestEvalState.eval_Value/DataParser.generationMutationModifier));
				}
				else{
					newOr = new OrTree(new State(blankState), FD);
					if(newOr.fillStateRecursive(blankState.CoursesLabsToAssign))
						newState = newOr.currentState;
				}
					
				if (Constr.finalCheck(newState, FD.incompatible, FD.preAssigned)) {
					schedule.add(newState);
					newState.eval_Value = eval.evaluateTimeslots(newState.timeSlots);
					if (newState.eval_Value < lowestEvalState.eval_Value){
						lowestEvalState = newState;
						genWithoutChange = 0;
					}
				}
			}
 			genWithoutChange++;
			genCount++;
			if(genWithoutChange == DataParser.generationsWithoutChangeForResult)
				return new State(lowestEvalState);
			schedule = purge(schedule);
		}
		return new State(lowestEvalState);
	}
	
	//Put some new preferred class in spots that they desire
	private State placePreferredClass(State state, int numberOfMutations){
		if(numberOfMutations < 1)
			numberOfMutations = 1;
		State output = new State(state);
		int randNum;
		TimeCoursePair TCP;
		Timeslot destinationSlot;
		//loop that assigns x number of courses to their preference
		for(int i = 0; i < numberOfMutations; i++){
			randNum = random.nextInt(fd.preferences.size());
			TCP = fd.preferences.get(randNum);
			
			//Remove item from its current location
			for(int j = 0; j < output.timeSlots.size(); j++){
				destinationSlot = output.timeSlots.get(j);
				for (int k = 0; k < destinationSlot.assignedItems.size(); k++){
					if(destinationSlot.assignedItems.get(k).isSameCourseItems(TCP.item)){
						destinationSlot.addItemToTimeslot(TCP.item);
						break;
					}
				}
			}
			
			//Loop that adds the course to its desired location
			for(int j = 0; j < output.timeSlots.size(); j++){
				if(output.timeSlots.get(j).localSlot.isSameSlot(TCP.time)){
					output.timeSlots.get(j).addItemToTimeslot(TCP.item);
					break;
				}
			}
		}
		return output;
	}
	
	//
	private State replaceUndesired(State state, int numberOfMutations){
		if(numberOfMutations > fd.unwanted.size())
			numberOfMutations = fd.unwanted.size()/2;
		if(numberOfMutations < 1)
			numberOfMutations = 1;
		State output = new State(state);
		LinkedList<Integer> altern = new LinkedList<Integer>();
		Slot source;
		Timeslot destination;
		for(int i = 0; i < fd.unwanted.size(); i++){
			altern.add(i);
		}
		int ranNum;
		boolean found = false;
		for(int i = 0; i < numberOfMutations; i++){
			ranNum = random.nextInt(fd.unwanted.size());
			source = fd.unwanted.get(ranNum).time;
			for(int j = 0; j < output.timeSlots.size(); j++){
				if(source.isSameSlot(output.timeSlots.get(j).localSlot)){
					for(int k = 0; k < output.timeSlots.get(j).assignedItems.size(); k++){
						if(fd.unwanted.get(ranNum).item.isSameCourseItems(output.timeSlots.get(j).assignedItems.get(k))){
							output.timeSlots.get(j).assignedItems.remove(k);
							found = true;
						}
					}
				}
			}
			while(found){
				destination = output.timeSlots.get(random.nextInt(output.timeSlots.size()));
				if(destination.addItemToTimeslot(fd.unwanted.get(ranNum).item))
					break;
			}
		}

		return output;
	}
	
	private State pairTwoItems(State state, int numberOfMutations){
		State output = new State(state);
		CoursePair CP;
		Timeslot destination1;
		Timeslot source;
		int checks;
		boolean validDest = false;
		if(numberOfMutations > fd.pair.size())
			numberOfMutations = fd.pair.size()/2;
		for(int i = 0; i < numberOfMutations; i++){
			CP = fd.pair.get(random.nextInt(fd.pair.size()));
			if(((CP.itemOne.isALec == true)&&(CP.itemTwo.isALec == true))||(CP.itemOne.isALec == false && CP.itemTwo.isALec == true)){
				for(int j = 0; j < output.timeSlots.size(); j++){
					source = output.timeSlots.get(j);
					for(int k = 0; k < source.assignedItems.size(); k++){
						if(CP.itemOne.isSameCourseItems(source.assignedItems.get(k))||(CP.itemTwo.isSameCourseItems(source.assignedItems.get(k)))){
							source.assignedItems.remove(k);
						}
					}
				}
				//In the case that the pairs are of the same type
				
					checks = 0;
					destination1 = output.timeSlots.get(0);
					while(!validDest && (checks < 20)){
						destination1 = output.timeSlots.get(random.nextInt(output.timeSlots.size()));
						if((destination1.assignedItems.size() < destination1.localSlot.Max - 2))
							break;
						checks++;
					}
					if(validDest){
						if(!destination1.addItemToTimeslot(CP.itemOne)||!destination1.addItemToTimeslot(CP.itemOne))
							throw new IllegalArgumentException("error adding both one course in pair two items extension rule");
					}
			}
			
		}
		return output;
	}

	
	
	private State breed (State state1, State state2, int numberOfMutations) {
		State FromState;
		State ToState;
		int randNum;
		if(numberOfMutations < 1){
			numberOfMutations = 1;
		}
		if (state1.eval_Value < state2.eval_Value) {
			FromState = state1;
			ToState = new State(state2);
		}else {
			FromState = state2;
			ToState = new State(state1);
		}
		LinkedList<Integer> altern;
		Timeslot sourceTimeslot;
		//Do a number of mutations based on the input provided to the method
		for(int k = 0; k <= numberOfMutations; k++){
			//Choose a timeslot to grab a course from
			altern = new LinkedList<Integer>();
			//Provide alternatives for the program to choose as a source timeslot.
			for(int i = 0; i < FromState.timeSlots.size(); i++){
				altern.add(i);
			}
			sourceTimeslot = null;
			boolean cont = true;
			while(altern.size() > 0){
				randNum = random.nextInt(altern.size());
				sourceTimeslot = FromState.timeSlots.get(altern.get(randNum));
				if(sourceTimeslot.assignedItems.size() > 0){
					cont = false;
					break;
				}
				altern.remove(randNum);
			}
			
			//If all of altern is exhausted and none of the slots have a course in them then return
			if(cont == true)
				return FromState;
			
			//Choose the course to modify to look more like the best
			int courseIndex = random.nextInt(sourceTimeslot.assignedItems.size());
			courseItem courseToMove = sourceTimeslot.getAssignedItems().get(courseIndex);
			List<courseItem> temp;
			
			//Remove the course that is to be added
			for(int i = 0; i < ToState.timeSlots.size(); i++){
				temp = ToState.timeSlots.get(i).getAssignedItems();
				for(int j = 0; j < temp.size(); j++){
					if(temp.get(j).isSameCourseItems(courseToMove)){
						temp.remove(j);
						i = ToState.timeSlots.size();
						break;
					}
				}
			}
			
			//Add the course to the proper location
			for(int i = 0; i < ToState.timeSlots.size(); i++){
				if(ToState.timeSlots.get(i).equals(sourceTimeslot)){
					ToState.timeSlots.get(i).addItemToTimeslot(courseToMove);
					break;
				}
			}
		}
		return ToState;
	}
	

	/*
	Mutate takes a state and returns a new state that has been mutated
	by taking an already assigned course and randomly re-assigning it to any empty slot
	Mutate does NOT ensure hard constraints are not violated, this must be done later!
	*/
	private State mutate(State state, int numberOfMutations){
		State newState = new State(state);
		LinkedList<Integer> altern = new LinkedList<Integer>();
		Timeslot source;
		Timeslot destination;
		boolean cont;
		if(numberOfMutations < 1){
			numberOfMutations = 1;
		}
		int randNum;
		//Loop to go through the number of mutations required
		for(int j = 0; j < numberOfMutations; j++){
			for(int i = 0; i < newState.timeSlots.size(); i++){
				altern.add(i);
			}
			source = null;
			cont = false;
			//Find a source timeslot to take a class from
			while(altern.size() > 0){
				randNum = random.nextInt(altern.size());
				source = newState.timeSlots.get(altern.get(randNum));
				if(source.assignedItems.size() > 0){
					cont = true;
					break;
				}
				altern.remove(randNum);
			}
			if(cont){
				//Grab a course at random
				int courseIndex = random.nextInt(source.assignedItems.size());
				courseItem courseToMove = source.getAssignedItems().remove(courseIndex);
				
				//Find a destination time-slot to put that class into
				for(int i = 0; i < newState.timeSlots.size(); i++){
					altern.add(i);
				}
				while(altern.size() > 0){
					randNum = random.nextInt(altern.size());
					destination = newState.timeSlots.get(altern.get(randNum));
					if((destination.assignedItems.size() > 0)&&(!destination.equals(source))){
						destination.assignedItems.add(courseToMove);
						break;
					}
					altern.remove(randNum);
				}
			}
		}
		return newState;
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
