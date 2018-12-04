public class CoursePair {
	courseItem itemOne;
	courseItem itemTwo;
	//Course that stores the data of a pair of courses
	public courseItem getItemOne() {
		return itemOne;
	}
	
	public courseItem getItemTwo() {
		return itemTwo;
	}
	
	public CoursePair(courseItem inOne, courseItem inTwo){
		itemOne = inOne;
		itemTwo = inTwo;
	}
}
