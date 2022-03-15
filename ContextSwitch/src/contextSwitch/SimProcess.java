package contextSwitch;

import java.util.Random;

public class SimProcess {
	/* This is a class that simulates a process. It has an 
	 * int pid, a String procName, and an int totalInstructions.
	 */

	private int pid;
	private String procName;
	int totalInstructions;
	
	public SimProcess(int pid, String procName, int totalInstructions) {
		this.pid = pid;
		this.procName = procName;
		this.totalInstructions = totalInstructions;
	}
	
	public ProcessState execute(int i) {
		StringBuilder info = new StringBuilder();
		info.append("PROCESS ID: " + pid);
		info.append("\tPROCESS NAME: " + procName);
		info.append("\tINSTRUCTION NUM: " + i);
		System.out.println(info);
		
		if (i >= totalInstructions) {
			return ProcessState.FINISHED;
		}
		
		else {
			Random rand = new Random();
			double prob = Math.random();
			if (prob < .15) {
				return ProcessState.BLOCKED;
			}
			else
				return ProcessState.READY;
		}
	}
	
	public String getName() {
		return procName;
	}

}
