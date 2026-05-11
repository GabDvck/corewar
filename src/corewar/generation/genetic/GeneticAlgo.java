package corewar.generation.genetic;

import corewar.redcode.*;
import corewar.redcode.exceptions.*;
import corewar.mars.*;
import corewar.util.*;
import java.util.*;

public class GeneticAlgo{
    private List<Warrior> population;
    private Random random;
    private int populationSize;
    private int codeSize;
    private double mutationRate;
    private int generation;
    private List<Warrior> benchmarks;
    private Mars mars;
    
    private static final int TournamentSize = 10;

/**
 * Loads an unique warrior at a random position in RAM
 * @param populationSize number of warriors
 * @param codeSize number of instructions for each warrior
 * @param generation number of generation to evolve
 * @param mutationRate rate of mutation
 */
    public GeneticAlgo(int populationSize, int codeSize, int generation, double mutationRate){
        this.populationSize = populationSize;
        this.codeSize = codeSize;
        this.generation = generation;
        this.mutationRate = mutationRate;
        this.random = new Random();
        this.population = new ArrayList<>();
        
        this.benchmarks = new ArrayList<>();
        this.benchmarks.add(createImp());
        for (int i = 0; i < populationSize; i++){
            population.add(createRandomWarrior());
        }

        this.mars = new Mars(Config.getRamSize());
    }

    /**
     * Creates a basic Imp warrior with a single copy instruction
     * @return a new Imp warrior
     */
    private Warrior createImp() {
        List<Instruction> code = new ArrayList<>();
        code.add(new Instruction(
                Instruction.OpCode.MOV,
                Instruction.Adresse.DIRECT, 0,
                Instruction.Adresse.DIRECT, 1
        ));
        return new Warrior(code);
    }

    /**
     * Create a random warrior
     * @return A new warrior with random instructions
     */
    private Warrior createRandomWarrior(){
        List<Instruction> code = new ArrayList<>();
        for (int i = 0; i<codeSize; i++){
            code.add(createRandomInstruction());
        }
        return new Warrior(code);
    }

    /**
     * Create a random instruction
     * @return A random instruction
     */
    private Instruction createRandomInstruction(){
        Instruction.OpCode[] opcodes = Instruction.OpCode.values();
        Instruction.Adresse[] address = Instruction.Adresse.values();

        Instruction.OpCode opcode = opcodes[random.nextInt(opcodes.length)];
        Instruction.Adresse addr1 = address[random.nextInt(address.length)];
        Instruction.Adresse addr2 = address[random.nextInt(address.length)];
        int val1 = random.nextInt(200) - 100;
        int val2 = random.nextInt(200) - 100;
        return new Instruction(opcode, addr1, val1, addr2, val2);
    }
    /**
     * Applies random genetic mutations to a warrior's instruction set based on the mutation rate
     * @param warrior the warrior to mutate
     */
    private void mutate(Warrior warrior){
        List<Instruction> code = warrior.getCode();

        for (int i = 0; i < code.size(); i++){
            if (random.nextDouble() < mutationRate){
                Instruction oldInst = code.get(i);
                
                // 7 types of mutations 
                int mutationType = random.nextInt(7);
                
                Instruction.OpCode op = oldInst.getOpcode();
                Instruction.Adresse addr1 = oldInst.getAdresse1();
                int val1 = oldInst.getVal1();
                Instruction.Adresse addr2 = oldInst.getAdresse2();
                int val2 = oldInst.getVal2();

                switch (mutationType) {
                    case 0: // Change the OpCode randomly
                        op = Instruction.OpCode.values()[random.nextInt(Instruction.OpCode.values().length)];
                        code.set(i, new Instruction(op, addr1, val1, addr2, val2));
                        break;
                    case 1: // changes addressing mode 1
                        addr1 = Instruction.Adresse.values()[random.nextInt(Instruction.Adresse.values().length)];
                        code.set(i, new Instruction(op, addr1, val1, addr2, val2));
                        break;
                    case 2: // slightly modifies val1
                        val1 += random.nextInt(11) - 5;
                        code.set(i, new Instruction(op, addr1, val1, addr2, val2));
                        break;
                    case 3: // changes addressing mode 2
                        addr2 = Instruction.Adresse.values()[random.nextInt(Instruction.Adresse.values().length)];
                        code.set(i, new Instruction(op, addr1, val1, addr2, val2));
                        break;
                    case 4: //slightly modifies val2
                        val2 += random.nextInt(11)-5;
                        code.set(i, new Instruction(op, addr1, val1, addr2, val2));
                        break;
                    case 5: //swap : exchanges two instructions
                        int swapTarget = random.nextInt(code.size());
                        Instruction temp = code.get(i);
                        code.set(i, code.get(swapTarget));
                        code.set(swapTarget, temp);
                        break;
                    case 6: // complete replacement of an instruction
                        code.set(i, createRandomInstruction());
                        break;
                }
            }
        }   
    }   

    /**
     * Evaluate a warrior by making it fight against others
     * @param warrior The warrior to evaluate
     * @param index Index of the warrior
     * @return Number of wins
     */
    private int evaluateWarrior(Warrior warrior, int index) throws InvalidInstructionException{
        int wins = 0;
        
        
        List<Integer> opponents = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            if (i != index) opponents.add(i);
        }
        Collections.shuffle(opponents, random);

        int nbOpponents = Math.min(TournamentSize, opponents.size());
        for (int k = 0; k < nbOpponents; k++) {
            Warrior code2 = population.get(opponents.get(k));
            int result = this.mars.game(warrior, code2);
            if (result == 1) {
                wins += 3; 
            } else if (result == 0) {
                wins += 1;
            }
        }
        //If the warriors win against the benchmark (IMP WARRIOR) he wins double
        for (Warrior benchmark : benchmarks) {
            int result = this.mars.game(warrior, benchmark);
            if (result == 1) {
                wins += 6;
            } else if (result == 0) {
                wins += 2;
            }
        }
        return wins;
    }

    /**
     * Select a parent using simple roulette wheel selection
     * @param scores Fitness scores
     * @return A selected warrior
     */
    private Warrior selectParent(int[] scores){
        int total = 0;
        for (int score : scores){
            total += score + 1;
        }

        int randomVal = random.nextInt(total);
        int res = 0;
        for (int i = 0; i < populationSize; i++){
            res += scores[i] +1;
            if (res >= randomVal){
                return population.get(i);
            }
        }
        return population.get(population.size() -1);
    }

    /**
    * Copies an instruction
    * @param instruction The instruction to copy
    * @return A new instruction
    */
    private Instruction copyInstruction(Instruction instruction) {
        return new Instruction(instruction.getOpcode(),instruction.getAdresse1(),instruction.getVal1(),instruction.getAdresse2(),instruction.getVal2());
    }

    /**
     * Copies a warrior.
     * @param warrior The warrior to copy
     * @return A new warrior with the same code
     */
    private Warrior copyWarrior(Warrior warrior) {
        List<Instruction> code = new ArrayList<>();
        for (Instruction instruction : warrior.getCode()) {
            code.add(copyInstruction(instruction));
        }
        return new Warrior(code);
    }

    /**
     * Made a crossover between two warriors, the cut-off point is random
     * @param parent1 First parent
     * @param parent2 Second parent
     * @return A new warrior
     */
    private Warrior crossover(Warrior parent1, Warrior parent2){
        List<Instruction> childCode = new ArrayList<>();
        List<Instruction> code1 = parent1.getCode();
        List<Instruction> code2 = parent2.getCode();
        int crossPoint = 1 + random.nextInt(codeSize -1);
        for (int i=0; i<crossPoint; i++){
            childCode.add(copyInstruction(code1.get(i)));
        }
        for (int i=crossPoint;i<codeSize; i++){
            childCode.add(copyInstruction(code2.get(i)));
        }
        return new Warrior(childCode);
    }

    /**
     * Evolves the population of warriors over multiple generations using evaluation, crossover, and mutation
     * @return the best performing warrior after all generations are processed
     * @throws InvalidInstructionException if an invalid instruction is evaluated during the simulation
     */
    public Warrior evolve() throws InvalidInstructionException{
        for (int gen = 0; gen < generation; gen++){

            int[] scores = new int[populationSize];
            for (int i = 0; i < populationSize; i++){
                scores[i] = evaluateWarrior(population.get(i), i);
            }

            int maxscore =0;
            int bestIndex = 0;
            for (int i = 0; i<populationSize; i++){
                if (scores[i]>maxscore){
                    maxscore = scores[i];
                    bestIndex =i;
                }
            }
            ArrayList<Warrior> newPopulation = new ArrayList<>();
            newPopulation.add(copyWarrior(population.get(bestIndex)));
            

            //replace 10% of the population with random warriors in each generation to maintain genetic diversity
            int nbRandom = populationSize / 10;
            for (int i = 0; i < nbRandom; i++) {
                newPopulation.add(createRandomWarrior());
            }
            while (newPopulation.size() < populationSize){
                Warrior parent1 = selectParent(scores);
                Warrior parent2 = selectParent(scores);
                Warrior child = crossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
            }
            population = newPopulation;

        }
        int[] finalscores = new int[populationSize];
        for (int i = 0; i<populationSize;i++){
            finalscores[i] = evaluateWarrior(population.get(i), i);
        }
        int bestIndex=0;
        for(int i =1; i<populationSize; i++){
            if (finalscores[i] >finalscores[bestIndex]){
                bestIndex =i;
            }
        }
        return population.get(bestIndex);
    }


}