
public class TimeCoursePair {
	Slot time;
	courseItem item;
	int prefVal;
	
	public Slot getTime() {
		return time;
	}
	
	public courseItem getCourseItem() {
		return item;
	}
	
	public int getPrefVal(){
		return prefVal;
	}
	
	public TimeCoursePair(Slot inTime, courseItem inItem, int inPrefVal){
		time = inTime;
		item = inItem;
		prefVal = inPrefVal;
	}
}
