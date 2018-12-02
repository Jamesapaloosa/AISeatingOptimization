import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
public class EvalData {
	private static int Wpref = -6969;
	private static int Wpair = -6969;
	private static int Wminfilled = -6969;
	private static int Wsecdiff = -6969;
	private static int pen_coursemin = -6969;
	private static int pen_labsmin = -6969;
	private static int pen_notpaired = -6969;
	private static int pen_section = -6969;
	
	
	
	public static void promptUserForValues(){
		Scanner scanner = new Scanner(System.in);
		String input;
		System.out.println("Please enter your values for eval below: ");
		boolean cont = true;
		int temp = -6969;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		while(cont){
			System.out.print("Please Enter Wminfilled value: " );
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		Wminfilled = temp;
		
		cont = true;
		while(cont){
			System.out.print("Please Enter Wpref value: ");
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		Wpref = temp;
		
		
		cont = true;
		while(cont){
			System.out.print("Please Enter Wpair value: " );
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		Wpair = temp;
		
		cont = true;
		while(cont){
			System.out.print("Please Enter Wsecdiff value: ");
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		Wsecdiff = temp;
		
		cont = true;
		while(cont){
			System.out.print("Please Enter the coursemin penalty: ");
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		pen_coursemin = temp;
		
		cont = true;
		while(cont){
			System.out.print("Please Enter the labs min penalty: ");
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		pen_labsmin = temp;
		
		cont = true;
		while(cont){
			System.out.print("Please enter the not paired penalty: ");
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		pen_notpaired = temp;
		
		cont = true;
		while(cont){
			System.out.print("Please enter the section penalty: ");
			try{
		        input = bufferedReader.readLine();
		        temp = Integer.parseInt(input);
				cont = false;
			}catch(NumberFormatException ex){
				System.out.println("Warning! Invalid input for weight.  Must be a number.");
			}catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		pen_section = temp;
		scanner.close();
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


	public static int getPen_coursemin() {
		return pen_coursemin;
	}


	public static int getPen_labsmin() {
		return pen_labsmin;
	}



	public static int getPen_notpaired() {
		return pen_notpaired;
	}



	public static int getPen_section() {
		return pen_section;
	}


}
