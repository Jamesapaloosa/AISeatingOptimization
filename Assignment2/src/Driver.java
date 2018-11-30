import java.util.LinkedList;

import java.util.Scanner;
public class Driver {
	public static void main(String[] args) {
		State currentState;
		long startTime;
		long endTime;
		long duration;

		FileData inputFileData;
		
		//command line inputs required here
		EvalData.promptUserForValues();
		
		//Code to call and parse file
		startTime = System.currentTimeMillis();
		DataParser inputFileParser = new DataParser(args[0]);
		try{
			inputFileData = inputFileParser.readfile();
		}catch(Exception e){
			System.out.println("Fatal error in inputFileParser method! " + e.getMessage());
			return;
		}
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("input file parser speed: " + duration);
		
		
		//preassigned courses to a time-slot and setup all of the time-slots based on imported data
		startTime = System.currentTimeMillis();
		currentState = StateMaker.convertFromFileData(inputFileData);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("input file parser speed: " + duration);
		
		
		//Or tree
		startTime = System.currentTimeMillis();
		OrTree thisOrTree;
		LinkedList<State> InitialStates = new LinkedList<State>();
		for(int i = 0; i < DataParser.generationSize; i = InitialStates.size()){
			thisOrTree = new OrTree(new State(currentState), inputFileData);
			if(thisOrTree.fillStateRecursive())
				InitialStates.add(thisOrTree.currentState);
		}		
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("or tree speed: " + duration);
		
		
		//Genetic algorithm here
		startTime = System.currentTimeMillis();
		Ext rules = new Ext(new Evaluator(inputFileData));
		currentState = rules.getOptomized(InitialStates, inputFileData);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("Genetic speed: " + duration);
		
		
		//Print output to the console.
		startTime = System.currentTimeMillis();
		OutputGenerator output = new OutputGenerator(currentState);
		output.OutputResultToCommandLine();
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("Ouput generator speed: " + duration);
	}
}
