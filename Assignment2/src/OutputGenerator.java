import java.util.Collections;
import java.util.LinkedList;
public class OutputGenerator {
	State resultState;
	LinkedList<String> outputData;
	
	//Constructor that takes in a state
	public OutputGenerator(State inState){
		outputData = new LinkedList<String>();
		resultState = inState;
	}
	
	//Send the results to a file: NOT FINISHED
	public void OutputResultToFile(String path){
		sortResults();
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
				temp = item.department + " " + item.number + " " + item.lec + " " + item.section + " " + item.getTutVLab() + " " + item.tutSection + "     : ";
				temp = temp + time.getDay() + ", " + time.getStartTime();
				outputData.add(temp);
			}
		}
		Collections.sort(outputData, String.CASE_INSENSITIVE_ORDER);
	}
	
}
