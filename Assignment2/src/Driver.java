
public class Driver {
	public static void main(String[] args) {
		State currentState = new State();
		long startTime;
		long endTime;
		long duration;
		
		//Code to call and parse file
		startTime = System.nanoTime();
		DataParser inputFileParser = new DataParser(args[0]);
		try{
			FileData inputFileData = inputFileParser.readfile();
		}catch(Exception e){
			
		}
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("input file parser speed: " + duration);
		
		
		//need to assign pre assigned courses to a timeslot
		//need to setup all of the timeslots based on imported data
		
		//Or tree
		startTime = System.nanoTime();
		OrTree thisOrTree = new OrTree(currentState);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("or tree speed: " + duration);
		
		
		//Genetic algorithm here
		//
		
		startTime = System.nanoTime();
		OutputGenerator output = new OutputGenerator(currentState);
		output.OutputResultToCommandLine();
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("Ouput generator speed: " + duration);
		
	}
}
