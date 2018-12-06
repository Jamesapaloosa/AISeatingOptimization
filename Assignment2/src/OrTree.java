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
	public boolean fillStateRecursive(LinkedList<courseItem> coursesToAssign, long endtime){
		if(endtime < System.currentTimeMillis())
			return true;
		courseItem addingItem;
		Timeslot destinationTimeslot;
		int courseIndexInToAssign;
		courseItem temp;
		LinkedList<courseItem> nxtCoursesToAssign;
		LinkedList<Integer> altern;
		LinkedList<Integer> courseAltern = new LinkedList<Integer>();
		int foundToRemove;

		if(!Constr.noDuplicates(coursesToAssign))
			throw new IllegalArgumentException("duplicates in courses to assign");
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
					temp = removeCourseFromList(addingItem, nxtCoursesToAssign);
					
					
					if(!temp.isSameCourseItems(addingItem)){
						throw new IllegalArgumentException("course removed does not match the added item");
					}
					if(Constr.partial(currentState, FD.incompatible, FD.preAssigned, FD.unwanted)&&fillStateRecursive(nxtCoursesToAssign, endtime))
						return true;
					else{
						foundToRemove = removeCourseFromState(addingItem);
						if(foundToRemove < 1){
							System.out.println("");
							System.out.println("coursesToAssign");
							for(int i = 0; i < coursesToAssign.size(); i++){
								System.out.println(coursesToAssign.get(i).department + " " + coursesToAssign.get(i).number + " " + coursesToAssign.get(i).section + " " +  coursesToAssign.get(i).tutVLab + " " + coursesToAssign.get(i).tutSection);
							}
							System.out.println("");
							System.out.println("nxtCoursesToAssign");
							for(int i = 0; i < nxtCoursesToAssign.size(); i++){
								System.out.println(nxtCoursesToAssign.get(i).department + " " + nxtCoursesToAssign.get(i).number + " " + nxtCoursesToAssign.get(i).section + " " +  nxtCoursesToAssign.get(i).tutVLab + " " + nxtCoursesToAssign.get(i).tutSection);
							}
							
							throw new IllegalArgumentException("could not find an Item to remove " + addingItem.department + " " + addingItem.number + " " + addingItem.section + " " +  addingItem.tutVLab + " " + addingItem.tutSection);
						}
						else if (foundToRemove > 1){
							System.out.println("found duplicates to remove " + addingItem.department + " " + addingItem.number + " " + addingItem.section + " " +  addingItem.tutVLab + " " + addingItem.tutSection);
							return false;
						}
							
					}
				}
			}
		}
		return false;
	}
	
	private courseItem removeCourseFromList(courseItem removeThis, LinkedList<courseItem> fromThis){
		courseItem temp;
		for(int i = 0; i < fromThis.size(); i++){
			temp = fromThis.get(i);
			if(temp.isSameCourseItems(removeThis))
					return fromThis.remove(i);
		}
		
		return null;
	}
	
	private int removeCourseFromState(courseItem removeThis){
        Timeslot fromSlot;
        Timeslot toSlot;
        courseItem fromCourse;
        courseItem toCourse;
        int count = 0;
        for(int k = 0; k < currentState.timeSlots.size(); k++){
        	toSlot = currentState.timeSlots.get(k);
        	for(int l = 0; l < toSlot.assignedItems.size(); l++){
        		toCourse = toSlot.assignedItems.get(l);
        		if(removeThis.isSameCourseItems(toCourse)){
        			toSlot.assignedItems.remove(l);
        			count++;
        		}
        	}
        }
       return count;
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
