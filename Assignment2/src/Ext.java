import java.util.*;
import java.util.LinkedList;
import java.util.Dictionary;
public class Ext {
	Random random = new Random();
	State lowestEvalState;
	Evaluator eval;
	long start;
	long end;
	LinkedList <State> schedule;
	FileData fd;
	State blankState;
	SoftConstraintState stateSoftCheck;
	
	public Ext(Evaluator eval, State blankState){
		this.eval = eval;
		this.blankState = blankState;
	}

	//method that runs and get the optomized solution
	public State getOptomized(LinkedList<State> factsSet, FileData FD){
		State newState = null;
		start = System.currentTimeMillis();
		fd = FD;
		stateSoftCheck = new SoftConstraintState(FD);
		end = start + 7200000;
		schedule = factsSet;
		OrTree newOr;
		int randNum;
		int randNum2;
		int ExtNum;
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
		long diff;
		int[] weight = setExtensionRulesWeight();
		long now = System.currentTimeMillis();
		diff = end - now;
		//While the time limit is not passed
		while (diff > 0) {
			if (lowestEvalState.eval_Value == 0) {
				return lowestEvalState;
			}
			//choose the different extension rules to try for each generation
			System.out.println("Generation number: " + genCount + " Top eval value: " + lowestEvalState.eval_Value);
 			for(int i = 0; i < DataParser.generationSize * DataParser.generationMultiplier; i++){
 				ExtNum = random.nextInt(100);
				if (ExtNum < weight[0]) {
					randNum = random.nextInt(schedule.size());
					randNum2 = random.nextInt(schedule.size());
					newState = breed(schedule.get(randNum), lowestEvalState, 1);
					
				}else if(ExtNum < weight[1]){
					randNum = random.nextInt(schedule.size());
					newState = mutate(schedule.get(randNum), 1);
				}
				else if(ExtNum < weight[2]){
					randNum = random.nextInt(schedule.size());
					newState = putCoursesIntoSlotsUnderMin(lowestEvalState, 1);
				}
				else if(ExtNum < weight[3]){
					randNum = random.nextInt(schedule.size());
					newState = pairTwoItems(lowestEvalState, 1);
				}
				else if(ExtNum < weight[5]){
					randNum = random.nextInt(schedule.size());
					newState = assignSectionPairsToSameSlot(lowestEvalState, 1);
				}
				else if(ExtNum < weight[6]){
					randNum = random.nextInt(schedule.size());
					newState = placePreferredClass(schedule.get(randNum), 1);
				}
				else{
					newOr = new OrTree(new State(blankState), FD);
					if(newOr.fillStateRecursive(blankState.CoursesLabsToAssign, System.currentTimeMillis() + DataParser.orTreeTimeOut))
						newState = newOr.currentState;
				}
				
				if (Constr.finalCheck(newState, FD.incompatible, FD.preAssigned, FD.unwanted)) {
					schedule.add(newState);
					newState.eval_Value = eval.evaluateTimeslots(newState.timeSlots);
					if (newState.eval_Value < lowestEvalState.eval_Value){
						lowestEvalState = newState;
						genWithoutChange = 0;
					}
					diff = end - System.currentTimeMillis();
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
	
	//method to determine the weight of each extension rule to employ per generation
	private int[] setExtensionRulesWeight(){
		int[] weights = new int[8];
		
		double prefVal = EvalData.getWpref() * fd.preferences.size();
		double pairVal = EvalData.getWpair() * fd.pair.size();
		double minVal = EvalData.getWminfilled() * fd.courseSlots.size();
		double secDiffVal = EvalData.getWsecdiff() * fd.courseSlots.size();
		double notPairedVal = EvalData.getWsecdiff() * fd.unwanted.size();
		
		/*
		
		// Pref Weight
		int maxPref = 0;
		for (TimeCoursePair tcp : fd.getPreferences()){
			maxPref = maxPref + tcp.prefVal;
		}
		int prefVal = EvalData.getWpref() * maxPref;
		
		// Pair Weight
		int pairVal = EvalData.getWpair() * (EvalData.getPen_notpaired() * fd.pair.size());
		
		// Minimum Weight
		int minVal = 0;
		for (Slot ts : fd.getCourseSlots()){
			minVal = minVal + ts.getMin() * EvalData.getPen_coursemin();
		}
		for (Slot ts : fd.getLabSlots()){
			minVal = minVal + ts.getMin() * EvalData.getPen_labsmin();
		}
		
		// Sections Weight
		int maxDiff = 0;
		for (int i = 0; i < fd.getCourses().size() - 1; i++) {
			for (int j = i + 1; j < fd.getCourses().size(); j++) {
				if (eval.isSameCourseDifferentSection(fd.getCourses().get(i), fd.getCourses().get(j))) {
					maxDiff = maxDiff + 1;
				}
			}
		}
		int secDiffVal = EvalData.getWsecdiff() * (maxDiff * EvalData.getPen_section());
		*/
		double randomNew = ((prefVal + pairVal + minVal + secDiffVal)/100)*8;
		double breed = ((prefVal + pairVal + minVal + secDiffVal + randomNew)/100)*8;
		double mutate = ((prefVal + pairVal + minVal + secDiffVal + randomNew)/100)*6;
		double total = prefVal + pairVal + minVal + secDiffVal + randomNew + breed + mutate;
		
		
		//Breed
		weights[0] =  (int)Math.round((breed/total) *100);
		//Mutate
		weights[1] = (int)Math.round((mutate/total)*100) + weights[0];
		//putCoursesIntoSlotsUnderMin
		weights[2] = (int)Math.round((minVal/total)*100) + weights[1];
		//pairTwoItems
		weights[3] = (int)Math.round((pairVal/total)*100) + weights[2];
		
		//replaceUndesired
		//weights[4] = (int)Math.round((notPairedVal/total)*100) + weights[3];
		weights[4] = 0;
		
		//assignSectionPairsToSameSlot
		weights[5] = (int)Math.round((secDiffVal/total)*100) + weights[3];
		//placePreferredClass
		weights[6] = (int)Math.round((prefVal/total)*100) + weights[5];
		//randomNew
		weights[7] = (int)Math.round((randomNew/total)*100) + weights[6];
		
		return weights;
	}
	
	//Moves a course to a timeslot that is below the minimum
	private State putCoursesIntoSlotsUnderMin(State state, int numberOfMutations){
		State output = new State(state);
		int numberOfMutationsDone = 0;
		Timeslot from;
		Timeslot to;
		int itemIndex = 0;
		Timeslot temp;
		int randNum;
		boolean forCourses;
		LinkedList<Integer> altern;
		while(numberOfMutationsDone < numberOfMutations){
			from = to = null;
			randNum = random.nextInt(fd.courseSlots.size() + fd.labSlots.size());
			if(randNum < fd.courseSlots.size())
				forCourses = true;
			else 
				forCourses = false;
			
			altern = new LinkedList<Integer>();
			
			for(int k = 0; k < output.timeSlots.size(); k++){
				altern.add(new Integer(k));
			}
			
			for(int i = 0; i < altern.size(); i++){
				randNum = altern.remove(random.nextInt(altern.size()));
				temp = output.timeSlots.get(randNum);
				if(temp.localSlot.Min > temp.assignedItems.size() && temp.forCourses == forCourses){
					to = temp;
				}else if (temp.localSlot.Min < temp.assignedItems.size()&& temp.forCourses == forCourses){
					from = temp;
				}
				if((from != null)&&(to != null)){
					break;
				}
			}
			
			if((from != null)){
				if(to != null){
					itemIndex = random.nextInt(from.assignedItems.size());
					if(to.addItemToTimeslot(from.assignedItems.get(itemIndex), fd))
						from.assignedItems.remove(itemIndex);
					}
			}
			numberOfMutationsDone++;
		}
		return output;
	}
	
	//Try and assign some courses to the same section
	private State assignSectionPairsToSameSlot(State state, int numberOfMutations){
		State output = new State(state);
		courseItem item1 = null;
		courseItem item2 = null;
		Timeslot timeslotToCheck;
		Timeslot destination;
		int randNum;
		int item2Index;
		int checks;
		boolean pairFound;
		CoursePair CP;
		if(fd.pair.size() == 0){
			return output;
		}
		//determine how many mutations to attempt
		for(int i = 0; i < numberOfMutations; i++){
			
			CP = fd.pair.get(random.nextInt(fd.pair.size()));
			
			if(CP.itemOne.isALec == CP.itemTwo.isALec){
				
				timeslotToCheck = output.timeSlots.get(random.nextInt(output.timeSlots.size()));
				checks = 0;
				while(checks < 40 && timeslotToCheck.forCourses != CP.itemOne.isALec && timeslotToCheck.assignedItems.size() < timeslotToCheck.localSlot.Max - 2){
					timeslotToCheck = output.timeSlots.get(random.nextInt(output.timeSlots.size()));
				}
				for( int j = 0; j < output.timeSlots.size(); j++){
					for(int k = 0; k < output.timeSlots.get(j).assignedItems.size(); k++){
						if(CP.itemOne.isSameCourseItems(output.timeSlots.get(j).assignedItems.get(k)) && timeslotToCheck.addItemToTimeslot(output.timeSlots.get(j).assignedItems.get(k), fd)){
							output.timeSlots.get(j).assignedItems.remove(k);
							break;
						}
					}
				}
				
				for( int j = 0; j < output.timeSlots.size(); j++){
					for(int k = 0; k < output.timeSlots.get(j).assignedItems.size(); k++){
						if(CP.itemTwo.isSameCourseItems(output.timeSlots.get(j).assignedItems.get(k)) && timeslotToCheck.addItemToTimeslot(output.timeSlots.get(j).assignedItems.get(k), fd)){
							output.timeSlots.get(j).assignedItems.remove(k);
							break;
						}
					}
				}
			}
		}
		return output;
	}
	
	//Check for the course being the same but a different section
	private Boolean isSameCourseDifferentSection(courseItem inItem1, courseItem inItem2)
	{
		if(!inItem1.getDepartment().equals(inItem2.getDepartment())) {
			//("got here 1" + inItem1.getDepartment() + inItem2.getDepartment());
			return false;
		}
		if(!inItem1.getNumber().equals(inItem2.getNumber())) {
			//("got here 1" + inItem1.getNumber() + inItem2.getNumber());
			return false;
		}
		if(!inItem1.getLecVsTut().equals(inItem2.getLecVsTut())) {
			//("got here 1" + inItem1.getLecVsTut() + inItem2.getLecVsTut());
			return false;
		}
		if(inItem1.getSection().equals(inItem2.getSection())) {
			//("got here 1" + inItem1.getSection() + inItem2.getSection());
			return false;
		}
		if(!inItem1.getTutVLab().equals(inItem2.getTutVLab())) {
			//("got here 1" + inItem1.getTutVLab() + inItem2.getTutVLab());
			return false;
		}
		if(!inItem1.getTutSection().equals(inItem2.getTutSection())) {
			//("got here 1" + inItem1.getTutSection() + inItem2.getTutSection());
			return false;
		}
		return true;
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
						destinationSlot.addItemToTimeslot(TCP.item, fd);
						break;
					}
				}
			}
			
			//Loop that adds the course to its desired location
			for(int j = 0; j < output.timeSlots.size(); j++){
				if(output.timeSlots.get(j).localSlot.isSameSlot(TCP.time)){
					output.timeSlots.get(j).addItemToTimeslot(TCP.item, fd);
					break;
				}
			}
		}
		return output;
	}
	
	//Move a course if it is in a spot it shouldn't be
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
				if(destination.addItemToTimeslot(fd.unwanted.get(ranNum).item, fd))
					break;
			}
		}

		return output;
	}
	
	
	//Method to get two items that are to be paired and put them together
	private State pairTwoItems(State state, int numberOfMutations){
		State output = new State(state);
		CoursePair CP;
		Timeslot destination1;
		Timeslot source;
		int checks;
		boolean validDest = false;
		if(numberOfMutations > fd.pair.size())
			numberOfMutations = fd.pair.size()/2;
		if(numberOfMutations < 1){
			
		}
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
						if(!destination1.addItemToTimeslot(CP.itemOne, fd)||!destination1.addItemToTimeslot(CP.itemOne, fd))
							throw new IllegalArgumentException("error adding both one course in pair two items extension rule");
					}
			}
			
		}
		return output;
	}

	
	//method to breed two objects
	private State breed (State state1, State state2, int numberOfMutations) {
		State FromState;
		State ToState;
		int randNum;
		int index = 0;
		if(numberOfMutations < 1){
			numberOfMutations = 1;
		}
		if (state1.eval_Value < state2.eval_Value) {
			ToState = new State(state1);
			FromState = state2;
		}else {
			ToState = new State(state2);
			FromState = state1;
		}
		
		Timeslot sourceTimeslot;
		Timeslot destinationTimeslot = null;
		//Do a number of mutations based on the input provided to the method
		for(int k = 0; k <= numberOfMutations; k++){
			
			sourceTimeslot = null;
			
			destinationTimeslot = stateSoftCheck.getSoftState(ToState.timeSlots);
			if (destinationTimeslot == null){
				return ToState;
			}
			for(int i = 0; i < FromState.timeSlots.size(); i++){
				if((FromState.timeSlots.get(i).equals(destinationTimeslot)) && (FromState.timeSlots.get(i).assignedItems.size() > 0)){
					sourceTimeslot = FromState.timeSlots.get(i);
					index = i;
					break;
				}
			}
			//Make sure we can add this course before we remove it from elsewhere
			if((destinationTimeslot.assignedItems.size() < destinationTimeslot.localSlot.Max) && sourceTimeslot != null){
				
				//Choose the course to modify to look more like the best
				int courseIndex = random.nextInt(sourceTimeslot.assignedItems.size());
				courseItem courseToMove = sourceTimeslot.getAssignedItems().get(courseIndex);
				List<courseItem> temp;
				
				//Remove the course that is to be added
				for(int i = 0; i < ToState.timeSlots.size(); i++){
					temp = ToState.timeSlots.get(i).getAssignedItems();
					for(int j = 0; j < temp.size(); j++){
						if(temp.get(j).isSameCourseItems(courseToMove)){
							ToState.timeSlots.get(i).getAssignedItems().remove(temp.get(j));
							break;
						}
					}
				}
				
				//Add the course to the proper location
				ToState.timeSlots.get(index).addItemToTimeslot(courseToMove, fd);
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
		Timeslot source;
		Timeslot destination;
		boolean cont;
		int destinationIndex = 0;
		int sourceIndex = 0;
		if(numberOfMutations < 1){
			numberOfMutations = 1;
		}
		int randNum;
		//Loop to go through the number of mutations required
		for(int j = 0; j < numberOfMutations; j++){
			
			destination = stateSoftCheck.getSoftState(newState.timeSlots);
			
			if (destination == null){
				return newState;
			}
			source = null;
			cont = false;
			while(true){
				source = newState.timeSlots.get(random.nextInt(newState.timeSlots.size()));
				if(source.assignedItems.size() > 0 && !source.equals(destination)){
					cont = true;
					break;
				}
			}
			
			for (int i = 0; i < newState.timeSlots.size(); i++) {
				if (newState.timeSlots.get(i).equals(destination)) {
					destinationIndex = i;
				}else if (newState.timeSlots.get(i).equals(source)) {
					sourceIndex = i;
				}
			}
			
			if (cont) {
				//Make sure we can add this course before we remove it from elsewhere
				if((destination.assignedItems.size() < destination.localSlot.Max) && source != null){
					

					int courseIndex = random.nextInt(source.assignedItems.size());
					courseItem courseToMove = newState.timeSlots.get(sourceIndex).getAssignedItems().remove(courseIndex);
					
					newState.timeSlots.get(destinationIndex).addItemToTimeslot(courseToMove, fd);
				}
			}
		}
		return newState;
	}


	//Method to purge a portion of the worst results
	public LinkedList <State> purge (LinkedList <State> states){
		LinkedList <State> output = new LinkedList<State>();
		LinkedList <State> best = new LinkedList<State>();
		LinkedList <State> other = new LinkedList<State>();

		
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
		
		//Grab the best results
		for(int i = evalValues.length - 1; i > (evalValues.length - 1) - DataParser.generationSize * DataParser.percentOfTopTenToTake ; i--){
			best.add(states.get(evalValues[i][1]));
		}
		//Grab some other results for random chance of other solutions
		int rangeOfWorst = evalValues.length - (int)(DataParser.generationSize * (1 - DataParser.percentOfTopTenToTake));
		int randNum;
		for(int i = 0; i < (DataParser.generationSize *.5); i++){
			randNum = random.nextInt(evalValues.length - rangeOfWorst);
			other.add(states.get(evalValues[randNum][1]));
		}
		output = best;
		output.addAll(other);

		states.clear();
		return output;
	}
}
