import java.util.Collections;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
public class OutputGenerator {
	State resultState;
	LinkedList<String> outputData;
	
	//Constructor that takes in a state
	public OutputGenerator(State inState){
		outputData = new LinkedList<String>();
		resultState = inState;
	}
	
	//Send the results to a file: 
	public void OutputResultToFile(){
		sortResults();
		try{
			PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
			writer.println("Eval Value: " + resultState.eval_Value);
			for(int i = 0; i < outputData.size(); i++){
				writer.println(outputData.get(i));
			}
			writer.close();
		}catch(Exception e){
			System.out.println("Error trying to write to file: " + e.getMessage());
		}
	}
	
	//Send the results to the command line
	public void OutputResultToCommandLine(){
		sortResults();
		System.out.println("Eval Value: " + resultState.eval_Value);
		for(int i = 0; i < outputData.size(); i++){
			System.out.println(outputData.get(i));
		}
	}
	
	//Sort data into a alphabetical order by class
	public void sortResults(){
		String temp;
		Slot time;
		courseItem item;
		for(int i = 0; i < resultState.getTimeSlots().size(); i++){
			time =  resultState.getTimeSlots().get(i).localSlot;
			for(int j = 0; j < resultState.getTimeSlots().get(i).getAssignedItems().size(); j++){
				item = resultState.getTimeSlots().get(i).getAssignedItems().get(j);
				if(item.getTutVLab() == "")
					item.tutVLab = "     ";
				temp = item.department + " " + item.number + " " + item.lec + " " + item.section + " " + item.getTutVLab() + " " + item.tutSection + "     : ";
				temp = temp + time.getDay() + ", " + time.getStartTime();
				outputData.add(temp);
			}
		}
		Collections.sort(outputData, String.CASE_INSENSITIVE_ORDER);
	}
	
}
