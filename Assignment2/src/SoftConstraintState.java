import java.util.*;

public class SoftConstraintState {
	
	LinkedList<CoursePair> course_pairs;
	LinkedList<TimeCoursePair> time_course_pairs;
	
	//A SoftConstraintState object needs a FileData object to be constructed
	public SoftConstraintState(FileData inFileData){
		course_pairs = (LinkedList<CoursePair>)inFileData.pair.clone();
		time_course_pairs = inFileData.getPreferences();
	}
	
	//By passing in a list of States, getSoftState will return a state that violates soft constraints
	//Most checks here are the same as in Evaluator
	public Timeslot getSoftState(LinkedList <Timeslot> states) {
		Collections.shuffle(states);
		for (Timeslot slot: states) {
			
			//EvalMinFilled
			int course_count = 0;
			int lab_count = 0;
			for (courseItem item : slot.getAssignedItems())
			{
				if (item.isALec) course_count++;
				else lab_count++;
			}
			
			if ((!slot.forCourses) && (slot.getLocalSlot().getMin() > lab_count)) {
				return slot;
			}
			else if ((slot.forCourses) && (slot.getLocalSlot().getMin() > course_count)) { 
				return slot;
			}
			
			//EvalPref
			for (TimeCoursePair tcp : time_course_pairs){
				
				if (slot.getLocalSlot().isSameSlot(tcp.getTime()) && ((slot.forCourses && tcp.getCourseItem().isALec)||(!slot.forCourses && !tcp.getCourseItem().isALec)))
				{
					
					//("got here" + ts.getLocalSlot().getDay() + ts.getLocalSlot().getStartTime());
					if (!existsInTimeslot(tcp.getCourseItem(), slot))
					{
						return slot;
					}
				}
			}
			
			//EvalPair
			for (CoursePair aCoursePair : course_pairs)
			{
				if ((!existsInTimeslot(aCoursePair.getItemOne(), slot) & existsInTimeslot(aCoursePair.getItemTwo(), slot)) || (existsInTimeslot(aCoursePair.getItemOne(), slot) & !existsInTimeslot(aCoursePair.getItemTwo(), slot))) {
					return slot;
				}
			}
			
			//EvalSecDiff
			if (getSectionPairs(slot).size() > 0) {
				return slot;
			}
		}
		
		return null;
	}
	
	public Boolean existsInTimeslot(courseItem inItem, Timeslot inTimeSlot)
	{
		for (courseItem aItem : inTimeSlot.getAssignedItems())
		{
			if (inItem.isSameCourseItems(aItem)) return true;
		}
		return false;
	}
	
	public LinkedList<CoursePair> getSectionPairs(Timeslot inTimeSlot)
	{
		LinkedList<CoursePair> sec_pairs = new LinkedList<CoursePair>();
		for (int i = 0; i < (inTimeSlot.getAssignedItems().size() - 1); i++)
		{	
			//(inTimeSlot.getAssignedItems().get(i).getNumber() + " " + inTimeSlot.getAssignedItems().get(i).getSection() + inTimeSlot.getAssignedItems().get(i).getTutVLab());
			for (int j =  i + 1; j < inTimeSlot.getAssignedItems().size(); j++)
			{
				//("item j is: " + inTimeSlot.getAssignedItems().get(j).getNumber() + " " + inTimeSlot.getAssignedItems().get(j).getSection() + inTimeSlot.getAssignedItems().get(j).getTutVLab());
				//("j is: " + j);
				if (isSameCourseDifferentSection(inTimeSlot.getAssignedItems().get(i), inTimeSlot.getAssignedItems().get(j)))
				{
					//("got here");
					sec_pairs.add(new CoursePair(inTimeSlot.getAssignedItems().get(i), inTimeSlot.getAssignedItems().get(j)));
				}
			}
		}
		//("Eval for Sec pair size is: " + sec_pairs.size());
		return sec_pairs;
	}
	
	public Boolean isSameCourseDifferentSection(courseItem inItem1, courseItem inItem2)
	{
		if(!inItem1.getDepartment().equals(inItem2.getDepartment())) {
			//("got here 1" + inItem1.getDepartment() + inItem2.getDepartment());
			return false;
		}
		if(!inItem1.getNumber().equals(inItem2.getNumber())) {
			//("got here 1" + inItem1.getNumber() + inItem2.getNumber());
			return false;
		}
		if(!inItem1.getLecVsTut().equals(inItem2.getLecVsTut())) {
			//("got here 1" + inItem1.getLecVsTut() + inItem2.getLecVsTut());
			return false;
		}
		if(inItem1.getSection().equals(inItem2.getSection())) {
			//("got here 1" + inItem1.getSection() + inItem2.getSection());
			return false;
		}
		if(!inItem1.getTutVLab().equals(inItem2.getTutVLab())) {
			//("got here 1" + inItem1.getTutVLab() + inItem2.getTutVLab());
			return false;
		}
		if(!inItem1.getTutSection().equals(inItem2.getTutSection())) {
			//("got here 1" + inItem1.getTutSection() + inItem2.getTutSection());
			return false;
		}
		return true;
	}
}
