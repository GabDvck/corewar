package corewar.mars;

import java.util.*;
import corewar.redcode.*;

/**
 * Represents the circular memory (RAM) of the virtual machine.
 * The RAM consists of two parallel arrays: one for the instructions 
 * and one to track the owner (the warrior who last modified the cell).
 */
public class Ram{

    /** The memory cells containing the Redcode instructions. */
    private ArrayList<Instruction> instructions;

    /** Parallel array tracking the ID of the warrior who owns/modified each cell. */
    private ArrayList<Integer> owners;

    /** The total number of memory cells in the RAM. */
    private int size;

    /**
     * Creates a new RAM with the specified size.
     * All memory cells are initialized with DAT 0, 0 instructions,
     * and all owner IDs are set to 0 (neutral).
     * * @param size The total number of memory cells to allocate.
     */
    public Ram(int size){
        this.size = size;
        this.instructions = new ArrayList<>(size);
            //Init Dat 0 0 
        this.owners = new ArrayList<>(size);
        for(int i = 0; i < size; i++){
            instructions.add(new Instruction(Instruction.OpCode.DAT, 0, 0));
            owners.add(0);
        }
    }

    /**
     * Gets the total size of the RAM.
     * @return The number of memory cells
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Retrieves the ID of the warrior who last modified the specified memory cell.
     * @param adresse The memory address to check.
     * @return The ID of the owner (e.g., 0 for neutral, 1 for Warrior 1, 2 for Warrior 2).
     */
    public int getOwner(int adresse){
        return this.owners.get(adresse);
    }  

    /**
     * Directly updates the owner of a specific memory cell without changing its instruction.
     * @param adresse The memory address to update.
     * @param owner The ID of the new owner.
     */
    public void setOwner(int adresse, int owner){
        this.owners.set(adresse, owner);
    }
    /**
     * Reads an instruction from the specified address.
     * @param adresse The address to read from
     * @return The instruction at the given address
     */
    public Instruction read(int adresse){
        return this.instructions.get(adresse);
    }

    /**
     * Writes an instruction to the specified memory address and updates its owner.
     * @param adresse The memory address to write to.
     * @param instruction The new instruction to place in memory.
     * @param owner The ID of the warrior writing the instruction.
     */
    public void write(int adresse, Instruction instruction, int owner){
        this.instructions.set(adresse, instruction);
        this.owners.set(adresse, owner);
    }
}