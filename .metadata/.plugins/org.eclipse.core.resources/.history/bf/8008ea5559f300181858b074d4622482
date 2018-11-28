import java.util.LinkedList;
public class Driver {
	public static void main(String[] args) {
		State currentState;
		long startTime;
		long endTime;
		long duration;

		FileData inputFileData;
		//command line inputs required here
		
		
		//Code to call and parse file
		startTime = System.nanoTime();
		DataParser inputFileParser = new DataParser(args[0]);
		try{
			inputFileData = inputFileParser.readfile();
		}catch(Exception e){
			System.out.println("Fatal error in inputFileParser method! ");
			return;
		}
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("input file parser speed: " + duration);
		
		
		//preassigned courses to a time-slot and setup all of the time-slots based on imported data
		startTime = System.nanoTime();
		currentState = StateMaker.convertFromFileData(inputFileData);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("input file parser speed: " + duration);
		
		
		//Or tree
		startTime = System.nanoTime();
		OrTree thisOrTree;
		LinkedList<State> InitialStates = new LinkedList<State>();

		for(int i = 0; i < DataParser.generationSize; i = InitialStates.size()){
			thisOrTree = new OrTree(new State(currentState), inputFileData);
			if(thisOrTree.fillStateRecursive())
				InitialStates.add(thisOrTree.currentState);
		}
		
		thisOrTree = new OrTree(new State(currentState), inputFileData);
		thisOrTree.fillStateRecursive();
		currentState = thisOrTree.currentState; 
				
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("or tree speed: " + duration);
		
		
		//Genetic algorithm here
		/*startTime = System.nanoTime();
		Ext rules = new Ext(InitialStates);
		currentState = rules.getOptomized(InitialStates);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("or tree speed: " + duration);
		*/
		
		//Print output to the console.
		startTime = System.nanoTime();
		OutputGenerator output = new OutputGenerator(currentState);
		output.OutputResultToCommandLine();
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("Ouput generator speed: " + duration);
		
	}
}
