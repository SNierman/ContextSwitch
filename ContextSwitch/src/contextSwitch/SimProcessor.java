package contextSwitch;

import java.util.Random;

public class SimProcessor {
	
	private SimProcess currentProcess;
	private int currInstruction;
	private int register1, register2, register3, register4;
	
	public SimProcessor() {
		super();
		this.currInstruction = 0;
		
	}
	
	public void setSimProcess(SimProcess process) {
		this.currentProcess = process;
	}
	
	public SimProcess getSimProcess() {
		return this.currentProcess;
	}
	
	public void setRegister1Value(int register1) {
		this.register1 = register1;
	}
	
	public int getRegister1Value() {
		return this.register1;
	}
	
	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}
	
	public int getCurrInstruction() {
		return this.currInstruction;
	}
	
	public ProcessState executeNextInstruction() {
		ProcessState result = currentProcess.execute(currInstruction);
		currInstruction++;
		Random rand = new Random();
		setRegister1Value(rand.nextInt());
		setRegister2Value(rand.nextInt());
		setRegister3Value(rand.nextInt());
		setRegister4Value(rand.nextInt());
		return result;
	}

	public int getRegister2Value() {
		return register2;
	}

	public void setRegister2Value(int register2) {
		this.register2 = register2;
	}

	public int getRegister3Value() {
		return register3;
	}

	public void setRegister3Value(int register3) {
		this.register3 = register3;
	}

	public int getRegister4Value() {
		return register4;
	}

	public void setRegister4Value(int register4) {
		this.register4 = register4;
	}

}
