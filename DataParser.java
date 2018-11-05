import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*; 
public class DataParser {
	String sourcefile;
	
	public DataParser(String infile) {
		if(infile == "") {
			sourcefile = "E:\\CPSC433\\testCase1.txt";
		}else {
			sourcefile = infile;
		}
	}
	
	
	public fileData readfile() throws IOException {
		File file = new File(sourcefile);
		LinkedList<inCoursesLabs> coursesAndLabs = new LinkedList<inCoursesLabs>();
		BufferedReader br;
		try {
		br = new BufferedReader(new FileReader(file));
		
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}
		String line;
		while ((line = br.readLine()) != null){
			switch (line) {
			case "Name:":
				
			}
		}
		
		return coursesAndLabs;
	}
	
	public void readfile(String infile) throws IOException {
		sourcefile = infile;
		readfile();
	}
	
	private inCoursesLabs readCourseLine(String input) {
		inCoursesLabs outCL;
		
		
		return outCL
	}
	
	
	inCoursesLabs temp;
}
