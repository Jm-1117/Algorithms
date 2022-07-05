import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class algorithms {
	ArrayList<ArrayList<Integer>> processDetails = new ArrayList<ArrayList<Integer>>();
	ArrayList<String> PID = new ArrayList<String>();
	String[] timeDuration;
	
	public int n;
	public int min = 25;
	
	public algorithms(ArrayList<String> process, ArrayList<ArrayList<Integer>> details) {
		this.PID = process;
		this.processDetails = details;
		this.n = PID.size();
		this.timeDuration = new String[n];
	}
	
	//Shortest-Job First
	public void SJF() { 
		System.out.println("Shortest-Job First Algorithm");	
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {	
			int time = 0;
			int current = 0; 		//for indexing current process running
			int prev = 0;
			int ctrBurst = 0; 		//for indicating remaining burst time in a process
			int currentBurst =0; 	//burst time of current process running
			int ctr =0;		 		//number of processes that has completed
			boolean add=false;
			String timeString, prevTimeString;
			//String[] timeDuration = new String[n];
			
			public void run() {
				Calendar date = Calendar.getInstance();
				timeString = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
				if (ctr!=n) { 		//if not all processes are completed yet
					for(int i=0; i<n; i++) {
						int burst = processDetails.get(i).get(0);
						int arrival = processDetails.get(i).get(1);
						int flag = processDetails.get(i).get(3);
						if(burst < min && arrival <= time && flag ==0) {
							min = burst;
							current = i;
							ctrBurst = processDetails.get(current).get(4);
							currentBurst = burst;	
							
							
							add = true;
							if(ctrBurst==0) {
								timeDuration[current] = timeString;
								processDetails.get(current).set(5,time); //set start time
								add = false;
							} 
							
							if	(prev!=current && processDetails.get(prev).get(3) ==0) {
								timeDuration[prev] += "-" + prevTimeString +", ";
								
							}
						}
					}
					if(add==true) {
						timeDuration[current] += timeString;
						add=false;
					}
					
					
					if (current==n) {	//if no process arrived in current time
						time++;
					} else {
						ctrBurst++;		
						processDetails.get(current).set(4, ctrBurst);
						System.out.print(timeString);
						System.out.println("\t Running Process " + PID.get(current));
						
						
						
						prev = current;
						prevTimeString = timeString;
						if(ctrBurst==currentBurst) {				//if process is completed
							processDetails.get(current).set(3,1);	//flag 1 as completed
							min = 25;
							timeDuration[current]= timeDuration[current]+"-"+timeString;
							processDetails.get(current).set(7,time-processDetails.get(current).get(5)+1);	//turnaround time = completion - start
							int wait = processDetails.get(current).get(7)-currentBurst +processDetails.get(current).get(5)-processDetails.get(current).get(1);
							processDetails.get(current).set(6, wait ); //waiting time = TAT - BT + (start - arrival);
							ctr++;		//add 1 to completed processes
						} 
						
						time++;
					}	
					
				} else {
					timer.cancel();
					summary();
				}
			}
		};
		
		timer.schedule(task, 1000,1000);
		
	}
	
	//Priority Scheduling
	public void PS() {
		System.out.println("Priority Scheduling");
		//Just printing contents of arraylist
		System.out.println(PID);
		System.out.println(processDetails);
	}
	
	//Round-Robin
	public void RR(int quantum, int inputMinArrival) {
		int timeQuantum = quantum;
		int minArrival = inputMinArrival;
		System.out.println("Round Robin Algorithm");
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {	
			int time = minArrival;	// start at minimum arrivaltime
			int current = 0; 		//for indexing current process running
			int prev = 0;
			int ctrBurst = 0; 		//for indicating remaining burst time in a process
			int currentBurst =0; 	//burst time of current process running
			int ctr =0;		 		//number of processes that has completed
			int i=0;
			//int next =25;
			boolean add=false;
			String timeString, prevTimeString;
			//String[] timeDuration = new String[n];
			int ctrQuantum =0;
			Queue<Integer> processQueue = new LinkedList<Integer>();
			
			public void run() {
				Calendar date = Calendar.getInstance();
				timeString = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
				if (ctr!=n) { 		//if not all processes are completed yet
						
					for(int j=0;j<n;j++) {
						int ctrArrival = processDetails.get(j).get(1);
						if(ctrArrival==time && !processQueue.contains(j)) {
							processQueue.add(j);
						}
					}
					i = processQueue.peek();	
						
					
						if(ctrQuantum==timeQuantum) {
							//i++;
							processQueue.add(i);
							processQueue.poll();
							i = processQueue.peek();
							ctrQuantum=0;	
							add = true;
						}
						if(i>=n) {
							i=0;
						}
						
						int flag = processDetails.get(i).get(3);
						int arrival = processDetails.get(i).get(1);
						
						
						// see process that has arrived
						while(arrival>time) {
							i++;
							if(i==n) {
								i=0;
								current =n;
								break;
							}
							arrival = processDetails.get(i).get(1);
						}
						
						if(flag==1) {
							//i++;
							processQueue.poll();
							i = processQueue.peek();
						}
						
						flag = processDetails.get(i).get(3);
						int burst = processDetails.get(i).get(0);
						arrival = processDetails.get(i).get(1);
						if(arrival <= time && flag ==0) {
							//next = arrival;
							current = i;
							ctrBurst = processDetails.get(current).get(4);
							currentBurst = burst;	
							
							if(ctrBurst==0) {
								timeDuration[current] = timeString;
								processDetails.get(current).set(5,time); //set start time
								add = false;
							} 
							
							if	(prev!=current && processDetails.get(prev).get(3) ==0) {
								timeDuration[prev] += "-" + prevTimeString +", ";	
							}
					}  
					
					if(add==true) {
						timeDuration[current] += timeString;
						add=false;
					}
					
					
					if (current==n) {	//if no process arrived in current time
						time++;
						i=0;
					} else {
						ctrBurst++;		
						ctrQuantum++;
						processDetails.get(current).set(4, ctrBurst);
						System.out.print(timeString);
						System.out.println("\t Running Process " + PID.get(current));
						
						
						
						prev = current;
						prevTimeString = timeString;
						if(ctrBurst==currentBurst) {				//if process is completed
							processDetails.get(current).set(3,1);	//flag 1 as completed
							min = 25;
							timeDuration[current]= timeDuration[current]+"-"+timeString;
							processDetails.get(current).set(7,time-processDetails.get(current).get(5)+1);	//turnaround time = completion - start
							int wait = processDetails.get(current).get(7)-currentBurst +processDetails.get(current).get(5)-processDetails.get(current).get(1);
							processDetails.get(current).set(6, wait ); //waiting time = TAT - BT + (start - arrival);
							ctr++;		//add 1 to completed processes
							//i++;
							processQueue.remove(i);
							if(!processQueue.isEmpty()) {
								i = processQueue.peek();
							}
							
							
							ctrQuantum=0;
							add =true;
						} 
						
						time++;
					}	
					
				} else {
					timer.cancel();
					summary();
					
				}
			}
		};
			
			timer.schedule(task, 1000,1000);
			
	}
	
	public void summary() {
		float aveWT =0;
		float aveTAT =0;
		System.out.println("\n \n \t \t Summary:");
		System.out.println("Process \t Time Duration \t \t \t Waiting Time \t \t Turnaround Time");
		for(int j=0;j<n;j++) {
			aveWT += processDetails.get(j).get(6);
			aveTAT += processDetails.get(j).get(7);
			System.out.println(PID.get(j) + "\t \t " + timeDuration[j] + "\t \t \t" + processDetails.get(j).get(6) + "\t \t \t"  + processDetails.get(j).get(7));
		}
		aveWT = (aveWT/n);
		aveTAT = (aveTAT/n);
		System.out.println("Average Waiting time: " + aveWT + "\nAverage Turnaround time: " + aveTAT);
	}

}
