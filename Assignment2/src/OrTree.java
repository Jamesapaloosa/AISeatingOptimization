//This class can be used to build solutions or as a way of building off of our set based solutions.
import java.util.LinkedList;
import java.util.Random;
public class OrTree {
	State currentState;
	FileData FD;
	
	//Constructor for the or tree
	public OrTree(State inState, FileData FD){
		currentState = inState;
		this.FD = FD;
	}

	
	//Or tree that acts as a recursive function to create all or tree versions of a solution.
	public boolean fillStateRecursive(LinkedList<courseItem> coursesToAssign){
		courseItem addingItem;
		Timeslot destinationTimeslot;
		int ranNum;
		LinkedList<courseItem> nxtCoursesToAssign;
		LinkedList<Integer> altern = new LinkedList<Integer>();
		LinkedList<Integer> courseAltern = new LinkedList<Integer>();
		

		//Return if all courses are assigned;
		if(coursesToAssign.size() == 0){
			if(Constr.finalCheck(currentState, FD.incompatible, FD.preAssigned, FD.unwanted))
				return true;
			else 
				return false;
		}
		//set the choices of courses to assign
		for(int k = 0; k < coursesToAssign.size(); k++){
			courseAltern.add(new Integer(k));
		}
		
		//make a list of the indexes of different choices to make at this point
		altern = new LinkedList<Integer>();
		while(courseAltern.size() > 0){
			ranNum = new Random().nextInt(courseAltern.size());
			addingItem = coursesToAssign.get(courseAltern.remove(ranNum));
			
			for(int k = 0; k < currentState.timeSlots.size(); k++){
				altern.add(new Integer(k));
			}
			
			while(altern.size() > 0){
				destinationTimeslot = currentState.timeSlots.get(altern.remove(new Random().nextInt(altern.size())));
				if(destinationTimeslot.addItemToTimeslot(addingItem, FD)){
					nxtCoursesToAssign = (LinkedList<courseItem>)coursesToAssign.clone();
					nxtCoursesToAssign.remove(ranNum);
					if(fillStateRecursive(nxtCoursesToAssign)&&Constr.partial(currentState, FD.incompatible, FD.preAssigned, FD.unwanted))
						return true;
					else
						removeCourseFromTimeslot(addingItem, destinationTimeslot);
				}
			}
		}
		return false;
	}
	
	//Finds a course and removes it from a timeslot if the or tree determines that it is not possible to place that course into that location
	private void removeCourseFromTimeslot(courseItem removeThis, Timeslot fromHere){
		for(int i = 0; i < fromHere.assignedItems.size(); i++){
			if(removeThis.isSameCourseItems(fromHere.assignedItems.get(i)))
				fromHere.assignedItems.remove(i);
		}
	}
}
