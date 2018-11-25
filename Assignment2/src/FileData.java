import java.util.LinkedList;
//Version
public class FileData {
	LinkedList<courseItem> courses;
	LinkedList<courseItem> labs;
	String name;
	LinkedList<Slot> courseSlots;
	LinkedList<Slot> labSlots;
	LinkedList<CoursePair> incompatible;
	LinkedList<TimeCoursePair> unwanted;
	LinkedList<CoursePair> pair;
	LinkedList<TimeCoursePair> preferences;
	LinkedList<TimeCoursePair> preAssigned;
	
	public FileData(){
		courses = new LinkedList<courseItem>();
		labs = new LinkedList<courseItem>();
		courseSlots = new LinkedList<Slot>();
		labSlots = new LinkedList<Slot>();
		incompatible = new LinkedList<CoursePair>();
		unwanted = new LinkedList<TimeCoursePair>();
		pair = new LinkedList<CoursePair>();
		preferences = new LinkedList<TimeCoursePair>();
		preAssigned = new LinkedList<TimeCoursePair>();
	}
	
	public LinkedList<courseItem> getCourses() {
		return courses;
	}
	public void setCourses(LinkedList<courseItem> courses) {
		this.courses = courses;
	}
	public LinkedList<courseItem> getLabs() {
		return labs;
	}
	public void setLabs(LinkedList<courseItem> labs) {
		this.labs = labs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LinkedList<Slot> getCourseSlots() {
		return courseSlots;
	}
	public void setCourseSlots(LinkedList<Slot> courseSlots) {
		this.courseSlots = courseSlots;
	}
	public LinkedList<Slot> getLabSlots() {
		return labSlots;
	}
	public void setLabSlots(LinkedList<Slot> labSlots) {
		this.labSlots = labSlots;
	}
	public LinkedList<CoursePair> getIncompatible() {
		return incompatible;
	}
	public void setIncompatible(LinkedList<CoursePair> incompatible) {
		this.incompatible = incompatible;
	}
	public LinkedList<TimeCoursePair> getUnwanted() {
		return unwanted;
	}
	public void setUnwanted(LinkedList<TimeCoursePair> unwanted) {
		this.unwanted = unwanted;
	}
	public LinkedList<CoursePair> getPair() {
		return pair;
	}
	public void setPair(LinkedList<CoursePair> pair) {
		this.pair = pair;
	}
	public LinkedList<TimeCoursePair> getPreferences() {
		return preferences;
	}
	public void setPreferences(LinkedList<TimeCoursePair> preferences) {
		this.preferences = preferences;
	}
	public LinkedList<TimeCoursePair> getPreAssigned() {
		return preAssigned;
	}
	public void setPreAssigned(LinkedList<TimeCoursePair> preAssigned) {
		this.preAssigned = preAssigned;
	}
	
	
	
}