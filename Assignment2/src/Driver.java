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
		Constr.items = ((LinkedList<courseItem>)inputFileData.getCourses().clone());
		Constr.items.addAll(inputFileData.getLabs());

		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("making the initial state speed: " + duration);
		
		
		//Or tree
		startTime = System.currentTimeMillis();
		OrTree thisOrTree;
		LinkedList<State> InitialStates = new LinkedList<State>();
		for(int i = 0; i < DataParser.generationSize; i = InitialStates.size()){
			thisOrTree = new OrTree(new State(currentState), inputFileData);
			if(thisOrTree.fillStateRecursive(thisOrTree.currentState.getCoursesLabsToAssign(), System.currentTimeMillis() + DataParser.orTreeTimeOut)){
				if(Constr.finalCheck(thisOrTree.currentState, inputFileData.incompatible, inputFileData.preAssigned, inputFileData.unwanted)){
					InitialStates.add(thisOrTree.currentState);
					System.out.print(".");
				}
			}
			else{
				System.out.println("Failed to create any or tree solutions for the provided state.  Problem is unsolvable");
				return;
			}
		}
		System.out.println(".");
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("or tree speed: " + duration);
		
		
		//Genetic algorithm here
		startTime = System.currentTimeMillis();
		Ext rules = new Ext(new Evaluator(inputFileData), currentState);
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
