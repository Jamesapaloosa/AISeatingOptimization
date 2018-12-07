/**
 * @author Christian Roatis
 * 
 * The Constr class ensures all hard constraints are satisfied. Most functions have two versions.
 * One based on a state input (used for the genetic algorithm and to confirm valid solutions) 
 * and for assignments, usually more efficient, used by the or-tree
 *
 */

import java.util.*;

public class Constr {

    static LinkedList<Timeslot> timeslots = new LinkedList<Timeslot>();
    static LinkedList<courseItem> items = new LinkedList<courseItem>();
  
//------------------------------------------------------------------------------------------------------------
//This section holds all functions that check the hard constraints of any given state
    /*
	// Ensure courseMax or labMax isn't violated in a current state
	private static Boolean maxAndOverlapCheck(State currentState){
		//state = currentState;
		//timeslots = state.timeSlots; 
		/*
		// Check every time-slot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			Timeslot currentSlot = timeslots.get(i);
			for(int j = 0; j < currentSlot.assignedItems.size(); j++){
				boolean validCourse = currentSlot.forCourses && (Arrays.stream(DataParser.validLecType).anyMatch(currentSlot.assignedItems.get(j).getLecVsTut()::equals) && currentSlot.assignedItems.get(j).getTutVLab() == "");
				boolean validTut = !currentSlot.forCourses && (Arrays.stream(DataParser.validTutType).anyMatch(currentSlot.assignedItems.get(j).getLecVsTut()::equals) || Arrays.stream(DataParser.validTutType).anyMatch(currentSlot.assignedItems.get(j).getTutVLab()::equals));
				if(!validCourse && !validTut)
					return false;
			}
		}
		
		return true;
	}
	*/
    
    public static boolean check813913Pairs(){
    	boolean foundhigh = false;
    	boolean foundlow = false;
    	courseItem thisItem;
    	for(int i = 0; i < items.size(); i++){
    		thisItem = items.get(i);
    		if(thisItem.isALec && thisItem.number.contentEquals("813") && thisItem.department.contentEquals("CPSC"))
    			foundhigh = true;
    		else if((thisItem.isALec && thisItem.number.contentEquals("313") && thisItem.department.contentEquals("CPSC")))
    			foundlow = true;
    	}
    	if((foundhigh && !foundlow) ||(!foundhigh && foundlow))
    		return false;
    	foundhigh = false;
    	foundlow = false;
    	for(int i = 0; i < items.size(); i++){
    		thisItem = items.get(i);
    		if(thisItem.isALec && thisItem.number.contentEquals("913") && thisItem.department.contentEquals("CPSC"))
    			foundhigh = true;
    		else if((thisItem.isALec && thisItem.number.contentEquals("413") && thisItem.department.contentEquals("CPSC")))
    			foundlow = true;
    	}
    	if((foundhigh && !foundlow) ||(!foundhigh && foundlow))
    		return false;
    	return true;
    }
    		
    
	//Makes sure all classes that are needed are contained in the timeslots
	private static Boolean confirmAllClassesAssigned(State currentState){
		Timeslot timeslot;
		int LoopSize;
		LinkedList<courseItem> valuesToFind = (LinkedList<courseItem>)items.clone();
		boolean found;
		for(int j = 0; j < currentState.timeSlots.size(); j++){
			timeslot = currentState.timeSlots.get(j);
			LoopSize = timeslot.assignedItems.size();
			for(int k = 0; k < LoopSize; k++){
				found = false;
				for(int i = 0; i < valuesToFind.size(); i++){
					if(valuesToFind.get(i).isSameCourseItems(timeslot.assignedItems.get(k))){
						found = true;
						valuesToFind.remove(i);
						LoopSize = timeslot.assignedItems.size();
						break;
					}
				}
			}
		}
		if(valuesToFind.size() > 0)
			return false;
		return true;
	}
	
	
	public static boolean noDuplicates(LinkedList<courseItem> inlist){
        courseItem fromCourse;
        courseItem toCourse;
        for(int i = 0; i < inlist.size(); i++){
        	fromCourse = inlist.get(i);
        	for(int j = i + 1; j < inlist.size(); j++){
        		toCourse = inlist.get(j);
        		if (fromCourse.isSameCourseItems(toCourse)){
        			return false;
        		}
        	}
        }
        return true;
	}
	
	//Goes through the code and makes sure that no class exists twice
	public static Boolean noDuplicates(State currentState) {
        Timeslot fromSlot;
        Timeslot toSlot;
        courseItem fromCourse;
        courseItem toCourse;
        for(int i = 0; i < currentState.timeSlots.size(); i++){
        	fromSlot = currentState.timeSlots.get(i);
        	for(int j = 0; j < fromSlot.assignedItems.size(); j++){
        		fromCourse = fromSlot.assignedItems.get(j);
        		for(int k = i + 1; k < currentState.timeSlots.size(); k++){
        			toSlot = currentState.timeSlots.get(k);
        			for(int l = 0; l < toSlot.assignedItems.size(); l++){
        				toCourse = toSlot.assignedItems.get(l);
        				if(fromCourse.isSameCourseItems(toCourse))
        					return false;
        			}
        		}
        	}
        }
        return true;
    }
	/*
	private static Boolean tuesdayCourseCheck(State currentState){
		timeslots = currentState.timeSlots; 
		Timeslot currentSlot;

		// Check every timeslot to ensure no course is assigned at 11:00 on a Tuesday 
		for (int i=0; i < timeslots.size(); i++){	
			currentSlot = timeslots.get(i);

			if((currentSlot.localSlot.day.contentEquals("TU"))&& (currentSlot.localSlot.startTime.contentEquals("11:00"))) {
				if ((currentSlot.forCourses == true)&&(currentSlot.assignedItems.size() > 0)){
					return false;
				}
			}

		}
		return true;
	}
	/*
	private static Boolean eveningLecCheck(State currentState){
		timeslots = currentState.timeSlots; 
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};
		Timeslot currentSlot;

		// Check every timeslot in a state to make sure no coursemax/labmax is violated, nor do any labs/tutorials share the same slot as their corresponding course
		for (int i=0; i < timeslots.size(); i++){	
			currentSlot = timeslots.get(i);

			for (int j=0; j < currentSlot.assignedItems.size(); j++) {	
				if(currentSlot.assignedItems.get(j).isALec == true){
					String lecNum = currentSlot.assignedItems.get(j).section;
					if (lecNum.charAt(0) == '9'){
						//if (!Arrays.stream(eveningSlots).anyMatch(currentSlot.localSlot.startTime::equals))
							//return false;
					}	
					
				}
			}
		}
		return true;
	}

	
	// Check that no two 500 level courses are assigned in the same slot in any given state
	private static Boolean check500(State currentState){
		timeslots = currentState.timeSlots; 
		int count = 0;
		Timeslot currentSlot;

		// Check every timeslot in a state to make sure no more than one 500 level class occupies any one timeslot
		for (int i=0; i < timeslots.size(); i++){	
			currentSlot = timeslots.get(i);
			count = 0;

			for (int j=0; j < currentSlot.assignedItems.size(); j++) {
				if ((currentSlot.assignedItems.get(j).isALec == true) && (Integer.parseInt(currentSlot.assignedItems.get(j).number) >= 500) && (Integer.parseInt(currentSlot.assignedItems.get(j).number) < 600)) {
					count++;
					if (count > 1){
						return false;
					}
				}
			}
		}
		return true;
	} 

	// Deal with the complicated CPSC 813/913 scheduling and overlap rules
	private static Boolean check13(State currentState){
		timeslots = currentState.timeSlots; 
		Timeslot currentSlot;

		
		
		// Ensure CPSC 813 and 913 are scheduled only during the TU timeslot starting at 18:00.
		for (int i=0; i < timeslots.size(); i++){	
			currentSlot = timeslots.get(i);

			for(int j = 0; j < currentSlot.assignedItems.size(); j++){
				
				// If CPSC 813 or 913 are not scheduled TU at 18:00, return false
				if(!(currentSlot.localSlot.day.contentEquals("TU")) && !(currentSlot.localSlot.startTime.contentEquals("18:00"))) {
					if ((currentSlot.assignedItems.get(j).isALec == true) && ((currentSlot.assignedItems.get(j).number.contentEquals("813")) || (currentSlot.assignedItems.get(j).number.contentEquals("913")))){
						return false;
					}
				}

				// If CPSC 813 is scheduled TU at 18:00 but so is any element of CPSC 313, return false
				else if (((currentSlot.localSlot.day.contentEquals("TU")) && (currentSlot.localSlot.startTime.contentEquals("18:00"))) && (currentSlot.assignedItems.get(j).number.contentEquals("813"))){
					for(int k = 0; k < currentSlot.assignedItems.size(); k++) {
						if (currentSlot.assignedItems.get(k).number.contentEquals("313")){
							return false;
						}
					}
				}
				
				// If CPSC 913 is scheduled TU at 18:00 but so is any element of CPSC 413, return false
				else if (((currentSlot.localSlot.day.contentEquals("TU")) && (currentSlot.localSlot.startTime.contentEquals("18:00"))) && (currentSlot.assignedItems.get(j).number.contentEquals("913"))){
					for(int k = 0; k < currentSlot.assignedItems.size(); k++) {
						if (currentSlot.assignedItems.get(k).number.contentEquals("413")){
							return false;
						}
					}
				}
			}
		}		
		return true;
	}
	*/
	/*
	// Deal with the CPSC 813 and 913 being scheduled outside allowed times
	private static Boolean schedule13(State currentState){
		timeslots = currentState.timeSlots; 
		Timeslot currentSlot;

		for (int i=0; i < timeslots.size(); i++){	
			currentSlot = timeslots.get(i);
			for(int j = 0; j < currentSlot.assignedItems.size(); j++){
				if(!(currentSlot.localSlot.day.contentEquals("TU")) && !(currentSlot.localSlot.startTime.contentEquals("18:00"))) {
					if ((currentSlot.assignedItems.get(j).isALec == true) && ((currentSlot.assignedItems.get(j).number.contentEquals("813")) || (currentSlot.assignedItems.get(j).number.contentEquals("913")))){
						return false;
					}
				}
			}
		}
		return true;
	}
	
		// Check incompatible classes aren't scheduled at the same times
	private static Boolean checkIncompatible(State currentState, LinkedList<CoursePair> incompClasses){
		timeslots = currentState.timeSlots; 
		int incompItems = 0;
		CoursePair cp;
		courseItem c1;
		courseItem c2;
		courseItem item;
		

		for (int i=0; i < timeslots.size(); i++){	

			for (int j=0; j < incompClasses.size(); j++){
				incompItems = 0;
				cp = incompClasses.get(j);
				c1 = cp.getItemOne();
				c2 = cp.getItemTwo();

				for (int k=0; k < timeslots.get(i).assignedItems.size(); k++){
					item = timeslots.get(i).assignedItems.get(k);

					if(item.isSameCourseItems(c1)||item.isSameCourseItems(c2)){
						incompItems++;
						if(incompItems > 1)
							return false;
					}
				}
			}
		}
		return true;
	} */
	
	private static Boolean checkPreassigned(State currentState, LinkedList<TimeCoursePair> preAssigned) {
		timeslots = currentState.timeSlots;
		TimeCoursePair pa;
		courseItem c;
		Slot s;
		courseItem item;

		for (int i=0; i < timeslots.size(); i++){	

			for (int j=0; j < preAssigned.size(); j++){

				pa = preAssigned.get(j);
				c = pa.getCourseItem();
				s = pa.getTime();

				for (int k=0; k < timeslots.get(i).assignedItems.size(); k++){
					item = timeslots.get(i).assignedItems.get(k);

					if(item.isSameCourseItems(c)){
						if (!timeslots.get(i).localSlot.startTime.contentEquals(s.startTime))
							return false;
					}
				}
			}
		}
		return true;
	}

	// Check that labs on Fridays don't overlap with any of their course sections 
	private static Boolean checkFridays(State currentState){
		timeslots = currentState.timeSlots; 
		LinkedList<Timeslot> fridayCourses = new LinkedList<Timeslot>();
		LinkedList<Timeslot> fridayLabs = new LinkedList<Timeslot>();
		
		// A linked list holding courses that start in the middle of a lab slot, in the format: Course Slot -> Lab Slot etc. 
		LinkedList<Timeslot> problemSlots = new LinkedList<Timeslot>();
		
		Timeslot currentCourseSlot;
		Timeslot currentLabSlot;
		
		String[] splitStartTime;
		String startTime;
		int labStart;
		int courseStart;
		
		
		// Filter all timeslots into two separate linked lists; one for Friday courses and one for Friday labs
		for (int i = 0; i < timeslots.size(); i++) {
			if((timeslots.get(i).localSlot.day.contentEquals("FR")) && (timeslots.get(i).forCourses == false)) {
				fridayLabs.add(timeslots.get(i));
			}
			else if ((timeslots.get(i).localSlot.day.contentEquals("FR")) && (timeslots.get(i).forCourses == true))
				fridayCourses.add(timeslots.get(i));
		}
		
		
		// Find which course and lab timeslots overlap and add them into problemSlots in the order Course, Lab
		for (int i=0; i < fridayLabs.size(); i++){		
			splitStartTime = fridayLabs.get(i).localSlot.startTime.split(":");
			startTime = splitStartTime[0];
			labStart = Integer.parseInt(startTime);
			
			for (int j = 0; j < fridayCourses.size(); j++) {
				splitStartTime = fridayCourses.get(i).localSlot.startTime.split(":");
				startTime = splitStartTime[0];
				courseStart = Integer.parseInt(startTime);
				
				if(courseStart == (labStart + 1)) {
					problemSlots.add(fridayCourses.get(j));
					problemSlots.add(fridayLabs.get(i));
				}
			}
		}
		// Loop to check if any of the problem slots house an overlap of a lab with its corresponding course
		for (int i=0; i< problemSlots.size(); i+=2 ) {
			currentCourseSlot = problemSlots.get(i);
			currentLabSlot = problemSlots.get(i+1);
			
			for(int j=0; j<currentCourseSlot.assignedItems.size(); j++) {
				for (int k=0; k<currentLabSlot.assignedItems.size(); k++) {
					if((currentCourseSlot.assignedItems.get(j).department.contentEquals(currentLabSlot.assignedItems.get(k).department)) && (currentCourseSlot.assignedItems.get(j).number.contentEquals(currentLabSlot.assignedItems.get(k).number))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// Check that labs on Tuesdays don't overlap with any of their course sections 
	private static Boolean checkTuesdays(State currentState){
		timeslots = currentState.timeSlots; 
		LinkedList<Timeslot> tuesdayCourses = new LinkedList<Timeslot>();
		LinkedList<Timeslot> tuesdayLabs = new LinkedList<Timeslot>();
		
		// A linked list holding courses that start in the middle of a lab slot, in the format: Course Slot -> Lab Slot etc. 
		LinkedList<Timeslot> problemSlots = new LinkedList<Timeslot>();
		
		Timeslot currentCourseSlot;
		Timeslot currentLabSlot;
		
		String[] splitTime;
		String startTime;
		int labStart;
		String endTime;
		int labEnd;
		String[] courseStartTime;
		String[] courseEndTime;
		
		// Filter all timeslots into two separate linked lists; one for Tuesday courses and one for Tuesday labs
		for (int i = 0; i < timeslots.size(); i++) {
			if((timeslots.get(i).localSlot.day.contentEquals("TU")) && (timeslots.get(i).forCourses == false)) {
				tuesdayLabs.add(timeslots.get(i));
			}
			else if ((timeslots.get(i).localSlot.day.contentEquals("TU")) && (timeslots.get(i).forCourses == true))
				tuesdayCourses.add(timeslots.get(i));
		}
		
		
		// Find which course and lab timeslots overlap and add them into problemSlots in the order Course, Lab
		for (int i=0; i < tuesdayLabs.size(); i++){		
			splitTime = tuesdayLabs.get(i).localSlot.startTime.split(":");
			startTime = splitTime[0];
			labStart = Integer.parseInt(startTime);
			
			try {
				splitTime = tuesdayLabs.get(i).localSlot.endTime.split(":");
			}catch (NullPointerException e) {
				continue;
			}
			
			endTime = splitTime[0];
			labEnd = Integer.parseInt(endTime);
			
			for (int j = 0; j < tuesdayCourses.size(); j++) {
				courseStartTime = tuesdayCourses.get(i).localSlot.startTime.split(":");
				courseEndTime = tuesdayCourses.get(i).localSlot.endTime.split(":");
				
				if((Integer.parseInt(courseStartTime[0]) == (labStart)) || (Integer.parseInt(courseEndTime[0]) == (labEnd))) {
					problemSlots.add(tuesdayCourses.get(j));
					problemSlots.add(tuesdayLabs.get(i));
				}
			}
		}
		// Loop to check if any of the problem slots house an overlap of a lab with its corresponding course
		for (int i=0; i< problemSlots.size(); i+=2 ) {
			currentCourseSlot = problemSlots.get(i);
			currentLabSlot = problemSlots.get(i+1);
			
			for(int j=0; j<currentCourseSlot.assignedItems.size(); j++) {
				for (int k=0; k<currentLabSlot.assignedItems.size(); k++) {
					if((currentCourseSlot.assignedItems.get(j).department.contentEquals(currentLabSlot.assignedItems.get(k).department)) && (currentCourseSlot.assignedItems.get(j).number.contentEquals(currentLabSlot.assignedItems.get(k).number))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// Check that labs on Fridays don't overlap with any of their course sections 
	private static Boolean checkMondays(State currentState){
		timeslots = currentState.timeSlots; 
		LinkedList<Timeslot> mondayCourses = new LinkedList<Timeslot>();
		LinkedList<Timeslot> mondayLabs = new LinkedList<Timeslot>();
		
		// A linked list holding courses that start in the middle of a lab slot, in the format: Course Slot -> Lab Slot etc. 
		LinkedList<Timeslot> problemSlots = new LinkedList<Timeslot>();
		
		Timeslot currentCourseSlot;
		Timeslot currentLabSlot;
		String[] splitStartTime;
		String startTime;
		int labStart;
		int courseStart;
		
		// Filter all timeslots into two separate linked lists; one for Friday courses and one for Friday labs
		for (int i = 0; i < timeslots.size(); i++) {
			if((timeslots.get(i).localSlot.day.contentEquals("MO")) && (timeslots.get(i).forCourses == false)) {
				mondayLabs.add(timeslots.get(i));
			}
			else if ((timeslots.get(i).localSlot.day.contentEquals("MO")) && (timeslots.get(i).forCourses == true))
				mondayCourses.add(timeslots.get(i));
		}
		
		
		// Find which course and lab timeslots overlap and add them into problemSlots in the order Course, Lab
		for (int i=0; i < mondayLabs.size(); i++){		
			splitStartTime = mondayLabs.get(i).localSlot.startTime.split(":");
			startTime = splitStartTime[0];
			labStart = Integer.parseInt(startTime);
			
			for (int j = 0; j < mondayCourses.size(); j++) {
				splitStartTime = mondayCourses.get(i).localSlot.startTime.split(":");
				startTime = splitStartTime[0];
				courseStart = Integer.parseInt(startTime);
				
				if(courseStart == (labStart)) {
					problemSlots.add(mondayCourses.get(j));
					problemSlots.add(mondayLabs.get(i));
				}
			}
		}
		// Loop to check if any of the problem slots house an overlap of a lab with its corresponding course
		for (int i=0; i< problemSlots.size(); i+=2 ) {
			currentCourseSlot = problemSlots.get(i);
			currentLabSlot = problemSlots.get(i+1);
			
			for(int j=0; j<currentCourseSlot.assignedItems.size(); j++) {
				for (int k=0; k<currentLabSlot.assignedItems.size(); k++) {
					if((currentCourseSlot.assignedItems.get(j).department.contentEquals(currentLabSlot.assignedItems.get(k).department)) && (currentCourseSlot.assignedItems.get(j).number.contentEquals(currentLabSlot.assignedItems.get(k).number))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private static Boolean checkMondaysJames(State currentState){
		
		LinkedList<Timeslot> mondayCourses = new LinkedList<Timeslot>();
		LinkedList<Timeslot> mondayLabs = new LinkedList<Timeslot>();
		for (int i = 0; i < timeslots.size(); i++) {
			if((timeslots.get(i).localSlot.day.contentEquals("MO")) && (timeslots.get(i).forCourses == false)) {
				mondayLabs.add(timeslots.get(i));
			}
			else if ((timeslots.get(i).localSlot.day.contentEquals("MO")) && (timeslots.get(i).forCourses == true))
				mondayCourses.add(timeslots.get(i));
		}
		courseItem lab;
		courseItem course;
		Timeslot labSlot;
		Timeslot courseSlot;
		for(int i = 0; i < mondayLabs.size(); i++){
			labSlot = mondayLabs.get(i);
			for(int j = 0; j < labSlot.assignedItems.size(); j++){
				lab = labSlot.getAssignedItems().get(j);
				for(int k = 0; k < mondayCourses.size(); k++){
					courseSlot = mondayCourses.get(k);
					for(int l = 0; l < courseSlot.assignedItems.size(); l++){
						course = courseSlot.assignedItems.get(l);
						if(lab.section.contentEquals(course.section) && lab.department.contentEquals(course.department) && lab.number.contentEquals(course.number)){
							if(courseSlot.localSlot.startTime.contentEquals(labSlot.localSlot.startTime))
								return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	// Check unwanted courseItem/Timeslot pairs are not scheudled 
//	private static Boolean checkUnwanted(State currentState, LinkedList<TimeCoursePair> unwanted){
//		timeslots = currentState.timeSlots; 
//		TimeCoursePair uw;
//		courseItem c;
//		courseItem item;
//		Slot s;
//		
//	
//		for (int i=0; i < timeslots.size(); i++){	
//	
//			for (int j=0; j < unwanted.size(); j++){
//				uw = unwanted.get(j);
//				c = uw.getCourseItem();
//				s = uw.getTime();
//	
//				for (int k=0; k < timeslots.get(i).assignedItems.size(); k++){
//					item = timeslots.get(i).assignedItems.get(k);
//
//					if(item.isSameCourseItems(c)){
//						if (timeslots.get(i).localSlot.startTime.contentEquals(s.startTime))
//							return false;
//					}
//				}
//			}
//		}
//		return true;
//	}

//------------------------------------------------------------------------------------------------------------
//This section holds all functions that check the hard constraints when attempting an assignment
	/*
	// Ensure no course is assigned on tuesday from 11
	private static Boolean tuesdayCourseCheckAssign(Timeslot timeslot, courseItem item){
		// Check timeslot so that a course is not being assigned on a Tuesday at 11
			if((timeslot.localSlot.day.contentEquals("TU")) && (timeslot.localSlot.startTime.contentEquals("11:00"))) {
				if ((timeslot.forCourses == true)){
					return false;
				}
			}
			
		return true;
	}
	
	// Check that you won't have more than one 500 level course in a given timeslot
	private static Boolean eveningLecAssign(Timeslot timeSlot, courseItem item){
		String[] eveningSlots = {"18:00", "18:30", "19:00", "20:00"};

		if(item.isALec == true){
			String lecNum = item.section;
			if (lecNum.contentEquals("09")){
				if (Arrays.stream(eveningSlots).anyMatch(timeSlot.localSlot.startTime::contentEquals))
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
	private static Boolean assign13(Timeslot timeslot, courseItem item){
		if ((item.number.contentEquals("813") && (item.isALec == true))){
			if ((!(timeslot.localSlot.day.contentEquals("TU")) || !(timeslot.localSlot.startTime.contentEquals ("18:00")))){	
				return false;
			}
		}
		
		else if (item.number.contentEquals("913") && (item.isALec == true)){
			if (!(timeslot.localSlot.day.contentEquals("TU")) || !(timeslot.localSlot.startTime.contentEquals ("18:00"))){	
				return false;
			}
		}
		
		return true;
	}
	
	// Check incompatible classes aren't scheduled at the same times
	private static Boolean checkIncompatibleAssign(Timeslot timeslot, courseItem item, LinkedList<CoursePair> incompClasses){
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

	// Check unwanted courseItem/Timeslot pairs are not scheudled 
	private static Boolean checkUnwantedAssign(Timeslot timeslot, courseItem item, LinkedList<TimeCoursePair> unwanted){
		courseItem c;
		Slot s;
		TimeCoursePair uw;
	
		for (int i=0; i < unwanted.size(); i++){
			uw = unwanted.get(i);
			c = uw.getCourseItem();
			s = uw.getTime();

			for (int j=0; j < timeslot.assignedItems.size(); j++){
				item = timeslot.assignedItems.get(j);

				if(item.isSameCourseItems(c)){
					if (timeslot.localSlot.startTime.contentEquals(s.startTime))
						return false;
				}
			}
		
		}
		return true;
	}

	// Check that the assignment about to be made doesn't cause an overlap 
	private static Boolean checkOverlapAssign(State currentState, Timeslot timeslot, courseItem item){
		String[] splitTime;
		int startTime;
		int endTime;
		int compareStart;
		int compareEnd;
		LinkedList<Timeslot> timeslots = currentState.timeSlots;
		
		if (timeslot.localSlot.day.contentEquals("TU")) {
			splitTime = timeslot.localSlot.startTime.split(":");
			startTime = Integer.parseInt(splitTime[0]);
			
			try {
				splitTime = timeslot.localSlot.endTime.split(":");
			}catch (NullPointerException e) {
				
			}
			
			endTime = Integer.parseInt(splitTime[0]);
			
			for(int i=0; i < timeslots.size(); i++) {
				if(timeslots.get(i).localSlot.day.contentEquals("TU")) {
					splitTime = timeslots.get(i).localSlot.startTime.split(":");
					compareStart = Integer.parseInt(splitTime[0]);
					
					try {
						splitTime = timeslots.get(i).localSlot.endTime.split(":");
					}catch (NullPointerException e) {
						continue;
					}
					
					compareEnd = Integer.parseInt(splitTime[0]);
					
					if (compareStart == startTime || compareEnd == endTime) {
						for(int j=0; j < timeslots.get(i).assignedItems.size(); j++) {
							if((timeslots.get(i).assignedItems.get(j).department.contentEquals(item.department)) && (timeslots.get(i).assignedItems.get(j).number.contentEquals(item.number))) {
								return false;
							}
						}
					}
				}
			}			
		}
		
		else if (timeslot.localSlot.day.contentEquals("FR")) {
			splitTime = timeslot.localSlot.startTime.split(":");
			startTime = Integer.parseInt(splitTime[0]);
			
			try {
				splitTime = timeslot.localSlot.endTime.split(":");
			}catch (NullPointerException e) {
				
			}
			
			endTime = Integer.parseInt(splitTime[0]);
			
			for(int i=0; i < timeslots.size(); i++) {
				if(timeslots.get(i).localSlot.day.contentEquals("FR")) {
					splitTime = timeslots.get(i).localSlot.startTime.split(":");
					compareStart = Integer.parseInt(splitTime[0]);
					
					try {
						splitTime = timeslots.get(i).localSlot.endTime.split(":");
					}catch (NullPointerException e) {
						continue;
					}
					
					compareEnd = Integer.parseInt(splitTime[0]);
					
					if (compareStart == startTime || compareStart == startTime + 1 || compareStart + 1 == startTime) {
						for(int j=0; j < timeslots.get(i).assignedItems.size(); j++) {
							if((timeslots.get(i).assignedItems.get(j).department.contentEquals(item.department)) && (timeslots.get(i).assignedItems.get(j).number.contentEquals(item.number))) {
								return false;
							}
						}
					}
				}
			}			
		}
		
		return true;
	}

*/
//------------------------------------------------------------------------------------------------------------
//This section holds all the complete check possiblities for Constr; includes Constr.assign, Constr.partial and Constr.final

	// Run Constr on a final solution
	public static Boolean finalCheck(State currentState, LinkedList<CoursePair> inc, LinkedList<TimeCoursePair> preAssigned, LinkedList<TimeCoursePair> unwanted){

		if(!confirmAllClassesAssigned(currentState))
			return false;
		if(!noDuplicates(currentState))
			return false;
		if(!checkFridays(currentState))
			return false;
		if(!checkTuesdays(currentState))
			return false;
		if(!checkMondays(currentState))
			return false;
		if(!checkPreassigned(currentState, preAssigned))
			return false;
		return true;
	}
	

	// Run Constr on a partial solution
	public static Boolean partial(State currentState, LinkedList<CoursePair> inc, LinkedList<TimeCoursePair> preAssigned, LinkedList<TimeCoursePair> unwanted){
		if(!noDuplicates(currentState))
			return false;
		if(!checkFridays(currentState))
			return false;
		if(!checkTuesdays(currentState))
			return false;
		if(!checkMondaysJames(currentState))
			return false;
		if(!checkPreassigned(currentState, preAssigned))
			return false;
		return true;
	}

	/*
	// Run Constr on an assignment
	public static Boolean assign(State currentState, Timeslot ts, courseItem ci, LinkedList<CoursePair> inc, LinkedList<TimeCoursePair> unwanted){
		if (eveningLecAssign(ts, ci) && assign500(ts) && assign13(ts, ci) && checkIncompatibleAssign(ts, ci, inc) && checkUnwantedAssign(ts, ci, unwanted) && tuesdayCourseCheckAssign(ts, ci) && checkOverlapAssign(currentState, ts, ci) && check13(currentState))
			return true;
		return false;

	}

	*/
}