package corewar.mars;


/**
 * Represents a process (execution thread) for a warrior.
 * Each process maintains a program counter (PC) pointing to its current position in RAM.
 */
public class Process{

    private int pc;   //ProgramCounter = executionnal address
    private int warriorId; //warrior process

    /**
     * Create a new process.
     * @param pc The initial program counter (address in RAM)
     * @param warriorId The ID of the warrior this process belongs to
     */
    public Process(int pc, int warriorId){  //Constructor
        this.pc = pc;
        this.warriorId = warriorId;
    }

    /**
     * Get the current program counter value.
     * @return The current address in RAM where this process is executing
     */
    public int getPc(){  //pc getter
        return pc;
    }

    /**
     * Set the program counter to a new address.
     * @param pc The new program counter value
     */
    public void setPc(int pc){  //pc setter
        this.pc = pc;
    }
    
    /**
     * Get the warrior ID this process belongs to.
     * @return The warrior's identifier
     */
    public int getWarriorsId(){  //warriorId getter
        return warriorId;
    }
}