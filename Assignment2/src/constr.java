/**
 * @author Christian Roatis
 *
 */

import java.util.*;

public class Constr {

//------------------------------------------------------------------------------------------------------------
//This section holds all functions that check the hard constraints of any given state

	// Ensure courseMax or labMax isn't violated in a current state
	public Boolean maxAndOverlapCheck(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		CourseItem item;

		int max = 0;
		int count = 0;

		// Check every timeslot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			labmax = currentSlot.localSlot.Max;

			if((currentSlot.assignedItems.size() <= currentSlot.localSlot.getMax())) {
				boolean validCourse = currentSlot.forCourses && (Arrays.stream(DataParser.validLecType).anyMatch(newItem.getLecVsTut()::equals) && newItem.getTutVLab() == "");
				boolean validTut = !currentSlot.forCourses && (Arrays.stream(DataParser.validTutType).anyMatch(newItem.getLecVsTut()::equals) || Arrays.stream(DataParser.validTutType).anyMatch(newItem.getTutVLab()::equals));
			
				if(validCourse || validTut){
					return true;
				}
			}
			
			return false;
		}

	}

	public Boolean tuesdayCourseCheck(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 

		// Check every timeslot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			labmax = currentSlot.localSlot.Max;

			if((currentSlot.localSlot.day.equals("TU"))&& (currentSlot.localSlot.startTime.equals("11:00"))) {
				if (currentSlot.assignedItems.isLec == false){
					return true;
				}
				
				else {
					return false;
				}
			}

		}
	}

	public Boolean eveningLecCheck(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};

		// Check every timeslot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			if ((currentSlot.assignedItems.get(i).isLec == true) && (currentSlot.assignedItems.get(i).section.equals("09"))) {
				if (Arrays.stream(eveningSlots).anyMatch(currentSlot.localSlot.startTime::equals)){
					return true;
				}
				
				else {
					return false;
				}
			}

		}
	}


	// Check that no two 500 level courses are assigned in the same slot in any given state
	public Boolean check500(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		int count = 0;

		// Check every timeslot in a state to make sure no more than one 500 level class occupies any one timeslot
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			if ((currentSlot.assignedItems.get(i).isLec == true) && (Integer.parseInt(currentSlot.assignedItems.get(i).number) >= 500) && (Integer.parseInt(currentSlot.assignedItems.get(i).number) < 600)) {
				count++;
				if (count > 1){
					return false;
				}
			}

		}
		
		return true;
	}

	// Deal with the complicated CPSC 813/913 scheduling and overlap rules
	public Boolean check13(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 

		// Ensure CPSC 813 and 913 are scheudled only during the TU timeslot starting at 18:00.
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			// If CPSC 813 or 913 are not scheduled TU at 18:00, return false
			if(!(currentSlot.localSlot.day.equals("TU")) && !(currentSlot.localSlot.startTime.equals("18:00"))) {
				if ((currentSlot.assignedItems.isLec == true) && ((currentSlot.assignedItems.number.equals(813)) || (currentSlot.assignedItems.number.equals(913)))){
					return false;
				}
			}


			// If CPSC 813 is scheduled TU at 18:00 but so is any element of CPSC 313, return false
			else if (((currentSlot.localSlot.day.equals("TU")) && (currentSlot.localSlot.startTime.equals("18:00"))) && (currentSlot.assignedItems.number.equals(813))){
				for (int j=0; j < currentSlot.assignedItems.size(); j++){
					if (currentSlot.assignedItems.get(j).number.equals("313")){
						return false;
					}
				}
			}
			
			// If CPSC 913 is scheduled TU at 18:00 but so is any element of CPSC 413, return false
			else if (((currentSlot.localSlot.day.equals("TU")) && (currentSlot.localSlot.startTime.equals("18:00"))) && (currentSlot.assignedItems.number.equals(913))){
				for (int j=0; j < currentSlot.assignedItems.size(); j++){
					if (currentSlot.assignedItems.get(j).number.equals("413")){
						return false;
					}
				}
			}			
				
			else {
				return true;
			}
		}
	}

	// Deal with the CPSC 813 overlap rules
	public Boolean check13(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 

		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			if(!(currentSlot.localSlot.day.equals("TU")) && !(currentSlot.localSlot.startTime.equals("18:00"))) {
				if ((currentSlot.assignedItems.isLec == true) && ((currentSlot.assignedItems.number.equals(813)) || (currentSlot.assignedItems.number.equals(913)))){
					return false;
				}
			}
				
			else {
				return true;
			}
		}
	}


//------------------------------------------------------------------------------------------------------------
//This section holds all functions that check the hard constraints when attempting an assignment

	// Check that you won't have more than one 500 level course in a given timeslot
	public Boolean eveningLecAssign(Timeslot ts, courseItem ci){
		Timeslot timeSlot = ts; 
		courseItem item = ci;
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};

		if(item.isLec == true){
			String lecNum = item.section;

			if (lecNum.equals("09")){
				if (Arrays.stream(eveningSlots).anyMatch(timeSlot.localSlot.startTime::equals)){
					return true;
				}
				
				else {
					return false;
				}
			}	
			
		}

		return true;
	}


	// Check every assignment in a timeslot to ensure there are no other 500 level courses currently assigned
	public Boolean assign500(Timeslot ts){
		Timeslot timeSlot = ts; 

		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			if ((currentSlot.assignedItems.get(i).isLec == true) && (Integer.parseInt(currentSlot.assignedItems.get(i).number) > 500) && (Integer.parseInt(currentSlot.assignedItems.get(i).number) < 600)){
				return false;
			}
		}
		
		return true;
	}

	// When assigning either CPSC 813 or 913, it must be assigned to TU at 18:00
	public Boolean assign13(Timeslot ts, courseItem ci){
		Timeslot timeslot = ts;
		courseItem item = ci;

		if ((item.number.equals("813") && (item.isLec == true))){
			if ((!(timeslot.localSlot.day.equals("TU")) || !(timeslot.localSlot.startTime.equals ("18:00")))){	
				return false;
			}
		}
		
		else if (item.number.equals("913") && (item.isLec == true)){
			if (!(timeslot.localSlot.day.equals("TU")) || !(timeslot.localSlot.startTime.equals ("18:00"))){	
				return false;
			}
		}		

		else{
			return true;
		}
		
	}



//------------------------------------------------------------------------------------------------------------
//This section holds all the complete check possiblities for Constr; includes Constr.assign, Constr.partial and Constr.final

	// Run Constr on a final solution
	public Boolean final(State currentState){
		State state = currentState;

		if (!(state.CoursesToAssign.isEmpty()) && !(state.LabsToAssign.isEmpty())){
			if ((maxAndOverlapCheck(state)) && (tuesdayCourseCheck(state)) && eveningLecCheck(state) && check500(state) && check13(state))
			return true;
		}

		else
			return false;
	}
	
	// Run Constr on a partial solution
	public Boolean partial(State currentState){
		State state = currentState;

		if ((maxAndOverlapCheck(state)) && (tuesdayCourseCheck(state)))
			return true;
		else
			return false;
	}

	// Run Constr on a state
	public Boolean partial(State currentState){
		State state = currentState;

		if ((maxAndOverlapCheck(state)) && (tuesdayCourseCheck(state)))
			return true;
		else
			return false;
	}


	public static void main(String[] args){

	}

}