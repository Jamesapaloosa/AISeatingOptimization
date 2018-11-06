import java.util.LinkedList;

public class Timeslot {
	Slot localSlot;
	LinkedList<courseItem> assignedItems;
	
	public Timeslot(Slot inSlot) {
		localSlot = inSlot;
		assignedItems = new LinkedList<courseItem>();
	}
	
	public Boolean addItemToTimeslot(courseItem newItem) {
		if(assignedItems.size() <= localSlot.getMax()) {
			assignedItems.add(newItem);
			return true;
		}
		return false;
	}
	
	public courseItem removeItemFromTimeslot() {
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
	
}
