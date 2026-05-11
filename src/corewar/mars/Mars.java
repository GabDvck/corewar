package corewar.mars;
import corewar.redcode.*;
import corewar.redcode.exceptions.*;
import corewar.util.*;

/**
 * MARS (Memory Array Redcode Simulator).
 * This class acts as the main engine for the Core War game.
 * It manages the memory (RAM), the cycle execution, the process queue, and the victory conditions.
 */
public class Mars extends ModelListenable{

    private Cu cu;
    private ProcessQueue fifo;
    private Alu alu;
    private Ram ram;

    // Game state trackers
    private int counterW1;
    private int counterW2;
    private int nbLoop;
    private boolean runningGame;

    /**
     * Constructs the MARS simulator.
     * Initializes the hardware components (CU, ALU, RAM) and set initial state.
     * @param ramSize The size of the memory array.
     */
    public Mars(int ramSize){
        this.cu = new Cu();
        this.fifo = new ProcessQueue();
        this.alu = new Alu(ramSize);
        this.ram = new Ram(ramSize);
        this.counterW1 = 0;
        this.counterW2 = 0;
        this.nbLoop = 0;
        this.runningGame = false;
    }

    /**
     * Get the machine's ram.
     * @return The ram
     */
    public Ram getRam(){
        return this.ram;
    }

    /**
     * Clears the process queue entirely.
     * Used to clean up the simulator state before a new game starts.
     */
    public void emptyFifo(){
        this.fifo = new ProcessQueue();
        this.fireChange();
    }

    /**
     * Prepares and initializes a new game by loading two warriors into memory.
     * Clears the RAM and the process queue, parses the provided Redcode strings,
     * and places the warriors in the ram.
     * @param code1 The Redcode source code of the first warrior.
     * @param code2 The Redcode source code of the second warrior.
     */
    public void load(String code1, String code2) throws InvalidInstructionException{
        this.ram = new Ram(this.ram.getSize()); // Clean the ram
        this.emptyFifo(); // Clean the queue

        InstructionFactory instFact = new InstructionFactory();
        Warrior w1 = instFact.createWarriorFromString(code1);
        Warrior w2 = instFact.createWarriorFromString(code2);
        LoaderWarrior.loadWarriors(w1, w2, this.ram, this.fifo);

        this.counterW1 = 1;
        this.counterW2 = 1;
        this.nbLoop = 0;
        this.runningGame = true;

        this.fireChange();
    }

    /**
     * Directly loads two Warrior objects into the memory (RAM).
     * This method bypasses the string parsing phase (InstructionFactory), 
     * making it highly optimized and lightning-fast for generation algorithms.
     * @param w1 The first Warrior object to be loaded into the ram.
     * @param w2 The second Warrior object to be loaded into the ram.
     */
    public void load(Warrior w1, Warrior w2) {
        this.ram = new Ram(this.ram.getSize());
        this.emptyFifo();

        LoaderWarrior.loadWarriors(w1, w2, this.ram, this.fifo);

        this.counterW1 = 1;
        this.counterW2 = 1;
        this.nbLoop = 0;
        this.runningGame = true;

        this.fireChange(); 
    }

    /**
     * Advances the simulation by exactly one instruction cycle.
     * This method is useful for GUI animations using a Timer.
     * It executes the next process in the queue and checks for victory conditions.
     * @return 
     * -1 if the game is still running. 
     * 0 if the game ends in a Draw (loop limit reached).
     * 1 if Warrior 1 wins.
     * 2 if Warrior 2 wins.
     */
    public int step() {
        if (!this.runningGame){
            return -1; 
        }

        // 1. Play the next process
        Process process = this.fifo.dequeue();
        int counter = this.cu.executeCycle(process, this.ram, this.alu, this.fifo);
    
        if (process.getWarriorsId() == 1){
            this.counterW1 += counter;
        } else {
            this.counterW2 += counter;
        }
        this.nbLoop += 1;
    
        this.fireChange(); // Prevent the view

        // 2. Check if there is a winner
        if (this.counterW1 <= 0) {
            this.runningGame = false;
            return 2; // W2 wins
        } 
        else if (this.counterW2 <= 0) {
            this.runningGame = false;
            return 1; // W1 wins
        } 
        else if (this.nbLoop >= Config.getMaxLoops()) {
            this.runningGame = false;
            return 0; // Draw
        }

        // 3. Game continues
        return -1; 
    }

    /**
     * Executes an entire match instantly without GUI updates during the process.
     * This method is specifically designed and used for our generation algorithms, 
     * where rapid, headless simulation of matches is required.
     * @param code1 The Redcode source code of the first warrior.
     * @param code2 The Redcode source code of the second warrior.
     * @return 0 for a Draw, 1 for a Warrior 1 victory, 2 for a Warrior 2 victory.
     */
    public int game(String code1, String code2) throws InvalidInstructionException{
        this.load(code1, code2);
        while(this.counterW1 > 0 && this.counterW2 > 0 && this.nbLoop < Config.getMaxLoops()){
            Process process = this.fifo.dequeue();
            int counter = this.cu.executeCycle(process, this.ram, this.alu, this.fifo);
            if (process.getWarriorsId() == 1){
                this.counterW1 += counter;
            }
            else{
                this.counterW2 += counter;
            }
            this.nbLoop += 1;
        }
        this.runningGame = false;
        if (this.nbLoop == Config.getMaxLoops()){
            return 0;
        }
        else if (this.counterW1 == 0){
            return 2;
        }
        else{
            return 1;
        }
    }

    /**
     * Executes an entire match instantly using direct Warrior objects.
     * Useful for genetic algorithms.
     * @param w1 The first Warrior object fighting in the match.
     * @param w2 The second Warrior object fighting in the match.
     * @return 0 for a Draw (time limit reached), 1 for a Warrior 1 victory, 2 for a Warrior 2 victory.
     */
    public int game(Warrior w1, Warrior w2) {
        this.load(w1, w2);
        
        while(this.counterW1 > 0 && this.counterW2 > 0 && this.nbLoop < Config.getMaxLoops()){
            Process process = this.fifo.dequeue();
            int counter = this.cu.executeCycle(process, this.ram, this.alu, this.fifo);
            
            if (process.getWarriorsId() == 1){
                this.counterW1 += counter;
            }
            else{
                this.counterW2 += counter;
            }
            this.nbLoop += 1;
        }
        
        this.runningGame = false;
        
        if (this.nbLoop >= Config.getMaxLoops()){
            return 0;
        }
        else if (this.counterW1 <= 0){
            return 2;
        }
        else{
            return 1;
        }
    }

}