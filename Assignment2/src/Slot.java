import java.util.Arrays;

public class Slot {
	String startTime;
	String endTime;
	String day;
	int Max;
	int Min;
	boolean isForCourses;
	//constructor for a slot that that is not a preference which means it has to be a real slot
	public Slot(int inMax, int inMin, String inStart, String inDay, boolean isForCourses) {
		Max = inMax;
		Min = inMin;
		startTime = inStart;
		day = inDay;
		this.isForCourses = isForCourses;
		setEndTime(isForCourses);
	}
	
	//constructor for a slot that is a preference and so does not need to be a real slot
	public Slot(int inMax, int inMin, String inStart, String inDay, boolean isForCourses, boolean isPref) {
		Max = inMax;
		Min = inMin;
		startTime = inStart;
		day = inDay;
		this.isForCourses = isForCourses;
	}
	
	public String getEndTime(){
		return endTime;
	}
	
	public void setEndTime(String endTimeIn){
		endTime = endTimeIn;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getDay() {
		return day;
	}
	
	public int getMax() {
		return Max;
	}
	
	public int getMin() {
		return Min;
	}
	
	//Method that confirms this is a valid slot and creates the end time
	private void setEndTime(boolean isForCourses){
		String lecVTut;
		if(isForCourses){
			lecVTut = " Lecture";
			if(Arrays.stream(DataParser.validMondays).anyMatch(this.getDay()::equals)){
				if(!Arrays.stream(DataParser.validCourseMondayTimes).anyMatch(this.getStartTime()::equals))
					throw new IllegalArgumentException("Invalid Timeslot " + this.day + " at " + this.startTime + lecVTut);
				this.setEndTime(DataParser.CourseMondayEndTimes.get(this.startTime));
			}
			else if(Arrays.stream(DataParser.validTuesdays).anyMatch(this.getDay()::equals)){
				if(!Arrays.stream(DataParser.validCourseTuesdayTimes).anyMatch(this.getStartTime()::equals))
					throw new IllegalArgumentException("Invalid Timeslot " + this.day + " at " + this.startTime + lecVTut);
				this.setEndTime(DataParser.CourseTuesdayEndTimes.get(this.startTime));
			}
			else
				throw new IllegalArgumentException("Invalid Timeslot " + this.day + " at " + this.startTime  + lecVTut);
		}
		else{
			lecVTut = " Lab";
			if((Arrays.stream(DataParser.validMondays).anyMatch(this.getDay()::equals))||(Arrays.stream(DataParser.validTuesdays).anyMatch(this.getDay()::equals))){
				if(!Arrays.stream(DataParser.validTutMondayTuesdayTimes).anyMatch(this.getStartTime()::equals))
					throw new IllegalArgumentException("Invalid Timeslot " + this.day + " at " + this.startTime + lecVTut);
				this.setEndTime(DataParser.TutMondayTuesdayEndTimes.get(this.startTime));
			}
			else if(Arrays.stream(DataParser.validFridays).anyMatch(this.getDay()::equals)){
				if(!Arrays.stream(DataParser.validLabFridayTimes).anyMatch(this.getStartTime()::equals))
					throw new IllegalArgumentException("Invalid Timeslot " + this.day + " at " + this.startTime + lecVTut);
				this.setEndTime(DataParser.TutFridayEndTimes.get(this.startTime));
			}
			else
				throw new IllegalArgumentException("Invalid Timeslot " + this.day + " at " + this.startTime  + lecVTut);
		}
	}

	//Method for making sure that two slots are the same, does not compare max and min because those are not always relevant
	public Boolean isSameSlot(Slot OtherSlot){
		if(!OtherSlot.getStartTime().equals(this.startTime))
			return false;
		if(!OtherSlot.getDay().equals( this.day))
			return false;
		return true;
	}
}
