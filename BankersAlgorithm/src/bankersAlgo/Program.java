package bankersAlgo;

import java.util.ArrayList;
import java.util.Random;

public class Program {

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static Random rand = new Random();
	static final boolean UNSAFE = false;
	static final boolean SAFE = true;

	public static void main(String[] args) {
		int totalHeldResources = 0;
		int avail = TOTAL_RESOURCES - totalHeldResources;
		;
		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		for (int i = 0; i < NUM_PROCS; i++) {
			processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small
																			// range for its max
		}
		// Run the simulation:
		for (int i = 0; i < ITERATIONS; i++) {
			// loop through the processes and for each one get its request
			for (int j = 0; j < processes.size(); j++) {
				Proc requestingProc = processes.get(j);
				// Get the request
				int currRequest = requestingProc.resourceRequest(avail);

				// Here you have to enter code to determine whether or not the request can be
				// granted,
				// and then grant the request if possible. Remember to give output to the
				// console
				// this indicates what the request is, and whether or not its granted.

				// Process is sleeping, just ignore processes that don't ask for resources
				if (currRequest == 0) {
					//System.out.println("Process " + j + " sleeping...");
					continue;
				}
				
				// Process is not sleeping and not done, request resource and process the request
				else if (currRequest > 0) {
					System.out.println("Process " + j + " REQUESTING " + currRequest + " resources.");
					// Find out if the request is safe to be granted 
					if (isSafe(processes, j, currRequest, avail)) {
						// update the statistics to correctly enumerate held resources and available ones
						totalHeldResources += currRequest;
						avail -= currRequest;
						requestingProc.addResources(currRequest);
					}
				} else {
					// Process is finished and is returning the resources it had been holding
					System.out.println("Process " + j + " finished. RETURNING " + -currRequest + " resources.");
					totalHeldResources += currRequest;
					avail -= currRequest;
				}				 

				// At the end of each iteration, give a summary of the current status:
				System.out.println("\n***** STATUS *****");
				System.out.println("Total Available: " + avail);
				for (int k = 0; k < processes.size(); k++)
					System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: "
							+ processes.get(k).getMaxResources() + ", claim: "
							+ (processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
				System.out.println("***** STATUS *****\n");

			}
		}

	}

	/**
	 * Find out if the process can be granted the resources it is requesting. If the projected 
	 * numbers will keep the system in a safe state, request is granted. Otherwise it is denied.
	 * @param processes - list of processes the system has to keep track of
	 * @param j - current process that is requesting resources
	 * @param currRequest - current number of resources process is requesting
	 * @param avail - number of available resources
	 * @return true if requested resources will keep system in safe state, else @return false
	 */
	private static boolean isSafe(ArrayList<Proc> processes,  int j, int currRequest, int avail) {
		// Create a deep copy of the original array so we don't destroy the original
		ArrayList<Proc> copyProcesses = new ArrayList<Proc>();
		for (int i = 0; i < processes.size(); i++) {
			Proc proc = processes.get(i);
			copyProcesses.add(new Proc(proc));
		}
		// Simulate what would happen if the request would be accepted
		copyProcesses.get(j).addResources(currRequest);
		boolean found;
		int potentialAvail = avail - currRequest;

		// Loop through until all processes are found to be able to go through
		while (!copyProcesses.isEmpty()) {
			found = false;

			// Loop through the processes in the copied list to find out if they are able to
			// be completed
			for (int i = 0; i < copyProcesses.size(); i++) {
				Proc currProcess = copyProcesses.get(i);
				// If there are enough resources to complete the current process, add the resources
				// held by that resource to the system and remove the process from the list of 
				// processes needed to be checked
				if ((currProcess.getMaxResources() - currProcess.getHeldResources()) <= potentialAvail) {
					potentialAvail += currProcess.getHeldResources();
					copyProcesses.remove(currProcess);
					found = true;
				}
			}
			// If there are no processes able to be finished with the number of resources left,
			// the system is in an unsafe state
			if (!found) {
				System.out.println("***REQUEST DENIED***");
				return UNSAFE;
			}
		}

		System.out.println("***REQUEST GRANTED***");
		return SAFE;
	}

}
