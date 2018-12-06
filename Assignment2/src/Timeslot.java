import java.util.Arrays;
import java.util.LinkedList;

public class Timeslot {
	Slot localSlot;
	boolean forCourses;
	public LinkedList<courseItem> assignedItems;
	
	public Timeslot(Slot inSlot) {
		localSlot = inSlot;
		forCourses = inSlot.isForCourses;
		assignedItems = new LinkedList<courseItem>();
	}
	
	//Method to add an item to this time slot that allows the user to know if the item was correctly added.
	public Boolean addItemToTimeslot(courseItem newItem, FileData FD) {
		if(localSlot.startTime.contentEquals("11:00") && forCourses && localSlot.day.contentEquals("TU")){
			return false;
		}
		if(!Constr.noDuplicates(assignedItems))
			return false;
		
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};

		if(newItem.isALec == true){
			String lecNum = newItem.section;
			if (lecNum.charAt(0) == '9'){
				if (!Arrays.stream(eveningSlots).anyMatch(this.localSlot.startTime::equals))
					return false;
			}	
			
		}
		
		//Deal with 913 and 813 constraints
		int inNumber = Integer.parseInt(newItem.number);
		int otherNum;
		if(((inNumber == 913)||(inNumber == 813))&&!((localSlot.startTime.contentEquals("18:00"))&&(localSlot.day.contentEquals("TU"))))
			return false;
		
		if(inNumber == 413){
			for(int i = 0; i < this.assignedItems.size(); i++){
				if(Integer.parseInt(this.assignedItems.get(i).number) == 913)
					return false;
			}
		}
		else if(inNumber == 913){
			for(int i = 0; i < this.assignedItems.size(); i++){
				if(Integer.parseInt(this.assignedItems.get(i).number) == 413)
					return false;
			}
		}
		
		if(inNumber == 313){
			for(int i = 0; i < this.assignedItems.size(); i++){
				if(Integer.parseInt(this.assignedItems.get(i).number) == 813)
					return false;
			}
		}
		else if(inNumber == 813){
			for(int i = 0; i < this.assignedItems.size(); i++){
				if(Integer.parseInt(this.assignedItems.get(i).number) == 313)
					return false;
			}
		}
		
		
		courseItem present;
		courseItem OtherItem;
		CoursePair CP;
		//Confirm not compatible
		for(int i = 0; i < this.assignedItems.size(); i++){
			present = this.assignedItems.get(i);
			for(int j = 0; j < FD.incompatible.size(); j++){
				CP = FD.incompatible.get(j);
				if(CP.itemOne.isSameCourseItems(present)){
					if(CP.itemTwo.isSameCourseItems(newItem))
						return false;
				}else if (CP.itemTwo.isSameCourseItems(present)){
					if(CP.itemOne.isSameCourseItems(newItem))
						return false;
				}
			}
		}
		TimeCoursePair CTP;
		//Confirm not unwanted
		for(int i = 0; i < FD.unwanted.size(); i++){
			CTP = FD.unwanted.get(i);
			if(CTP.item.isSameCourseItems(newItem) && CTP.time.isSameSlot(localSlot))
				return false;
		}
		
		//Confirm there are no other 500s present
		

		if((inNumber >= 500)&&(inNumber < 600)){
			for(int i = 0; i < this.assignedItems.size(); i++){
				otherNum =  Integer.parseInt(this.assignedItems.get(i).number);
				if(otherNum >= 500 && otherNum < 600)
					return false;
			}
		}
		
		//confirm course max is not violated
		if((assignedItems.size() < localSlot.getMax())) {
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
		Timeslot output = new Timeslot(new Slot(localSlot.Max, localSlot.Min, localSlot.startTime, localSlot.day, this.forCourses));
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
		if(localSlot.isSameSlot(otherSlot.localSlot)&&(forCourses && otherSlot.forCourses)){
			return true;
		}
		else if((!forCourses && !otherSlot.forCourses) && (localSlot.isSameSlot(otherSlot.localSlot)))
			return true;
		return false;
	}
}
