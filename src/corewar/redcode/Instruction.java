package corewar.redcode;
import java.util.*;

/**
 * Represents a single Redcode instruction in the Core War simulation.
 * An instruction consists of an Opcode and two operands (A and B), 
 * each with its own addressing mode and integer value.
 */
public class Instruction{

    /**
     * Enumeration of all supported Redcode operation codes.
     */
    public enum OpCode {
        DAT, MOV, ADD, SUB, JMP, JMZ, JMN, CMP, SLT, DJN, SPL
    };

    /**
     * Enumeration of the addressing modes used for operands.
     */
    public enum Adresse { 
        INDIRECT("@"), // @ 
        IMMEDIATE("#"), // # 
        PRECEDENT("<"), // < 
        DIRECT("");

        private final String symbol; 

        Adresse(String symbol) {
            this.symbol = symbol; 
        } 

        /**
         * Gets the string representation of the addressing mode.
         * @return The symbol (e.g., "#", "@", "<", or "").
         */
        public String getSymbol() { 
            return symbol; 
        }
        
    }

    private OpCode opcode;
    private Adresse adresse1;
    private Adresse adresse2;
    private int val1;
    private int val2;

    /**
     * Fully specifies a new Redcode instruction.
     * @param opcode The operation code (e.g., MOV, ADD).
     * @param adresse1 The addressing mode for operand A.
     * @param val1 The value for operand A.
     * @param adresse2 The addressing mode for operand B.
     * @param val2 The value for operand B.
     */
    public Instruction(OpCode opcode, Adresse adresse1, int val1, Adresse adresse2, int val2){
        this.opcode = opcode;
        this.adresse1 = adresse1;
        this.adresse2 = adresse2;
        this.val1 = val1;
        this.val2 = val2;
    }

    /**
     * Creates an instruction with DIRECT addressing modes by default.
     */
    public Instruction(OpCode opcode, int val1, int val2){
        this.opcode = opcode;
        this.adresse1 = Adresse.DIRECT;
        this.adresse2 = Adresse.DIRECT;
        this.val1 = val1;
        this.val2 = val2;
    }

    /**
     * Creates an instruction specifying the A addressing mode, defaulting B to DIRECT.
     */
    public Instruction(OpCode opcode, Adresse adresse1, int val1, int val2){
        this.opcode = opcode;
        this.adresse1 = adresse1;
        this.adresse2 = Adresse.DIRECT;
        this.val1 = val1;
        this.val2 = val2;
    }

    /**
     * Creates an instruction specifying the B addressing mode, defaulting A to DIRECT.
     */
    public Instruction(OpCode opcode, int val1, Adresse adresse2, int val2){
        this.opcode = opcode;
        this.adresse1 = Adresse.DIRECT;
        this.adresse2 = adresse2;
        this.val1 = val1;
        this.val2 = val2;
    }

    /**
     * Creates an instruction with a single operand (A), defaulting B to 0/DIRECT.
     */
    public Instruction(OpCode opcode, Adresse adresse1, int val1){
        this.opcode = opcode;
        this.adresse1 = adresse1;
        this.adresse2 = Adresse.DIRECT;
        this.val1 = val1;
        this.val2 = 0;
    }

    /**
     * Creates an instruction with a single operand (A) in DIRECT mode, defaulting B to 0/DIRECT.
     */
    public Instruction(OpCode opcode, int val1){
        this.opcode = opcode;
        this.adresse1 = Adresse.DIRECT;
        this.adresse2 = Adresse.DIRECT;
        this.val1 = val1;
        this.val2 = 0;
    }


    //------Getteurs------

    public OpCode getOpcode(){
        return this.opcode;
    }

    public Adresse getAdresse1(){
        //retrun object and not the symbol (ex: "IMMEDIATE" and not "#")
        //use .getSymbol() after to return the symbol (ex: inst.getAdresse1().getSymbol() => "#")
        return this.adresse1;
    }
    
    public Adresse getAdresse2(){
        //return object and not the symbol (ex: "IMMEDIATE" and not "#")
        //use .getSymbol() after to return the symbol (ex: inst.getAdresse2().getSymbol() => "#")
        return this.adresse2;
    }

    public int getVal1(){
        return this.val1;
    }

    public int getVal2(){
        return this.val2;
    }

    //-----Setters-----

    public void setOpcode(OpCode opcode){
        this.opcode = opcode;
    }

    public void setAdresse1(Adresse adresse1){
        this.adresse1 = adresse1;
    }

    public void setAdresse2(Adresse adresse2){
        this.adresse2 = adresse2;
    }

    public void setVal1(int val1){
        this.val1 = val1;
    }

    public void setVal2(int val2){
        this.val2 = val2;
    }

    /**
     * Creates a deep copy of this instruction.
     * @return A new Instruction object with identical values.
     */
    public Instruction copy(){
        return new Instruction(this.opcode, this.adresse1, this.val1, this.adresse2, this.val2);
    }

    @Override
    public String toString(){
        return (this.opcode + " " + this.adresse1.getSymbol() + this.val1 + ", " + this.adresse2.getSymbol()+ this.val2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Instruction other = (Instruction) obj;

        return opcode == other.opcode && adresse1 == other.adresse1 && adresse2 == other.adresse2 && val1 == other.val1 && val2 == other.val2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, adresse1, adresse2, val1, val2);
    }
}