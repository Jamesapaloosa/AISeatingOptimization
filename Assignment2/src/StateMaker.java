import java.util.LinkedList;
public class StateMaker {
	
	public static State convertFromFileData(FileData inData, boolean check, boolean twoCheck){
		
		State output = new State();
		output.setCoursesLabsToAssign((LinkedList<courseItem>)inData.getCourses().clone());
		output.getCoursesLabsToAssign().addAll(inData.getLabs());
		LinkedList<Timeslot> tempTime = output.getTimeSlots();
		Timeslot tempSlot;
		for(int i = 0; i < inData.getLabSlots().size(); i++){
			tempSlot = new Timeslot(inData.getLabSlots().get(i));
			tempTime.add(tempSlot);
		}
		for(int i = 0; i < inData.getCourseSlots().size();i++){
			tempSlot = new Timeslot(inData.getCourseSlots().get(i));
			tempTime.add(tempSlot);
		}
		

		Slot a = new Slot(2, 2, "18:00", "TU", true);
		if(twoCheck) {
			tempTime.add(new Timeslot(a));
			System.out.println("Ddawdw");
		}else if (check) {
			tempTime.add(new Timeslot(a));
		}
		
		TimeCoursePair tempPair;
		Timeslot destinationTime;
		boolean found;
		for(int i = 0; i < inData.getPreAssigned().size(); i++){
			found = false;
			tempPair = inData.getPreAssigned().get(i);
			//Remove pre-assigned from to be assigned list
			for(int j = 0; j < output.getCoursesLabsToAssign().size(); j++){
				if(tempPair.item.isSameCourseItems(output.getCoursesLabsToAssign().get(j)))
					output.getCoursesLabsToAssign().remove(j);
			}
			//Assign courses as per file data
			for(int j = 0; j < output.getTimeSlots().size(); j++){
				destinationTime = output.getTimeSlots().get(j);
				if(destinationTime.localSlot.isSameSlot(tempPair.time)){
					if(destinationTime.addItemToTimeslot(tempPair.item, inData)){
						found = true;
						break;
					}
				}
			}
			if(!found)
				throw new IllegalArgumentException("Unexpected error when trying to add: " + tempPair.item.department + " " + tempPair.item.number + " " + tempPair.item.lec);
		}
		return output;
	}
}
