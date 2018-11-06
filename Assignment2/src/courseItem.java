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
	
	public courseItem(String inDepartment, String inNumber, String inLecVsTut, String inSection) {
		department= inDepartment;
		number = inNumber;
		lec = inLecVsTut;
		section = inSection;
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
