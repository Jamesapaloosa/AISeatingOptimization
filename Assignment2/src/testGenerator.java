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
	
	public void createCourse(){
		
	}
	
	public void createLab(){
		
	}
	
	public void createCourseSlot(){
		
	}
	
	public void createLabSlot(){
		
	}
	
	public void createIncompatible(){
		
	}
	
	public void createUnwanted(){
		
	}
	
	public void createPair(){
		
	}
	
	public void createPreference(){
		
	}
	
	public void preAssigned(){
		
	}
}
