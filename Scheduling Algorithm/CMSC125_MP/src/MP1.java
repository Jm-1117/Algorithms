import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MP1 {
	public static String filename;
	public static char algo;
	
	
	public static void main(String[] args) {
		int timeQuantum;
		int minArrival = 25;
		ArrayList<ArrayList<Integer>> processDetails = new ArrayList<ArrayList<Integer>>();
		ArrayList<String> PID = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		
		// Ask user for file name
		System.out.println("Provide file name containing process information: (might need na kasama file location) ");
		filename = input.nextLine();
		
		 try {
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      myReader.nextLine();
		      while (myReader.hasNextLine()) {
		        PID.add(myReader.next()); // Get process name
		   		        
		        ArrayList<Integer> ctrDetails = new ArrayList<Integer>();
		        ctrDetails.add(myReader.nextInt());	//0	Burst Time
		        ctrDetails.add(myReader.nextInt());	//1	Arrival Time
		        if(ctrDetails.get(1)<=minArrival) {
		        	minArrival = ctrDetails.get(1);
		        }
		        ctrDetails.add(myReader.nextInt());	//2	Priority
		        ctrDetails.add(0);					//3	Flag (0 if not yet done, 1 if done)
		        ctrDetails.add(0);					//4 ctrBurst - when process is preempted 
		        ctrDetails.add(0); 					//5 start time
		        ctrDetails.add(0); 					//6 waiting time
		        ctrDetails.add(0); 					//7 turnaround time 
		        processDetails.add(ctrDetails); 
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		// Just printing contents of arraylist  
		System.out.println(PID);
		System.out.println(processDetails);
		
		algorithms algorithm = new algorithms(PID, processDetails);
		
		// Ask user what algorithm
		System.out.println("Choose an algorithm: \n A. Shortest-Job First \n B. Priority Scheduling \n C. Round Robin ");
		algo = input.next().toUpperCase().charAt(0);
		
		switch(algo) {
		case 'A'://Shortest Job First
			algorithm.SJF();
			break;
		case 'B'://Priority Scheduling
			algorithm.PS();
			break;
		case 'C'://Round Robin
			System.out.print("Provide time quantum in seconds: ");
			timeQuantum = input.nextInt();
			algorithm.RR(timeQuantum,minArrival);
			break;
		default:
			System.out.println("Please choose a valid option.");
		}
		
		
		
		input.close();
	}

}
