//This class can be used to build solutions or as a way of building off of our set based solutions.
import java.util.LinkedList;
import java.util.Random;
public class OrTree {
	State currentState;
	LinkedList<TimeCoursePair> history;
	FileData FD;
	
	
	public OrTree(State inState, FileData FD){
		currentState = inState;
		history = new LinkedList<TimeCoursePair>();
		this.FD = FD;
	}
	
	public boolean fillStateRecursive(){
		courseItem addingItem;
		Timeslot destinationTimeslot;
		int ranNum;
		boolean found;
		LinkedList<Integer> altern = new LinkedList<Integer>();

		if(currentState.getCoursesLabsToAssign().size() == 0)
			return true;
		if(currentState.getCoursesLabsToAssign().size() == 1)
			ranNum = 0;
		else
			ranNum = new Random().nextInt(currentState.getCoursesLabsToAssign().size());
		addingItem = currentState.getCoursesLabsToAssign().get(ranNum);
		
		//make a list of the indexes of different choices to make at this point
		found = false;
		altern = new LinkedList<Integer>();
		
		//Altern creates different choices
		for(int k = 0; k < currentState.timeSlots.size(); k++){
			altern.add(new Integer(k));
		}
		int j = altern.size();
		if(j == 0){
			TimeCoursePair temp = history.removeFirst();
			return false;
		}
		int alternChoice;
		int addingItemIndex;
		while(altern.size() > 0){
			alternChoice = new Random().nextInt(altern.size());
			destinationTimeslot = currentState.timeSlots.get(altern.get(alternChoice));
			altern.remove(alternChoice);
			if(addingItem.isALec == destinationTimeslot.forCourses){
					found = destinationTimeslot.addItemToTimeslot(addingItem);
					if(found){
						addingItemIndex = destinationTimeslot.assignedItems.size() - 1;		
						currentState.CoursesLabsToAssign.remove(ranNum);
						if(Constr.partial(currentState, FD.incompatible, FD.preAssigned)&&fillStateRecursive()){
							return true;
						}
						else
							currentState.CoursesLabsToAssign.add(destinationTimeslot.assignedItems.remove(addingItemIndex));
					}
			}
		}
		return false;
	}
	
	//Do not use. here for reference
	/*
	public State fillState(FileData FD){
		courseItem addingItem;
		Timeslot destinationTimeslot;
		boolean isCourse;
		int ranNum;
		boolean found;
		LinkedList<Integer> altern = new LinkedList<Integer>();
		LinkedList<courseItem> sourceList = new LinkedList<courseItem>();
		//Loop to go through all courses and labs to add
		for(int i = (currentState.getCoursesToAssign().size() + currentState.getLabsToAssign().size()); i > 0 ; i++){
			//get the course item to add
			if(currentState.getCoursesToAssign().size() == 0){
				sourceList = currentState.getLabsToAssign();
				isCourse = false;
			}
			else if(currentState.getLabsToAssign().size()== 0){
				sourceList = currentState.getCoursesToAssign();
				isCourse = true;
			}
			else{
				ranNum = new Random().nextInt(1) + 1;
				if(ranNum == 1 ){
					sourceList = currentState.getLabsToAssign();
					isCourse = false;
				}else{
					sourceList = currentState.getCoursesToAssign();
					isCourse = true;
				}
			}
			ranNum = new Random().nextInt(sourceList.size() - 1);
			addingItem = sourceList.get(ranNum);
			//get the destination 
			found = false;
			altern = new LinkedList<Integer>();
			//Altern creates different choices
			for(int k = 0; k < currentState.timeSlots.size(); k++){
				altern.add(new Integer(k));
			}
			int j = altern.size();
			if(j == 0){
				TimeCoursePair temp = history.removeFirst();
				//for(int k = 0; k < )
			}
			else{
				int alternChoice;
				while(j > 0 ){
					j = altern.size();
					alternChoice = new Random().nextInt(altern.size() - 1);
					destinationTimeslot = currentState.timeSlots.get(altern.get(alternChoice));
					altern.remove(alternChoice);
					if(isCourse == destinationTimeslot.forCourses){
						if(Constr.assign(destinationTimeslot, addingItem, FD.getIncompatible())){
							found = destinationTimeslot.addItemToTimeslot(addingItem);
							if(found){
								history.add(new TimeCoursePair(destinationTimeslot.localSlot, addingItem, -1));
								sourceList.remove(ranNum);
								break;
							}
						}
					}
				}
				if(found == false)
					return null;
			}
		}
		return currentState;
	}
	*/
	
}
