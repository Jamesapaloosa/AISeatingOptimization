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
		courseItem temp;
		courseItem temp2;
		Timeslot destinationTimeslot;
		int courseIndexInToAssign;
		LinkedList<courseItem> nxtCoursesToAssign;
		LinkedList<Integer> altern;
		LinkedList<Integer> courseAltern = new LinkedList<Integer>();
		

		//Return if all courses are assigned;
		if(coursesToAssign.size() == 0 && Constr.finalCheck(currentState, FD.incompatible, FD.preAssigned, FD.unwanted)){
			return true;
		}
		else if (coursesToAssign.size() == 0 && !Constr.finalCheck(currentState, FD.incompatible, FD.preAssigned, FD.unwanted))
			return false;
		//set the choices of courses to assign
		for(int k = 0; k < coursesToAssign.size(); k++){
			courseAltern.add(new Integer(k));
		}
		
		//make a list of the indexes of different choices to make at this point

		while(courseAltern.size() > 0){
			courseIndexInToAssign = new Random().nextInt(courseAltern.size());
			addingItem = coursesToAssign.get(courseAltern.remove(courseIndexInToAssign));
			altern = new LinkedList<Integer>();
			for(int k = 0; k < currentState.timeSlots.size(); k++){
				altern.add(new Integer(k));
			}
			
			while(altern.size() > 0){
				destinationTimeslot = currentState.timeSlots.get(altern.remove(new Random().nextInt(altern.size())));
				if(destinationTimeslot.addItemToTimeslot(addingItem, FD)){
					nxtCoursesToAssign = (LinkedList<courseItem>)coursesToAssign.clone();
					if(Constr.partial(currentState, FD.incompatible, FD.preAssigned, FD.unwanted)&&fillStateRecursive(nxtCoursesToAssign))
						return true;
					else{
						if(!removeCourseFromTimeslot(addingItem, destinationTimeslot))
							throw new IllegalArgumentException("Failed to remove class from destination");
					}
				}
			}
		}
		return false;
	}
	
	
	//Finds a course and removes it from a timeslot if the or tree determines that it is not possible to place that course into that location
	private boolean removeCourseFromTimeslot(courseItem removeThis, Timeslot fromHere){
		for(int i = 0; i < fromHere.assignedItems.size(); i++){
			if(removeThis.isSameCourseItems(fromHere.assignedItems.get(i))){
				fromHere.assignedItems.remove(i);
				return true;
			}
		}
		return false;
	}
}
