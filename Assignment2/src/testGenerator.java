import java.io.File;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.io.IOException;

public class testGenerator {
	int coursesCount;
	int labsCount;
	String name;
	int courseSlotCount;
	int labSlotCount;
	int incompatibleCount;
	int unwantedCount;
	int pairCount;
	int preferencesCount;
	int preAssignedCount;
	FileWriter write;
	
	/*
	public testGenerator(String inDestination, String inName, int coursesCount, int inLabsCount, int inCourseSlotCount, int labSlotCount, int inIncompatibleCount, int inUnwantedCount, int inPairCount, int inPreferencesCount, int inPreAssignedCount ) throws IOException{
		File file = new File(inDestination);
		if(file.isDirectory())
			write = new FileWriter( inDestination + "RandomInput.txt" , false);
		else
			write = new FileWriter(inDestination, false);
		this.coursesCount = coursesCount;
		labsCount = inLabsCount;
		name = inName;
		courseSlotCount = inCourseSlotCount;
		this.labSlotCount = labSlotCount;
		incompatibleCount = inIncompatibleCount;
		unwantedCount = inUnwantedCount;
		pairCount = inPairCount;
		preferencesCount = inPreferencesCount;
		preAssignedCount = inPreAssignedCount;
	}
	
	
	//Size is a character either s, m, l, x, g with s being smallest and g being biggest 
	public testGenerator(char size, String inName, String destination) throws IOException{
		File file = new File(destination);
		name = inName;
		if(file.isDirectory())
			write = new FileWriter( destination + "RandomInput.txt" , false);
		else
			write = new FileWriter(destination, false);
		int ranSize;
		switch (size){
		case 's':
			ranSize = 5;
			break;
		case 'm':
			ranSize = 10;
			break;
		case 'l':
			ranSize = 50;
			break;
		case 'x':
			ranSize = 1000;
			break;
		case 'g':
			ranSize = 100000;
			break;
		default:
			ranSize = 20;
		}
		coursesCount = new Random().nextInt(ranSize);
		labsCount = new Random().nextInt(ranSize);
		courseSlotCount = new Random().nextInt(ranSize);
		labSlotCount = new Random().nextInt(ranSize);
		incompatibleCount = new Random().nextInt(ranSize);
		unwantedCount = new Random().nextInt(ranSize);
		pairCount = new Random().nextInt(ranSize);
		preferencesCount = new Random().nextInt(ranSize);
		preAssignedCount = new Random().nextInt(ranSize);
	}
	
	public void generateSourceData(){
		
	}
	
	public void createCourse() throws IOException{
		String courseItem;
		int courseNum = new Random().nextInt(899) + 100;
		String courseName = DataParser.validDepartments[new Random().nextInt(DataParser.validDepartments.length)];
		String Section = DataParser.validSectionNum[new Random().nextInt(DataParser.validSectionNum.length)];
		courseItem = courseName + " " + Integer.toString(courseNum) + " LEC " + Section;
		write.append(courseItem);
	}
	
	public void createLab() throws IOException{
		String labItem;
		int courseNum = new Random().nextInt(899) + 100;
		String courseName = DataParser.validDepartments[new Random().nextInt(DataParser.validDepartments.length)];
		String Section = DataParser.validSectionNum[new Random().nextInt(DataParser.validSectionNum.length)];
		String LabVsLec = DataParser.validClassType[new Random().nextInt(DataParser.validClassType.length)];
		if("LEC".equalsIgnoreCase(LabVsLec))
			labItem = courseName + " " + Integer.toString(courseNum) + " " + LabVsLec + " " + Section;
		else{
			String LabVal = DataParser.validTutType[new Random().nextInt(DataParser.validTutType.length)];
			String LabSection = DataParser.validSectionNum[new Random().nextInt(DataParser.validSectionNum.length)];
			labItem = courseName + " " + Integer.toString(courseNum) + " " + LabVsLec + " " + Section + " " +  LabVal + " " + LabSection;
		}
		write.append(labItem);
	}
	
	public void createSlot() throws IOException{
		String SlotItem;
		String Day = DataParser.validDays[new Random().nextInt(DataParser.validDays.length)];
		String time = DataParser.validTimes[new Random().nextInt(DataParser.validDays.length)];
		int max = new Random().nextInt(20);
		String min = Integer.toString(new Random().nextInt(max));
		SlotItem = Day + "," + time + "," + Integer.toString(max) + "," + min;
		write.append(SlotItem);
	}
	
	public void createNoMaxSlot() throws IOException{
		String SlotItem;
		String Day = DataParser.validDays[new Random().nextInt(DataParser.validDays.length)];
		String time = DataParser.validTimes[new Random().nextInt(DataParser.validDays.length)];
		SlotItem = Day + "," + time;
		write.append(SlotItem);
	}	
	
	public void createIncompatible() throws IOException{
		int chooseFirstItem = new Random().nextInt(1);
		int chooseSecondItem = new Random().nextInt(1);
		if(chooseFirstItem == 0)
			createCourse();
		else
			createLab();
		write.append(",");
		if(chooseSecondItem == 0)
			createCourse();
		else
			createLab();
	}
	
	public void createUnwanted() throws IOException{
		int chooseFirstItem = new Random().nextInt(1);
		if(chooseFirstItem == 0)
			createCourse();
		else
			createLab();
		write.append(",");
		createSlot();
	}
	
	public void createPair() throws IOException{
		int chooseFirstItem = new Random().nextInt(1);
		int chooseSecondItem = new Random().nextInt(1);
		if(chooseFirstItem == 0)
			createCourse();
		else
			createLab();
		write.append(",");
		if(chooseSecondItem == 0)
			createCourse();
		else
			createLab();
	}
	
	public void createPreference() throws IOException{
		int chooseFirstItem = new Random().nextInt(1);
		if(chooseFirstItem == 0)
			createCourse();
		else
			createLab();
		createNoMaxSlot();
	}
	
	public void preAssigned() throws IOException{
		int chooseFirstItem = new Random().nextInt(1);
		if(chooseFirstItem == 0)
			createCourse();
		else
			createLab();
		createNoMaxSlot();
	}
	*/
}
