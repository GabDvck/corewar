package corewar.redcode;

import java.util.*;

/**
 * Represents a Core War program (a warrior) before it is loaded into the MARS arena.
 * It contains the list of Redcode instructions that make up its source code.
 */
public class Warrior{

    private List<Instruction> code;

    /**
     * Constructs a new Warrior with the provided list of instructions.
     * @param code The ArrayList of Redcode instructions.
     */
    public Warrior(List<Instruction> code){
        this.code = code;
    }

    /**
     * Constructs a new Warrior with an empty list of instructions.
     */
    public Warrior(){
        this.code = new ArrayList<Instruction>();
    }

    /**
     * Appends a new instruction to the end of the warrior's code.
     * @param instruction The instruction to add.
     */
    public void addInstruction(Instruction instruction){
        this.code.add(instruction);
    }

    /**
     * Retrieves the entire list of instructions of the warrior.
     * @return The ArrayList containing the warrior's code.
     */
    public List<Instruction> getCode(){
        return this.code;
    }
    
    /**
     * Gets the total number of instructions this warrior has.
     * @return The size of the code.
     */
    public int getSizeCode(){
        return this.code.size();
    }

    @Override
    public String toString(){
        String res = "";
        for (Instruction instr : this.code){
            res += instr.toString() +"\n";
        }
        return res;
    }
}