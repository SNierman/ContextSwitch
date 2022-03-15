package contextSwitch;

import java.util.ArrayList;
import java.util.Random;

public class ContextSwitchMain {

	public static void main(String[] args) {
		
		SimProcessor processor = new SimProcessor();
		
		ArrayList<SimProcess> ready = new ArrayList<>();
		ArrayList<SimProcess> blocked = new ArrayList<>();
		ArrayList<ProcessControlBlock> readyPcbs = new ArrayList<>();
		ArrayList<ProcessControlBlock> blockedPcbs = new ArrayList<>();
		final int QUANTUM = 5;
		
		// there are 10 processes in the processor
		ready.add(new SimProcess(100, "Chrome", 255));
		ready.add(new SimProcess(101, "File Explorer", 255));
		ready.add(new SimProcess(102, "Microsoft Word", 255));
		ready.add(new SimProcess(103, "Microsoft Excel", 255));
		ready.add(new SimProcess(104, "Microsoft PowerPoint", 255));
		ready.add(new SimProcess(105, "Eclipse", 255));
		ready.add(new SimProcess(106, "Zoom", 255));
		ready.add(new SimProcess(107, "Outlook", 255));
		ready.add(new SimProcess(108, "Microsoft Edge", 255));
		ready.add(new SimProcess(109, "Calendar", 255));
		
		// there are 10 pcbs to correspond to the 10 processes
		readyPcbs.add(new ProcessControlBlock(ready.get(0)));
		readyPcbs.add(new ProcessControlBlock(ready.get(1)));
		readyPcbs.add(new ProcessControlBlock(ready.get(2)));
		readyPcbs.add(new ProcessControlBlock(ready.get(3)));
		readyPcbs.add(new ProcessControlBlock(ready.get(4)));
		readyPcbs.add(new ProcessControlBlock(ready.get(5)));
		readyPcbs.add(new ProcessControlBlock(ready.get(6)));
		readyPcbs.add(new ProcessControlBlock(ready.get(7)));
		readyPcbs.add(new ProcessControlBlock(ready.get(8)));
		readyPcbs.add(new ProcessControlBlock(ready.get(9)));
				
		int stepNum = 1;
		int quantumTracker = 0;

		for (int i = 0; i < 3000; i++) {
			System.out.print("Step " + stepNum + ": ");
			stepNum++;
			
			// simProcess that is currently on the processor
			SimProcess runningProc = ready.get(0);
			processor.setSimProcess(runningProc);
			
			// run the new process
			ProcessState state = processor.executeNextInstruction();
			quantumTracker++;
			
			// create temporary pcb to hold the info that needs to be retrieved
			ProcessControlBlock tempPcb = new ProcessControlBlock(runningProc);
			
			// if the state of the current process that is running is blocked, add it to the
			// blocked list and remove it from the ready list
			if (state == ProcessState.BLOCKED) {
				System.out.println("***PROCESS BLOCKED***");
				System.out.print("Step " + stepNum + ": ");
				stepNum++;
				i++;
				saveToPCB(readyPcbs.get(0), processor);
				blocked.add(runningProc);
				blockedPcbs.add(readyPcbs.get(0));
				
				ready.remove(runningProc);
				readyPcbs.remove(0);
				
				// reset quantum tracker
				quantumTracker = 0;
				
				while (ready.isEmpty()) {
					wakeUp(ready, blocked, readyPcbs, blockedPcbs);
				}
				// get updated pcb data and put it on the processor
				tempPcb = retrievePcbData(readyPcbs.get(0));
				processor.setCurrInstruction(tempPcb.getCurrInstruction());
			}
			
			// if the state of the current process that is running is finished, remove it from
			// both the ready list and the blocked list
			else if (state == ProcessState.FINISHED) {
				System.out.println("***PROCESS FINISHED***");
				System.out.print("Step " + stepNum + ": ");
				stepNum++;
				i++;
				saveToPCB(readyPcbs.get(0), processor);

				ready.remove(runningProc);
				readyPcbs.remove(0);
				
				// reset quantum tracker
				quantumTracker = 0;
				
				while (ready.isEmpty()) {
					wakeUp(ready, blocked, readyPcbs, blockedPcbs);
				}
				// get updated pcb data and put it on the processor
				tempPcb = retrievePcbData(readyPcbs.get(0));
				processor.setCurrInstruction(tempPcb.getCurrInstruction());
			}
			
			else if (quantumTracker == QUANTUM) {
				System.out.println("***QUANTUM EXPIRED***");
				System.out.print("Step " + stepNum + ": ");
				stepNum++;
				i++;
				saveToPCB(readyPcbs.get(0), processor);
				// if quantum is maxed out, move quantum value to 0 and move current running
				// process to the end of the ready list
				quantumTracker = 0;
				ready.add(runningProc);
				readyPcbs.add(readyPcbs.get(0));
				ready.remove(runningProc);
				readyPcbs.remove(0);

				while (ready.isEmpty()) {
					wakeUp(ready, blocked, readyPcbs, blockedPcbs);
				}
				// get updated pcb data and put it on the processor
				tempPcb = retrievePcbData(readyPcbs.get(0));
				processor.setCurrInstruction(tempPcb.getCurrInstruction());
			}
			
			// always go through the blocked processes once a cycle
			wakeUp(ready, blocked, readyPcbs, blockedPcbs);
			
		}
	}

	/**
	 * Wake up blocked processes with 30% probability
	 * @param ready
	 * @param blocked
	 * @param readyPcbs
	 * @param blockedPcbs
	 */
	private static void wakeUp(ArrayList<SimProcess> ready, ArrayList<SimProcess> blocked,
			ArrayList<ProcessControlBlock> readyPcbs, ArrayList<ProcessControlBlock> blockedPcbs) {
		for (int blockedIndex = 0; blockedIndex < blocked.size(); blockedIndex++) {
			double prob = Math.random();
			if (prob < .3) {
				ready.add(blocked.get(blockedIndex));
				readyPcbs.add(blockedPcbs.get(blockedIndex));
				blocked.remove(blocked.get(blockedIndex));
				blockedPcbs.remove(blockedPcbs.get(blockedIndex));
			}
			if (ready.isEmpty()) {
				System.out.println("***IDLING***");
			}
			if (ready.isEmpty() && blocked.isEmpty()) {
				System.out.println("***ALL PROCESSES FINISHED***");
				System.exit(0);
			}
		}
	}
	
	/**
	 * This method displays the first part of the context switch, storing the data
	 * @param tempPcb - get the current pcb
	 * @param processor - get the processor
	 */
	public static void saveToPCB(ProcessControlBlock tempPcb, SimProcessor processor) {
		tempPcb.setCurrInstruction(processor.getCurrInstruction());
		tempPcb.setRegister1(processor.getRegister1Value());
		tempPcb.setRegister2(processor.getRegister2Value());
		tempPcb.setRegister3(processor.getRegister3Value());
		tempPcb.setRegister4(processor.getRegister4Value());
		System.out.printf("***CONTEXT SWITCH*** %nSave Data Process %s: Instruction: %d%nR1: %d\tR2: %d\tR3: %d\tR4: %d%n", tempPcb.getSimProcess().getName(), tempPcb.getCurrInstruction()-1, tempPcb.getRegister1(), tempPcb.getRegister2(), tempPcb.getRegister3(), tempPcb.getRegister4());
	}
	
	/**
	 * This method displays the second part of the context switch, retrieving the data
	 * @param pcb - get the current pcb
	 * @return updated current pcb
	 */
	public static ProcessControlBlock retrievePcbData(ProcessControlBlock pcb) {
		pcb.getCurrInstruction();
		pcb.getRegister1();
		pcb.getRegister2();
		pcb.getRegister3();
		pcb.getRegister4();
		if (pcb.getCurrInstruction() > 0){
			System.out.printf("***RETRIEVED DATA***  Process %s: Instruction: %d%nR1: %d\tR2: %d\tR3: %d\tR4: %d%n", pcb.getSimProcess().getName(), pcb.getCurrInstruction()-1, pcb.getRegister1(), pcb.getRegister2(), pcb.getRegister3(), pcb.getRegister4());
		}
		return pcb;
	}
}
