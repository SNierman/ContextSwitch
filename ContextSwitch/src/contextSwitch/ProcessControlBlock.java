package contextSwitch;

public class ProcessControlBlock {
	
	private SimProcess process;
	private int currInstruction;
	private int register1, register2, register3, register4;

	public ProcessControlBlock(SimProcess process) {
		this.process = process;
		this.currInstruction = 0;
	}
	
	public SimProcess getSimProcess() {
		return this.process;
	}
	
	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}
	
	public int getCurrInstruction() {
		return this.currInstruction;
	}

	public int getRegister1() {
		return register1;
	}

	public void setRegister1(int register1) {
		this.register1 = register1;
	}

	public int getRegister2() {
		return register2;
	}

	public void setRegister2(int register2) {
		this.register2 = register2;
	}

	public int getRegister3() {
		return register3;
	}

	public void setRegister3(int register3) {
		this.register3 = register3;
	}

	public int getRegister4() {
		return register4;
	}

	public void setRegister4(int register4) {
		this.register4 = register4;
	}

}
