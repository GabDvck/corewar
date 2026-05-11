package corewar.mars;

import corewar.redcode.*;

/**
 * The ALU manages arithmetic calculations and logical comparisons.
 * It ensures all memory addresses and values wrap around the circular RAM.
 */
public class Alu{

    private int ramSize;

    /**
     * Constructor for the ALU.
     * @param ramSize The total number of cells in the RAM (e.g., 8000).
     */
    public Alu(int ramSize){
        this.ramSize = ramSize;
    }

    /**
     * Calculates the modulo to ensure circular memory behavior.
     * @param number The value to be normalized.
     * @return The normalized value within the range [0, ramSize - 1].
     */
    public int modulo(int number){
        return ((number % this.ramSize) + this.ramSize) % this.ramSize;
    }

    /**
     * Calculates the next instruction address.
     * @param pc The current Program Counter.
     * @return The next address in the circular memory (PC + 1).
     */
    public int incrementePc(int pc){
        return modulo(pc + 1);
    }

    /**
     * Calculates the effective address by adding an offset to a base address,
     * ensuring the result wraps around the circular memory.
     * @param pc The base address (usually the current Program Counter).
     * @param val The offset to add (can be positive or negative).
     * @return The normalized memory address within the bounds of the RAM.
     */
    public int goTo(int pc, int val){
        return modulo(pc + val);
    }

    /**
     * Executes the ADD instruction.
     * @param source The source instruction containing the values to add.
     * @param target The target instruction to be modified in RAM.
     * @param isImmediate If true (# mode), adds source A-field to target B-field. Otherwise, adds A to A and B to B.
     */
    public void add(Instruction source, Instruction target, boolean isImmediate){
        if (isImmediate){
            target.setVal2(modulo(target.getVal2() + source.getVal1()));
        }
        else{
            target.setVal1(modulo(target.getVal1() + source.getVal1()));
            target.setVal2(modulo(target.getVal2() + source.getVal2()));
        }
    }

   /**
     * Executes the SUB (Subtraction) instruction.
     * @param source The source instruction containing the values to subtract.
     * @param target The target instruction to be modified in RAM.
     * @param isImmediate True if Mode A is immediate (#).
     */
    public void sub(Instruction source, Instruction target, boolean isImmediate){
        if (isImmediate){
            target.setVal2(modulo(target.getVal2() - source.getVal1()));
        }
        else{
            target.setVal1(modulo(target.getVal1() - source.getVal1()));
            target.setVal2(modulo(target.getVal2() - source.getVal2()));
        }
    }

  /**
     * Executes the DJN (Decrement and Jump if Not zero) logic.
     * @param target The instruction whose B-field is to be decremented.
     * @return true if the decremented value is non-zero, false otherwise.
     */
    public boolean djn(Instruction target){
        int newValue = modulo(target.getVal2() - 1);
        target.setVal2(newValue);
        return newValue != 0;
    }

    /**
     * Executes the CMP (Compare) instruction.
     * @param source The source instruction to compare.
     * @param target The target instruction to compare against.
     * @param isImmediate True if Mode A is immediate (#).
     * @return true if the compared values or instructions are equal.
     */
    public boolean cmp(Instruction source, Instruction target, boolean isImmediate){
        if (isImmediate){
            return source.getVal1() == target.getVal2();
        }
        return (source.getVal1() == target.getVal1()) && (source.getVal2() == target.getVal2()) && (source.getOpcode() == target.getOpcode());
    }

    /**
     * Executes the SLT (Skip if Less Than) instruction.
     * @param source The source instruction.
     * @param target The target instruction.
     * @param isImmediate True if Mode A is immediate (#).
     * @return true if the source value is strictly less than the target value.
     */
    public boolean slt(Instruction source, Instruction target, boolean isImmediate){
        if (isImmediate){
            return source.getVal1() < target.getVal2();
        }
        return source.getVal2() < target.getVal2();
    }

}