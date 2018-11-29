import java.util.Scanner;

public class EvalData {
	private static int Wpref;
	private static int Wpair;
	private static int Wminfilled;
	private static int Wsecdiff;
	
	
	
	public static void promptUserForValues(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter your values for eval below: ");
		boolean cont = true;
		int temp;
		System.out.print("Please Enter Wminfilled value: " );
		temp = scanner.nextInt();
		Wminfilled = temp;
		
		System.out.print("Please Enter Wpref value: ");
		temp = scanner.nextInt();
		Wpref = temp;
		
		System.out.print("Please Enter Wpair value: " );
		temp = scanner.nextInt();
		Wpair = temp;
		
		System.out.print("Please Enter Wsecdiff value: ");
		temp = scanner.nextInt();
		Wsecdiff = temp;
		
	}
	
	
	public static int getWminfilled() {
		return Wminfilled;
	}

	public static int getWpref() {
		return Wpref;
	}

	public static int getWpair() {
		return Wpair;
	}

	public static int getWsecdiff() {
		return Wsecdiff;
	}

}
