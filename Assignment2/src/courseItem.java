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
	public boolean isALec;
	
	//Constructor that is used in the case of a single course or a tutorial that is for a course with only one section.
	public courseItem(String inDepartment, String inNumber, String inLecVsTut, String inSection, boolean inIsALec) {
		department= inDepartment;
		number = inNumber;
		lec = inLecVsTut;
		section = inSection;
		tutVLab = "";//DataParser.emptyData;
		tutSection = "";//DataParser.emptyData;
		isALec = inIsALec;
	}
	
	//Constructor that is used in the case of multiple tutorials for the same section of a course
	public courseItem(String inDepartment, String inNumber, String inLecVsTut, String inSection, String inTutVLab, String inTutSection, boolean inIsALec) {
		department= inDepartment;
		number = inNumber;
		lec = inLecVsTut;
		section = inSection;
		tutVLab = inTutVLab;
		tutSection = inTutSection;
		isALec = inIsALec;
	}
	
	//Copy the item
	public courseItem copy(){
		courseItem output = new courseItem(department, number, lec, section, tutVLab, tutSection, isALec);
		return output;
	}
	
	//compare two course items to see if they are the same;
	public Boolean isSameCourseItems(courseItem OtherCourseItem){
		if(!department.contentEquals(OtherCourseItem.getDepartment()))
			return false;
		if(!number.contentEquals(OtherCourseItem.getNumber()))
			return false;
		if(!lec.contentEquals(OtherCourseItem.getLecVsTut()))
			return false;
		if(!section.contentEquals(OtherCourseItem.getSection()))
			return false;
		if((tutVLab == "")&&(OtherCourseItem.getTutVLab() == ""))
			return true;
		else if(!tutVLab.contentEquals(OtherCourseItem.getTutVLab()))
			return false;
		if((tutSection == "")&&(OtherCourseItem.getTutSection() == ""))
			return true;
		else if(!tutSection.contentEquals(OtherCourseItem.getTutSection()))
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
