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
		boolean Pres913 = false;
		boolean Pres813 = false;
		courseItem tempCourse;
		for(int i = 0; i < inputFileData.getCourses().size(); i++){
			tempCourse =  inputFileData.getCourses().get(i);
			if(tempCourse.department.contentEquals("CPSC")&& tempCourse.number.contentEquals("813"))
				Pres813 = true;
			if(tempCourse.department.contentEquals("CPSC")&& tempCourse.number.contentEquals("913"))
				Pres913 = true;
		}
		
		Slot a = new Slot(2, 2, "18:00", "TU", true);
		
		for(int i = 0; i < inputFileData.getCourses().size(); i++){
			tempCourse = inputFileData.getCourses().get(i);
			if(tempCourse.department.contentEquals("CPSC")&& tempCourse.number.contentEquals("413") && !Pres913) {
				inputFileData.preAssigned.add(new TimeCoursePair(a, new courseItem("CPSC", "913", "LEC", "01", true), 1));
			}
			else if((tempCourse.department.contentEquals("CPSC")&& tempCourse.number.contentEquals("313") && !Pres813)) {
				inputFileData.preAssigned.add(new TimeCoursePair(a, new courseItem("CPSC", "813", "LEC", "01", true), 1));
			}
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
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("making the initial state speed: " + duration);
		
		
		if(!Constr.noDuplicates(Constr.items))
			return;
		if(!Constr.noDuplicates(currentState.CoursesLabsToAssign))
			return;
		
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
				if(thisOrTree.fillStateRecursive((LinkedList<courseItem>)thisOrTree.currentState.getCoursesLabsToAssign().clone(), System.currentTimeMillis() + DataParser.orTreeTimeOut)){
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
		output.OutputResultToFile();
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
	}
}
