/**
 * @author Christian Roatis
 *
 */

import java.util.*;

public class Constr {

//------------------------------------------------------------------------------------------------------------
//This section holds all functions that check the hard constraints of any given state

	// Ensure courseMax or labMax isn't violated in a current state
	private static Boolean maxAndOverlapCheck(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		courseItem item;

		int max = 0;
		int count = 0;

		// Check every time-slot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			if((currentSlot.assignedItems.size() <= currentSlot.localSlot.getMax())) {
				for(int j = 0; j < currentSlot.assignedItems.size(); j++){
					boolean validCourse = currentSlot.forCourses && (Arrays.stream(DataParser.validLecType).anyMatch(currentSlot.assignedItems.get(j).getLecVsTut()::equals) && currentSlot.assignedItems.get(j).getTutVLab() == "");
					boolean validTut = !currentSlot.forCourses && (Arrays.stream(DataParser.validTutType).anyMatch(currentSlot.assignedItems.get(j).getLecVsTut()::equals) || Arrays.stream(DataParser.validTutType).anyMatch(currentSlot.assignedItems.get(j).getTutVLab()::equals));
					if(!validCourse && !validTut)
						return false;
				}
			}
		}
		return true;
	}
	
	private static Boolean noDuplicates(State currentState) {
        State state = currentState;
        LinkedList<Timeslot> timeslots = state.timeSlots;
        LinkedList<courseItem> items = new LinkedList<courseItem>();
        courseItem currentItem;

        for (int i=0; i < timeslots.size(); i++){    
            Timeslot currentSlot = timeslots.get(i);
            for (int j = 0; j < currentSlot.assignedItems.size(); j++){
                currentItem = currentSlot.assignedItems.get(j);

                for (int k=0; k<items.size(); k++){
                    if(currentItem.isSameCourseItems(items.get(k))){
                        return false;
                    } 
                }
                items.add(currentItem);
            }
        }
        return true;
    }

	private static Boolean tuesdayCourseCheck(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 

		// Check every timeslot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			//labmax = currentSlot.localSlot.Max;

			if((currentSlot.localSlot.day.equals("TU"))&& (currentSlot.localSlot.startTime.equals("11:00"))) {
				if (currentSlot.forCourses == false){}
					
				else 
					return false;
			}

		}
		return true;
	}

	private static Boolean eveningLecCheck(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};

		// Check every timeslot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			if ((currentSlot.assignedItems.get(i).isALec == true) && (currentSlot.assignedItems.get(i).section.equals("09"))) {
				if (Arrays.stream(eveningSlots).anyMatch(currentSlot.localSlot.startTime::equals)){}
				else 
					return false;
			}

		}
		return true;
	}


	// Check that no two 500 level courses are assigned in the same slot in any given state
	private static Boolean check500(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		int count = 0;

		// Check every timeslot in a state to make sure no more than one 500 level class occupies any one timeslot
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			if ((currentSlot.assignedItems.get(i).isALec == true) && (Integer.parseInt(currentSlot.assignedItems.get(i).number) >= 500) && (Integer.parseInt(currentSlot.assignedItems.get(i).number) < 600)) {
				count++;
				if (count > 1){
					return false;
				}
			}

		}
		return true;
	}

	// Deal with the complicated CPSC 813/913 scheduling and overlap rules
	private static Boolean check13_2(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 

		// Ensure CPSC 813 and 913 are scheudled only during the TU timeslot starting at 18:00.
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);

			for(int j = 0; j < currentSlot.assignedItems.size(); j++){
				// If CPSC 813 or 913 are not scheduled TU at 18:00, return false
				if(!(currentSlot.localSlot.day.equals("TU")) && !(currentSlot.localSlot.startTime.equals("18:00"))) {
					if ((currentSlot.assignedItems.get(j).isALec == true) && ((currentSlot.assignedItems.get(j).number.equals(813)) || (currentSlot.assignedItems.get(j).number.equals(913)))){
						return false;
					}
				}
	
	
				// If CPSC 813 is scheduled TU at 18:00 but so is any element of CPSC 313, return false
				else if (((currentSlot.localSlot.day.equals("TU")) && (currentSlot.localSlot.startTime.equals("18:00"))) && (currentSlot.assignedItems.get(j).number.equals(813))){
						if (currentSlot.assignedItems.get(j).number.equals("313")){
							return false;
						}
				}
				
				// If CPSC 913 is scheduled TU at 18:00 but so is any element of CPSC 413, return false
				else if (((currentSlot.localSlot.day.equals("TU")) && (currentSlot.localSlot.startTime.equals("18:00"))) && (currentSlot.assignedItems.get(j).number.equals(913))){
						if (currentSlot.assignedItems.get(j).number.equals("413")){
							return false;
						}
				}		
			}
		}
		return true;
	}

	// Deal with the CPSC 813 overlap rules
	private static Boolean check13(State currentState){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 

		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			for(int j = 0; j < currentSlot.assignedItems.size(); j++){
				if(!(currentSlot.localSlot.day.equals("TU")) && !(currentSlot.localSlot.startTime.equals("18:00"))) {
					if ((currentSlot.assignedItems.get(j).isALec == true) && ((currentSlot.assignedItems.get(j).number.equals(813)) || (currentSlot.assignedItems.get(j).number.equals(913)))){
						return false;
					}
				}
			}
		}
		return true;
	}

		// Check incompatible classes aren't scheduled at the same times
	private static Boolean checkIncompatible(State currentState, LinkedList<CoursePair> inc){
		State state = currentState;
		LinkedList<Timeslot> timeslots = state.timeSlots; 
		LinkedList<CoursePair> incompClasses = inc; 
		int incompItems = 0;

		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			int counter = 0;

			for (int j=0; j < incompClasses.size(); j++){
				incompItems = 0;
				CoursePair cp = incompClasses.get(j);
				courseItem c1 = cp.getItemOne();
				courseItem c2 = cp.getItemTwo();

				for (int k=0; k < timeslots.get(i).assignedItems.size(); k++){
					courseItem item = timeslots.get(i).assignedItems.get(k);

					if(item.isSameCourseItems(c1)||item.isSameCourseItems(c2)){
						incompItems++;
						if(incompItems > 1)
							return false;
					}
				}
			}
		}
		return true;
	}


//------------------------------------------------------------------------------------------------------------
//This section holds all functions that check the hard constraints when attempting an assignment

	// Check that you won't have more than one 500 level course in a given timeslot
	private static Boolean eveningLecAssign(Timeslot ts, courseItem ci){
		Timeslot timeSlot = ts; 
		courseItem item = ci;
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};

		if(item.isALec == true){
			String lecNum = item.section;
			if (lecNum.equals("09")){
				if (Arrays.stream(eveningSlots).anyMatch(timeSlot.localSlot.startTime::equals))
					return true;
				else 
					return false;
			}	
			
		}
		return true;
	}


	// Check every assignment in a timeslot to ensure there are no other 500 level courses currently assigned
	private static Boolean assign500(Timeslot timeSlot){
		for (int i=0; i < timeSlot.assignedItems.size(); i++)
			if ((timeSlot.assignedItems.get(i).isALec == true) && (Integer.parseInt(timeSlot.assignedItems.get(i).number) > 500) && (Integer.parseInt(timeSlot.assignedItems.get(i).number) < 600))
				return false;
		return true;
	}

	// When assigning either CPSC 813 or 913, it must be assigned to TU at 18:00
	private static Boolean assign13(Timeslot ts, courseItem ci){
		Timeslot timeslot = ts;
		courseItem item = ci;

		if ((item.number.equals("813") && (item.isALec == true))){
			if ((!(timeslot.localSlot.day.equals("TU")) || !(timeslot.localSlot.startTime.equals ("18:00")))){	
				return false;
			}
		}
		
		else if (item.number.equals("913") && (item.isALec == true)){
			if (!(timeslot.localSlot.day.equals("TU")) || !(timeslot.localSlot.startTime.equals ("18:00"))){	
				return false;
			}
		}		
		return true;
	}
	
	// Check incompatible classes aren't scheduled at the same times
	private static Boolean checkIncompatibleAssign(Timeslot ts, courseItem ci, LinkedList<CoursePair> inc){
		courseItem item = ci;
		Timeslot timeslot = ts; 
		LinkedList<CoursePair> incompClasses = inc; 
		int incompItems = 0;
	
		for (int i=0; i < timeslot.assignedItems.size(); i++){	
	
			for (int j=0; j < incompClasses.size(); j++){
				incompItems = 0;
				CoursePair cp = incompClasses.get(j);
				courseItem c1 = cp.getItemOne();
				courseItem c2 = cp.getItemTwo();
				
				if(item.isSameCourseItems(c1) || item.isSameCourseItems(c2)){
					incompItems++;
				}
				
				for (int k=0; k < timeslot.assignedItems.size(); k++){
					courseItem currentItem = timeslot.assignedItems.get(k);
	
					if(currentItem.isSameCourseItems(c1)||currentItem.isSameCourseItems(c2)){
						incompItems++;
						if(incompItems > 1)
							return false;
					}
				}
			}
		}
		return true;
	}



//------------------------------------------------------------------------------------------------------------
//This section holds all the complete check possiblities for Constr; includes Constr.assign, Constr.partial and Constr.final

	// Run Constr on a final solution
	public static Boolean finalCheck(State currentState){
		State state = currentState;

	
		if ((maxAndOverlapCheck(state)) && (tuesdayCourseCheck(state)) && eveningLecCheck(state) && check500(state) && check13(state) && noDuplicates(state)){
			if (!(state.CoursesLabsToAssign.isEmpty())){	
				return false;
			}
			return true;
		}
		

		else
			return false;
	}
	

	// Run Constr on a partial solution
	public static Boolean partial(State currentState){
		State state = currentState;

		if ((maxAndOverlapCheck(state)) && (tuesdayCourseCheck(state)))
			return true;
		else
			return false;
	}

	
	// Run Constr on an assignment
	public static Boolean assign(Timeslot ts, courseItem ci, LinkedList<CoursePair> inc){
		
		if (eveningLecAssign(ts, ci) && assign500(ts) && assign13(ts, ci) && checkIncompatibleAssign(ts, ci, inc)){
			return true;
		}
		else
			return false;

	}


}