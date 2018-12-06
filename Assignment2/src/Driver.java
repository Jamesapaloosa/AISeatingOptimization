import java.util.Arrays;
import java.util.LinkedList;

import java.util.Scanner;
public class Driver {
	public static void main(String[] args) {
		State currentState;
		long startTime;
		long endTime;
		long duration;

		FileData inputFileData;
		
		if(args.length == 9){
			EvalData.readCommandLineArg(args);
		}else{
			//command line inputs required here
			EvalData.promptUserForValues();
		}
		

		
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
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};
		
		//preassigned courses to a time-slot and setup all of the time-slots based on imported data
		startTime = System.currentTimeMillis();
		currentState = StateMaker.convertFromFileData(inputFileData);
		Constr.items = ((LinkedList<courseItem>)inputFileData.getCourses().clone());
		Constr.items.addAll(inputFileData.getLabs());
//		if(!Constr.check813913Pairs()){
//			System.out.println("Failed to create any or tree solutions for the provided state.  Problem is unsolvable");
//			return;
//		}
		
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("making the initial state speed: " + duration);
		courseItem newItem;
		LinkedList<courseItem> nightCourses = new LinkedList<courseItem>();

		//Or tree
		startTime = System.currentTimeMillis();
		OrTree thisOrTree;
		LinkedList<State> InitialStates = new LinkedList<State>();
		if(!Constr.partial(currentState, inputFileData.incompatible, inputFileData.preAssigned, inputFileData.unwanted)){
			System.out.println("invalid start state for the provided state.  Problem is unsolvable");
			return;
		}
		for(int i = 0; i < DataParser.generationSize; i = InitialStates.size()){
			thisOrTree = new OrTree(new State(currentState), inputFileData);
				if(thisOrTree.fillStateRecursive((LinkedList<courseItem>)thisOrTree.currentState.getCoursesLabsToAssign().clone())){
					InitialStates.add(thisOrTree.currentState);
					System.out.print(".");
				}
				else{
					System.out.println("Failed to create any or tree solutions for the provided state.  Problem is unsolvable");
					return;
				}
		}
		if(InitialStates.size() == 0){
			System.out.println("Failed to create or trees because of constr final");
			return;
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
	}
}
