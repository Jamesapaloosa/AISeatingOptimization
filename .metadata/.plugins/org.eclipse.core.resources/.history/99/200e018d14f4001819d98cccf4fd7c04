import java.util.Arrays;
import java.util.LinkedList;

public class Timeslot {
	Slot localSlot;
	boolean forCourses;
	public LinkedList<courseItem> assignedItems;
	
	public Timeslot(Slot inSlot, boolean isForCourses) {
		localSlot = inSlot;
		forCourses = isForCourses;
		assignedItems = new LinkedList<courseItem>();
	}
	
	//Method to add an item to this time slot that allows the user to know if the item was correctly added.
	public Boolean addItemToTimeslot(courseItem newItem) {
		if((assignedItems.size() <= localSlot.getMax())) {
			boolean validCourse = forCourses && (Arrays.stream(DataParser.validLecType).anyMatch(newItem.getLecVsTut()::equals) && newItem.getTutVLab() == "");
			boolean validTut = !forCourses && (Arrays.stream(DataParser.validTutType).anyMatch(newItem.getLecVsTut()::equals) || Arrays.stream(DataParser.validTutType).anyMatch(newItem.getTutVLab()::equals));
			if(validCourse || validTut){
				assignedItems.add(newItem);
				return true;
			}
		}
		return false;
	}
	
	//Copy a time-slot
	public Timeslot Copy(){
		Timeslot output = new Timeslot(new Slot(localSlot.Max, localSlot.Min, localSlot.startTime, localSlot.day), this.forCourses);
		for(int i = 0; i < assignedItems.size(); i++){
			output.assignedItems.add(this.assignedItems.get(i).copy());
		}
		return output;
	}
	
	//Removes the last item in this time slot from this location and returns that Item if it can.
	public courseItem popItemFromTimeslot() {
		if(assignedItems.size() > 0) {
			return assignedItems.removeLast();
		}
		return null;
	}
	
	public Slot getLocalSlot() {
		return localSlot;
	}
	
	public LinkedList<courseItem> getAssignedItems() {
		return assignedItems;
	}
	
	public boolean equals(Timeslot otherSlot){
		if(localSlot.isSameSlot(otherSlot.localSlot)&&(forCourses == otherSlot.forCourses)){
			return true;
		}
		return false;
	}
	
}
