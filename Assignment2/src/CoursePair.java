public class CoursePair {
	courseItem itemOne;
	courseItem itemTwo;
	
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
