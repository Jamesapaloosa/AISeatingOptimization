//This class can be used to build solutions or as a way of building off of our set based solutions.
import java.util.LinkedList;
import java.util.Random;
public class OrTree {
	State currentState;
	FileData FD;
	
	
	public OrTree(State inState, FileData FD){
		currentState = inState;
		this.FD = FD;
	}
	
	public boolean fillStateRecursive(LinkedList<courseItem> coursesToAssign){
		courseItem addingItem;
		Timeslot destinationTimeslot;
		int ranNum;
		boolean found;
		LinkedList<courseItem> nxtCoursesToAssign;
		LinkedList<Integer> altern = new LinkedList<Integer>();
		LinkedList<Integer> courseAltern = new LinkedList<Integer>();
		
		//Return if all courses are assigned;
		if(coursesToAssign.size() == 0){
			currentState.CoursesLabsToAssign = new LinkedList<courseItem>();
			return true;
		}
		//set the choices of courses to assign
		for(int k = 0; k < coursesToAssign.size(); k++){
			courseAltern.add(new Integer(k));
		}
		
		//make a list of the indexes of different choices to make at this point
		found = false;
		altern = new LinkedList<Integer>();
		while(courseAltern.size() > 0){
			ranNum = new Random().nextInt(courseAltern.size());
			addingItem = coursesToAssign.get(courseAltern.remove(ranNum));
			//Altern creates different choices of time slots
			for(int k = 0; k < currentState.timeSlots.size(); k++){
				altern.add(new Integer(k));
			}
			if(altern.size() == 0)
				return false;
			while(altern.size() > 0){
				destinationTimeslot = currentState.timeSlots.get(altern.remove(new Random().nextInt(altern.size())));
				if(Constr.assign(destinationTimeslot, addingItem, FD.incompatible)){
				found = destinationTimeslot.addItemToTimeslot(addingItem);
				if(found){
					nxtCoursesToAssign = (LinkedList<courseItem>)coursesToAssign.clone();
					nxtCoursesToAssign.remove(ranNum);
					if(fillStateRecursive(nxtCoursesToAssign))
						return true;
					else
						removeCourseFromTimeslot(addingItem, destinationTimeslot);
				}
				}
			}
		}
		return false;
	}
	
	private void removeCourseFromTimeslot(courseItem removeThis, Timeslot fromHere){
		for(int i = 0; i < fromHere.assignedItems.size(); i++){
			if(removeThis.isSameCourseItems(fromHere.assignedItems.get(i)))
				fromHere.assignedItems.remove(i);
		}
	}
}
