import java.util.LinkedList;
public class State {
	LinkedList<Timeslot> timeSlots;
	LinkedList<courseItem> CoursesToAssign;
	LinkedList<courseItem> LabsToAssign;
	int eval_Value; 
	boolean isSolvable;
	
	public LinkedList<Timeslot> getTimeSlots() {
		return timeSlots;
	}
	public void setTimeSlots(LinkedList<Timeslot> timeSlots) {
		this.timeSlots = timeSlots;
	}
	public LinkedList<courseItem> getCoursesToAssign() {
		return CoursesToAssign;
	}
	public void setCoursesToAssign(LinkedList<courseItem> coursesToAssign) {
		CoursesToAssign = coursesToAssign;
	}
	public LinkedList<courseItem> getLabsToAssign() {
		return LabsToAssign;
	}
	public void setLabsToAssign(LinkedList<courseItem> labsToAssign) {
		LabsToAssign = labsToAssign;
	}
}
