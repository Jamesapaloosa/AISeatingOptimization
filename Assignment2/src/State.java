import java.util.LinkedList;
public class State {
	LinkedList<Timeslot> timeSlots;
	LinkedList<courseItem> CoursesLabsToAssign;
	int eval_Value; 
	boolean isSolvable;
	
	//Creates a new state
	public State(){
		timeSlots = new LinkedList<Timeslot>();
		CoursesLabsToAssign = new LinkedList<courseItem>();
		eval_Value = -1;
		isSolvable = true;
	}
	
	//Creates a state that is a copy of the fed state
	public State(State inState){
		timeSlots = new LinkedList<Timeslot>();
		CoursesLabsToAssign = new LinkedList<courseItem>();
		for(int i = 0; i < inState.timeSlots.size(); i++){
			timeSlots.add(inState.timeSlots.get(i).Copy());
		}
		for(int i = 0; i < inState.CoursesLabsToAssign.size(); i++){
			CoursesLabsToAssign.add(inState.CoursesLabsToAssign.get(i).copy());
		}
		eval_Value = inState.eval_Value;
		isSolvable = inState.isSolvable;
	}
	
	public LinkedList<Timeslot> getTimeSlots() {
		return timeSlots;
	}
	public void setTimeSlots(LinkedList<Timeslot> timeSlots) {
		this.timeSlots = timeSlots;
	}
	public LinkedList<courseItem> getCoursesLabsToAssign() {
		return CoursesLabsToAssign;
	}
	public void setCoursesLabsToAssign(LinkedList<courseItem> coursesToAssign) {
		CoursesLabsToAssign = coursesToAssign;
	}
	public int getEval_Value() {
		return eval_Value;
	}
	public void setEval_Value(int eval_Value) {
		this.eval_Value = eval_Value;
	}
	public boolean isSolvable() {
		return isSolvable;
	}
	public void setSolvable(boolean isSolvable) {
		this.isSolvable = isSolvable;
	}
}
