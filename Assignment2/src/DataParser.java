import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*; 
public class DataParser {
	String sourcefile;
	FileData dataOutput;
	
	//The following are universal data sources that are for use across the machine
	public static String[] validDays = {"MO", "TU", "FR"};
	public static String[] validMondays = {"Mo", "mO", "mo", "MO"};
	public static String[] validTuesdays = {"TU", "Tu", "tu", "tU"};
	public static String[] validFridays = {"FR", "Fr", "fr", "fR"};
	
	public static Map<String, String> CourseMondayEndTimes;
	public static Map<String, String> CourseTuesdayEndTimes;
	public static Map<String, String> TutMondayTuesdayEndTimes;
	public static Map<String, String> TutFridayEndTimes;
	
	public static String[] validCourseMondayTimes = {"8:00","9:00", "10:00", "11:00","12:00","13:00","14:00", "15:00","16:00", "17:00","18:00","19:00","20:00"};
	public static String[] validTutMondayTuesdayTimes = {"8:00","9:00", "10:00", "11:00","12:00","13:00","14:00", "15:00","16:00", "17:00","18:00","19:00","20:00"};
	public static String[] validCourseTuesdayTimes = {"8:00", "9:30","11:00","12:30","14:00","15:30","17:00","18:30", "18:00"};
	public static String[] validLabFridayTimes = {"8:00", "10:00", "12:00", "14:00", "16:00", "18:00"};
	
	public static String[] validClassType = {"LEC", "TUT", "LAB", "tut", "lab", "lec"};
	public static String[] validTutType = {"TUT", "LAB", "tut", "lab"};
	public static String[] validLecType = {"LEC", "lec"};
	public static String[] validDepartments = {"CPSC", "SENG"};
	public static String[] validSectionNum = {"01", "02", "03", "04", "05", "06", "07", "09"};
	public static String[] invalidDepartmentChar = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "?"};
	
	public static String emptyData = "<EMPTY>";
	public static String[] FileDataHeaders = {"Name", "Course slots:", "Lab slots:","Courses:","Labs:", "Not compatible:","Unwanted:","Preferences:", "Pair:","Partial assignments:"};
	
	public static int generationSize = 10;
    public static int generationMultiplier = 10;
    public static int generationsWithoutChangeForResult = 200000000;
    public static int generationMutationModifier = 1000;
    public static long orTreeTimeOut = 2500;
    public static double percentOfTopTenToTake = .5;
	
	//
	public DataParser(String infile) {
		if(infile == "") {
			sourcefile = "E:\\CPSC433\\testCase1.txt";
		}else {
			sourcefile = infile;
		}
		CourseMondayEndTimes = new HashMap<String, String>();
		setCourseMondayEndTimes();

		CourseTuesdayEndTimes = new HashMap<String, String>();
		setCourseTuesdayEndTimes();
		
		TutMondayTuesdayEndTimes = new HashMap<String, String>();
		setTutMondayTuesdayEndTimes();
		
		TutFridayEndTimes = new HashMap<String, String>();
		setTutFridayEndTimes();
		
	}
	
	//Set the dictionary with the end times for each start time
	private void setTutFridayEndTimes(){
		TutFridayEndTimes.put("8:00", "10:00");
		TutFridayEndTimes.put("10:00", "12:00");
		TutFridayEndTimes.put("12:00", "14:00");
		TutFridayEndTimes.put("14:00", "16:00");
		TutFridayEndTimes.put("16:00", "18:00");
		TutFridayEndTimes.put("18:00", "20:00");
		
	}
	
	//Set the dictionary with the tuesday course end times
	private void setCourseTuesdayEndTimes(){
		CourseTuesdayEndTimes.put("8:00", "9:30");
		CourseTuesdayEndTimes.put("9:30", "11:00");
		CourseTuesdayEndTimes.put("11:00", "12:30");
		CourseTuesdayEndTimes.put("12:30", "14:00");
		CourseTuesdayEndTimes.put("14:00", "15:30");
		CourseTuesdayEndTimes.put("15:30", "17:00");
		CourseTuesdayEndTimes.put("17:00", "18:30");
		CourseTuesdayEndTimes.put("18:30", "20:00");
	}
	
	//Set the tutorial monday tuesday times
	private void setTutMondayTuesdayEndTimes(){
		CourseTuesdayEndTimes.put("8:00", "9:00");
		CourseTuesdayEndTimes.put("9:00", "10:00");
		CourseTuesdayEndTimes.put("10:00", "11:00");
		CourseTuesdayEndTimes.put("11:00", "12:00");
		CourseTuesdayEndTimes.put("12:00", "13:00");
		CourseTuesdayEndTimes.put("13:00", "14:00");
		CourseTuesdayEndTimes.put("14:00", "15:00");
		CourseTuesdayEndTimes.put("15:00", "16:00");
		CourseTuesdayEndTimes.put("16:00", "17:00");
		CourseTuesdayEndTimes.put("17:00", "18:00");
		CourseTuesdayEndTimes.put("18:00", "19:00");
		CourseTuesdayEndTimes.put("19:00", "20:00");
		CourseTuesdayEndTimes.put("20:00", "21:00");
	}
	
	//Set course endings for Monday courses
	private void setCourseMondayEndTimes(){
		CourseMondayEndTimes.put("8:00", "9:00");
		CourseMondayEndTimes.put("9:00", "10:00");
		CourseMondayEndTimes.put("10:00", "11:00");
		CourseMondayEndTimes.put("11:00", "12:00");
		CourseMondayEndTimes.put("12:00", "13:00");
		CourseMondayEndTimes.put("13:00", "14:00");
		CourseMondayEndTimes.put("14:00", "15:00");
		CourseMondayEndTimes.put("15:00", "16:00");
		CourseMondayEndTimes.put("16:00", "17:00");
		CourseMondayEndTimes.put("17:00", "18:00");
		CourseMondayEndTimes.put("18:00", "19:00");
		CourseMondayEndTimes.put("19:00", "20:00");
		CourseMondayEndTimes.put("20:00", "21:00");
		
	}
	
	
	
	//------------------------------------------------------------------------------------------------------------
	//This method reads the file and adds data to the file data object and returns everything from the input file into
	//a format for the program to read and do optimization on.
	public FileData readfile() throws IOException {
		File file = new File(sourcefile);
		dataOutput = new FileData();
		courseItem tempCourseItem;
		int rowNum = 0;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}
		boolean readNewLine = true;
		String line = "";
		while (true){
			//Switch to bounce between different data sets
			if(readNewLine){
				if((line = br.readLine()) == null)
					break;
			}
			else
				readNewLine = true;
			switch(line){
			case "Name:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.setName(line);
				}
				break;
		
			case "Course slots:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getCourseSlots().add(readCourseSlot(line, rowNum, true));
				}
				break;
			case "Lab slots:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getLabSlots().add(readCourseSlot(line, rowNum, false));
				}
				break;
			case "Courses:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getCourses().add(readCourseLine(line, rowNum));
				}
				break;
			case "Labs:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0){
						tempCourseItem = readCourseLine(line, rowNum);
						dataOutput.getLabs().add(tempCourseItem);
						//Add an incompatible pair for each lab and its lecture
						dataOutput.incompatible.add(new CoursePair(tempCourseItem, new courseItem(tempCourseItem.department, tempCourseItem.number, validLecType[0], tempCourseItem.section, true)));
					}
				}
				break;
			case "Not compatible:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getIncompatible().add(readCoursePair(line, rowNum));
				}
				break;
			case "Unwanted:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getUnwanted().add(readTimeCoursePair(line, rowNum, true));
				}
				break;
			case "Preferences:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getPreferences().add(readTimeCoursePair(line, rowNum, true));
				}
				break;
			case "Pair:":
				while(true){
					line = br.readLine();
					rowNum++;
					if((Arrays.stream(FileDataHeaders).anyMatch(line::equals))||(line == null)){
						readNewLine = false;
						break;
					}
					if(line.length() > 0)
						dataOutput.getPair().add(readCoursePair(line, rowNum));
				}
				break;
			case "Partial assignments:":
				while(true){
					line = br.readLine();
					rowNum++;
					if(line != null){
						if(line.length() > 0)
							dataOutput.getPreAssigned().add(readTimeCoursePair(line, rowNum, false));
					}
					else 
						break;
				}
				break;
			}
		}
		return dataOutput;
	}
	//------------------------------------------------------------------------------------------------------------
	//Overload for possibility of directly throwing in a file path
	public void readfile(String infile) throws IOException {
		sourcefile = infile;
		readfile();
	}
	//------------------------------------------------------------------------------------------------------------
	//Method for reading a line that contains a course or lab data set
	private courseItem readCourseLine(String input, int rowNum) {
		courseItem outCL;
		input = input.trim();
		String[] dataSet = input.split("\\s+");
		//Type verification for each of the input types
		if(!Arrays.stream(validClassType).anyMatch(dataSet[2]::equals))
			throw new IllegalArgumentException("Invalid input " + dataSet[2] + " is not a valid Class type on row: " + rowNum);
		try{
			int temp = Integer.parseInt(dataSet[1]);
			if((temp < 100)||(temp > 999))
				throw new IllegalArgumentException("Invalid input " + dataSet[1] + " is not a valid course number on row: " + rowNum);
		}catch(NumberFormatException e){
			throw new IllegalArgumentException("Invalid course number " + dataSet[1] + " on row: " + rowNum);
		}
		if((dataSet[0].length() != 4)||(Arrays.stream(invalidDepartmentChar).anyMatch(dataSet[0]::equals)))
			throw new IllegalArgumentException("Invalid department input " + dataSet[0] + " on row: " + rowNum);
		if((!Arrays.stream(validSectionNum).anyMatch(dataSet[3]::equals))&&(Integer.parseInt(dataSet[3]) >= 100))
			throw new IllegalArgumentException("Invalid Section number: " + dataSet[3] + " on row: " + rowNum);
		
		//Create a new course item based on the amount of data provided.
		if(dataSet.length == 4){
			boolean isCourse = true;
			if(Arrays.stream(validTutType).anyMatch(dataSet[2]::equals))
				isCourse = false;
			outCL = new courseItem(dataSet[0], dataSet[1], dataSet[2], dataSet[3], isCourse);
		}
		else if (dataSet.length == 6){
			//Type verification if there are the two extra data points
			if(!Arrays.stream(validTutType).anyMatch(dataSet[4]::equals))
				throw new IllegalArgumentException("Invalid type for the lab: " + dataSet[4] + " on row: " + rowNum);
			if((!Arrays.stream(validSectionNum).anyMatch(dataSet[5]::equals))&&(Integer.parseInt(dataSet[5]) >= 100))
				throw new IllegalArgumentException("Invalid Section number: " + dataSet[5] + " on row: " + rowNum);
			outCL = new courseItem(dataSet[0], dataSet[1], dataSet[2], dataSet[3], dataSet[4], dataSet[5], false);
		}
		else
			throw new IllegalArgumentException("Unexpected number of arguments (" + dataSet.length + ") for Course: " + input + " on row: " + rowNum);
		return outCL;
	}
	
	//------------------------------------------------------------------------------------------------------------
	//Method for reading a line that contains a slot data set 
	private Slot readCourseSlot(String input, int rowNum, boolean isForCourses){
		Slot outSlot;
		input = input.trim();
		String[] dataSet = input.split("\\s*,\\s*");
		if(dataSet.length != 4)
			throw new IllegalArgumentException("The number of arguments found in row: " + rowNum + " is incorrect.");
		if(!Arrays.stream(validDays).anyMatch(dataSet[0]::equals))
			throw new IllegalArgumentException("Invalid format for day found in: " + dataSet[0] + " in row: " + rowNum);
		switch(dataSet[0]){
		case "MO":	
			if(!Arrays.stream(validCourseMondayTimes).anyMatch(dataSet[1]::equals))
				throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
			break;
		case "TU":
			if((!Arrays.stream(validCourseTuesdayTimes).anyMatch(dataSet[1]::equals))&&((!Arrays.stream(validTutMondayTuesdayTimes).anyMatch(dataSet[1]::equals))))
				throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
			break;
		case "FR":
			if(!Arrays.stream(validLabFridayTimes).anyMatch(dataSet[1]::equals))
				throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
		}
		int tempMax = Integer.parseInt(dataSet[2]);
		int tempMin = Integer.parseInt(dataSet[3]);
		if(tempMax < tempMin)
			throw new IllegalArgumentException("The format for the max and min for a slot are invalid: " + dataSet[2] + ", " + dataSet[3] + "in row: " + rowNum);
		outSlot = new Slot(tempMax, tempMin, dataSet[1], dataSet[0], isForCourses);
		return outSlot;
	}
	//------------------------------------------------------------------------------------------------------------
	//Method for reading a line that contains a pair of courses
	private CoursePair readCoursePair(String input, int rowNum){
		input = input.trim();
		String[] dataSet = input.split("\\s*,\\s*");
		if(dataSet.length != 2)
			throw new IllegalArgumentException("Unexpected number of arguments in row: " + rowNum);
		courseItem ItemOne = readCourseLine(dataSet[0], rowNum);
		courseItem ItemTwo = readCourseLine(dataSet[1], rowNum); 
		return new CoursePair(ItemOne, ItemTwo);
	}
	//------------------------------------------------------------------------------------------------------------
	//Method for reading a line that contains a pair of time and course data sets.
	private TimeCoursePair readTimeCoursePair(String input, int rowNum, boolean isPref){
		input = input.trim();
		String day = "";
		String time = "";
		courseItem tempCourse;
		Slot tempSlot;
		int classIndex = 0;
		String[] dataSet = input.split("\\s*,\\s*");
		if(dataSet.length == 3){
			for(int i = 0; i < dataSet.length; i++){
				if((Arrays.stream(validDays).anyMatch(dataSet[i]::equals))){
					day = dataSet[i];
					switch(day){
					case "MO":	
						if(!Arrays.stream(validCourseMondayTimes).anyMatch(dataSet[i + 1]::equals))
							throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
						break;
					case "TU":
						if((!Arrays.stream(validCourseTuesdayTimes).anyMatch(dataSet[i + 1]::equals))&&((!Arrays.stream(validTutMondayTuesdayTimes).anyMatch(dataSet[i + 1]::equals))))
							throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
						break;
					case "FR":
						if(!Arrays.stream(validLabFridayTimes).anyMatch(dataSet[i + 1]::equals))
							throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
					}
					time = dataSet[i+ 1];
					if(i == 0)
						classIndex = i + 2;
					else
						classIndex = i - 1;
					break;
				}
			}
			if((day == "")||(time == ""))
				throw new IllegalArgumentException("Valid day or time were not found for row: " + rowNum);
			tempCourse = readCourseLine(dataSet[classIndex], rowNum);
			if(isPref)
				tempSlot = new Slot(0, 0, time, day, tempCourse.isALec, isPref);
			else
				tempSlot = new Slot(0, 0, time, day, tempCourse.isALec);
			return new TimeCoursePair(tempSlot, tempCourse, 0);
		}
		else if(dataSet.length == 4){
			for(int i = 0; i < dataSet.length; i++){
				if((Arrays.stream(validDays).anyMatch(dataSet[i]::equals))){
					day = dataSet[i];
					switch(day){
					case "MO":	
						if(!Arrays.stream(validCourseMondayTimes).anyMatch(dataSet[i + 1]::equals))
							throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
						break;
					case "TU":
						if((!Arrays.stream(validCourseTuesdayTimes).anyMatch(dataSet[i + 1]::equals))&&((!Arrays.stream(validTutMondayTuesdayTimes).anyMatch(dataSet[i + 1]::equals))))
							throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
						break;
					case "FR":
						if(!Arrays.stream(validLabFridayTimes).anyMatch(dataSet[i + 1]::equals))
							throw new IllegalArgumentException("Invalid format for time found in: " + dataSet[1] + " in row: " + rowNum);
					}
					time = dataSet[i+ 1];
					if(i == 0)
						classIndex = i + 2;
					else
						classIndex = i - 1;
					break;
				}
			}
			if((day == "")||(time == ""))
				throw new IllegalArgumentException("Valid day or time were not found for row: " + rowNum);
			tempCourse = readCourseLine(dataSet[classIndex], rowNum);
			if(isPref)
				tempSlot = new Slot(0, 0, time, day, tempCourse.isALec, isPref);
			else
				tempSlot = new Slot(0, 0, time, day, tempCourse.isALec);
			return new TimeCoursePair(tempSlot, tempCourse, Integer.parseInt(dataSet[3]));
		}
		throw new IllegalArgumentException("Unexpected number of arguments in row: " + rowNum);
	}
	
	
}