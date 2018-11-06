
public class TimeCoursePair {
	Slot time;
	courseItem item;
	
	public Slot getTime() {
		return time;
	}
	
	public courseItem getCourseItem() {
		return item;
	}
	
	public TimeCoursePair(Slot inTime, courseItem inItem){
		time = inTime;
		item = inItem;
	}
}
