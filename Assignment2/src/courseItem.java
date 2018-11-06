/**
 * @author James Gilders
 *
 */
public class courseItem {
	public String department;
	public String number;
	public String lec;
	public String section;
	public String tutVLab;
	public String tutSection;
	
	//Constructor that is used in the case of a single course or a tutorial that is for a course with only one section.
	public courseItem(String inDepartment, String inNumber, String inLecVsTut, String inSection) {
		department= inDepartment;
		number = inNumber;
		lec = inLecVsTut;
		section = inSection;
		tutVLab = "";
		tutSection = "";
	}
	
	//Constructor that is used in the case of multiple tutorials for the same section of a course
	public courseItem(String inDepartment, String inNumber, String inLecVsTut, String inSection, String inTutVLab, String inTutSection) {
		department= inDepartment;
		number = inNumber;
		lec = inLecVsTut;
		section = inSection;
		tutVLab = inTutVLab;
		tutSection = inTutSection;
	}
	
	//compare two course items to see if they are the same;
	public Boolean isSameCourseItems(courseItem OtherCourseItem){
		if(department != OtherCourseItem.getDepartment())
			return false;
		if(number != OtherCourseItem.getNumber())
			return false;
		if(lec != OtherCourseItem.getLecVsTut())
			return false;
		if(section != OtherCourseItem.getSection())
			return false;
		if(tutVLab != OtherCourseItem.getTutVLab())
			return false;
		if(tutSection != OtherCourseItem.getTutSection())
			return false;
		return true;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getLecVsTut() {
		return lec;
	}
	
	public String getSection() {
		return section;
	}
	
	public String getTutVLab() {
		return tutVLab;
	}
	
	public String getTutSection() {
		return tutSection;
	}
}
