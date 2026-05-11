package corewar.mars;

import corewar.redcode.*;

/**
 * Control Unit (CU) for the MARS simulator.
 * Responsible for the instruction lifecycle: fetch, decode, and execute.
 * It acts as the bridge between the process, the RAM, and the ALU.
 */
public class Cu{

    /** * Temporary register to hold the spawn address for a new process.
     * Set to -1 when no split is pending.
     */
    private int splSpawn;

    /**
     * Initializes the Control Unit.
     * Sets the split spawn register to -1 (inactive).
     */
    public Cu(){
        this.splSpawn = -1;
    }

    /**
     * Decodes a raw operand into an effective memory address.
     * Handles addressing modes (Direct, Indirect, Pre-decrement).
     * @param currentPc The current Program Counter of the process.
     * @param val The raw value from the instruction field.
     * @param mode The addressing mode (DIRECT, INDIRECT, etc.).
     * @param ram The RAM.
     * @return The effective memory address, or -1 if the mode is IMMEDIATE.
     */
    public int decode(int currentPc, int val, Instruction.Adresse mode, Ram ram, Alu alu, int owner){
        int newPc;
        switch(mode){
            case DIRECT:
                return alu.goTo(currentPc, val);
            case INDIRECT:
                newPc = alu.goTo(currentPc, val);
                int offset = ram.read(newPc).getVal2();
                return alu.goTo(newPc, offset);
            case PRECEDENT:
                newPc = alu.goTo(currentPc, val);
                Instruction inst = ram.read(newPc);
                int newVal = alu.goTo(inst.getVal2(), -1);
                inst.setVal2(newVal);
                ram.setOwner(newPc, owner);
                return alu.goTo(newPc, newVal);
            default:
                return -1; //if mode is IMMEDIATE
        }
    }

    /**
     * Executes a single Redcode instruction.
     * Performs logic, arithmetic, or control flow operations based on the Opcode.
     * @param currentPc The process's current Program Counter.
     * @param instruction The instruction to execute.
     * @param ram The RAM to modify or read from.
     * @param alu The Arithmetic Logic Unit for calculations.
     * @return The calculated next PC for the process, or -1 if the process dies (DAT).
     */
    public int execute(int currentPc, Instruction instruction, Ram ram, Alu alu, int owner){
        int adresseA = decode(currentPc, instruction.getVal1(), instruction.getAdresse1(), ram, alu, owner);
        int adresseB = decode(currentPc, instruction.getVal2(), instruction.getAdresse2(), ram, alu, owner);
        if (adresseB == -1){
            adresseB = currentPc;
        }
        boolean skip;
        Instruction target;
        switch(instruction.getOpcode()){
            case MOV:
                if (adresseA == -1){
                    ram.read(adresseB).setVal2(instruction.getVal1());
                    ram.setOwner(adresseB, owner);
                }
                else{
                    ram.write(adresseB, ram.read(adresseA).copy(), owner);
                }
                return alu.incrementePc(currentPc);
            case ADD:
                if (adresseA == -1){
                    alu.add(instruction, ram.read(adresseB), true);
                }
                else{
                     alu.add(ram.read(adresseA), ram.read(adresseB), false);
                }
                ram.setOwner(adresseB, owner);
                return alu.incrementePc(currentPc);
            case SUB:
                if (adresseA == -1){
                    alu.sub(instruction, ram.read(adresseB), true);
                }
                else{
                    alu.sub(ram.read(adresseA), ram.read(adresseB), false);
                }
                ram.setOwner(adresseB, owner);
                return alu.incrementePc(currentPc);
            case JMP:
                if (adresseA == -1){
                    return -1;
                }
                else{
                    return adresseA;
                }
            case JMZ:
                target = ram.read(adresseB);
                if (target.getVal2() == 0){
                    if (adresseA == -1){
                        return -1;
                    }
                    return adresseA;
                }
                else{
                    return alu.incrementePc(currentPc);
                }
            case JMN:
                target = ram.read(adresseB);
                if (target.getVal2() != 0){
                    if (adresseA == -1){
                        return -1;
                    }
                    return adresseA;
                }
                else{
                    return alu.incrementePc(currentPc);
                }
            case CMP:
                if (adresseA == -1){
                    skip = alu.cmp(instruction, ram.read(adresseB), true);
                }
                else{
                    skip = alu.cmp(ram.read(adresseA), ram.read(adresseB), false);
                }
                if (skip){
                    return alu.goTo(currentPc, 2);
                }
                else{
                    return alu.incrementePc(currentPc);
                }
            case SLT:
                if (adresseA == -1){
                    skip = alu.slt(instruction, ram.read(adresseB), true);
                }
                else{
                    skip = alu.slt(ram.read(adresseA), ram.read(adresseB), false);
                }
                if (skip){
                    return alu.goTo(currentPc, 2);
                }
                else{
                    return alu.incrementePc(currentPc);
                }
            case DJN:
                target = ram.read(adresseB);
                skip = alu.djn(target);
                ram.setOwner(adresseB, owner);
                if (skip){
                    return adresseA;
                }
                else{
                    return alu.incrementePc(currentPc);
                }
            case SPL:
                this.splSpawn = adresseA;
                return alu.incrementePc(currentPc);
            case DAT:
                return -1;
            default:
                return 0;
        }
    }

    /**
     * Orchestrates the complete execution cycle for a specific process.
     * Steps:
     * 1. Reads the instruction at the process's PC.
     * 2. Executes the instruction.
     * 3. Updates the process PC and re-queues it (if alive).
     * 4. Handles the creation of a new process if a SPL instruction was executed.
     * @param process The process currently being executed.
     * @param ram The machine's RAM.
     * @param alu The machine's ALU.
     * @param fifo The process queue.
     * @return 1 if a split occurred, 0 if normal execution, -1 if the process died.
     */
    public int executeCycle(Process process, Ram ram, Alu alu, ProcessQueue fifo){
        Instruction instruction = ram.read(process.getPc());
        int newPc = execute(process.getPc(), instruction, ram, alu, process.getWarriorsId());
        if (newPc != -1){
            newPc = alu.modulo(newPc);
            process.setPc(newPc);
            fifo.enqueue(process);
            if (splSpawn != -1){
                Process newProcess = new Process(this.splSpawn, process.getWarriorsId());
                fifo.enqueue(newProcess);
                this.splSpawn = -1;
                return 1;
            }
            return 0;
        }
        return -1;
    }

}