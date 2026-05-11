package corewar.mars;

import corewar.redcode.*;
import java.util.*;

/**
 * Loads warriors into the CoreWar RAM and initializes their processes.
 * Provides methods for loading warriors at random positions, specific addresses,
 * or random with anti collisions.
 */
public class LoaderWarrior{

    private static final Random random = new Random();

    /** Private beacuse "new LoaderWarrior()" is not allowed */
    private LoaderWarrior(){
    }

    /**
     * Loads an unique warrior at a random position in RAM.
     * @param warrior The warrior to load
     * @param warriorId The warrior's identifier
     * @param ram The RAM into which warrior is loaded
     * @param queue The process queue where the first process is added
     * @return The starting address where the warrior was loaded
     */
    public static int loadWarriorRandom(Warrior warrior, int warriorId, Ram ram, ProcessQueue queue){
        int ramSize = ram.getSize();
        int codeSize = warrior.getSizeCode();
        if(codeSize > ramSize){
            throw new IllegalArgumentException("Warrior too big for the ram");
        }

        int firstAdress = random.nextInt(ramSize - codeSize);
        //charge warrior into the ram
        List<Instruction> code = warrior.getCode();
        for(int i = 0; i < code.size(); i++){
            ram.write(firstAdress + i, code.get(i), warriorId);
        }

        //create the first process
        queue.enqueue(new Process(firstAdress, warriorId));

        return firstAdress;
    }
    
    /**
     * Load exactly 2 warriors at random positons without collision
     * @param warrior1 The first warrior
     * @param warrior2 The second warrior
     * @param ram The RAM into wich warriors are loaded
     * @param queue The FIFO
     */
    public static void loadWarriorsRandom(Warrior warrior1, Warrior warrior2, Ram ram, ProcessQueue queue){
        int ramSize = ram.getSize();
        int codeSize1 = warrior1.getSizeCode();
        int codeSize2 = warrior2.getSizeCode();
        
        //First warrior
        int startAdress1 = random.nextInt(ramSize - codeSize1);
        int endAdress1 = startAdress1 + codeSize1 - 1;
        loadWarriorAt(warrior1, 1, ram, queue, startAdress1);  //Charging the first Warrior ! 
        //Second warrior
        int startAdress2 = random.nextInt(ramSize - codeSize2);
        int endAdress2 = startAdress2 + codeSize2 - 1;

        //Collision point
        boolean collision = (startAdress1 <= endAdress2) && (startAdress2 <= endAdress1);
        
        //If collision == TRUE -> search a new place to put warrior2 
        int attemps = 0;
        while (collision){
            startAdress2 = random.nextInt(ramSize - codeSize2);
            endAdress2 = startAdress2 + codeSize2 - 1;
            collision = (startAdress2 <= endAdress1) && (endAdress2 <= startAdress1);
            attemps ++;
            if (attemps > 100){
                throw new IllegalArgumentException("No place founded to place the second Warrior !");
            }
        }
        loadWarriorAt(warrior2, 2, ram, queue, startAdress2);
    }

    /**
     * Loads a warrior at a specific position.
     * @param warrior The warrior to load
     * @param warriorId The warrior's identifier
     * @param ram The RAM into which warrior is loaded
     * @param queue The process queue where the first process is added
     * @param startAdress The starting address
     */
    public static void loadWarriorAt(Warrior warrior, int warriorId, Ram ram, ProcessQueue queue, int startAdress){
        List<Instruction> code = warrior.getCode();
        for(int i = 0; i < code.size(); i++){
            ram.write((startAdress + i) % ram.getSize(), code.get(i).copy(), warriorId);
        }
        queue.enqueue(new Process(startAdress, warriorId));
    }

    /**
     * Loads exactly two warriors into the RAM at fixed, symmetrical positions.
     * The first warrior is loaded at the very beginning of the memory (address 0),
     * and the second warrior is loaded exactly at the halfway point.
     * @param warrior1 The first warrior to be loaded into memory.
     * @param warrior2 The second warrior to be loaded into memory.
     * @param ram The memory (RAM) where the instructions will be written.
     * @param queue The queue where the initial processes will be added.
     */
    public static void loadWarriors(Warrior warrior1, Warrior warrior2, Ram ram, ProcessQueue queue){
        if (warrior1.getSizeCode() > ram.getSize() / 2){
            throw new IllegalArgumentException("Warrior 1 is too big for the ram");
        }
        else if (warrior2.getSizeCode() > ram.getSize() / 2){
            throw new IllegalArgumentException("Warrior 2 is too big for the ram");
        }
        else{
            loadWarriorAt(warrior1, 1, ram, queue, 0);
            loadWarriorAt(warrior2, 2, ram, queue, ram.getSize() / 2);
        }
    }

}