package corewar.generation.simulatedannealing;

import corewar.mars.*;
import corewar.util.*;
import corewar.redcode.*;
import java.util.*;

public class SimulatedAnnealing {

    private Warrior current;
    private Warrior best;
    private double temperature = Config.getSaTemperature();
    private double minTemperature = Config.getSaMinTemperature();
    private double coolingRate = Config.getSaCoolingRate();
    private int codeSize;
    private Warrior initial;
    private Random random = new Random();
    private Warrior bomber;
    private Warrior imp;
    private Warrior dwarf;
    private Mars mars;

    public SimulatedAnnealing(int codeSize) {
        this.codeSize = codeSize;
        this.initial = createRandomWarrior();
        this.current = copyWarrior(initial);
        this.best = copyWarrior(initial);

        this.bomber = new Warrior(new ArrayList<>());
        bomber.addInstruction(new Instruction(Instruction.OpCode.MOV, Instruction.Adresse.IMMEDIATE, 0, Instruction.Adresse.INDIRECT, -1));

        this.imp = new Warrior(new ArrayList<>());
        this.imp.addInstruction(new Instruction(Instruction.OpCode.MOV, Instruction.Adresse.DIRECT, 0, Instruction.Adresse.DIRECT, 1));

        this.dwarf = new Warrior(new ArrayList<>());
        this.dwarf.addInstruction(new Instruction(Instruction.OpCode.ADD, Instruction.Adresse.IMMEDIATE, 4, Instruction.Adresse.DIRECT, 3));
        this.dwarf.addInstruction(new Instruction(Instruction.OpCode.MOV, Instruction.Adresse.DIRECT, 2, Instruction.Adresse.INDIRECT, 2));
        this.dwarf.addInstruction(new Instruction(Instruction.OpCode.JMP, Instruction.Adresse.DIRECT, -2, Instruction.Adresse.DIRECT, 0));
        this.dwarf.addInstruction(new Instruction(Instruction.OpCode.DAT, Instruction.Adresse.IMMEDIATE, 0, Instruction.Adresse.IMMEDIATE, 0));

        this.mars = new Mars(Config.getRamSize());
    }

    public Warrior getInitial(){
        return initial;
    }
    
    /**
     * Create a random warrior
     * @return A new warrior with random instructions
     */
    private Warrior createRandomWarrior(){
        ArrayList<Instruction> code = new ArrayList<>();
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
     * Run the simulated annealing 
     * @return The best warrior
     */
    public Warrior run() { //algo of annealing
        long currentScore = this.evaluate(current);
        long bestScore = currentScore;
        temperature = Config.getSaTemperature();
        while (temperature > minTemperature) {
            Warrior neighbor = findNeighbour(current);
            long neighborScore = this.evaluate(neighbor);
            if (acceptable(currentScore, neighborScore)) {
                current = neighbor;
                currentScore = neighborScore;
                if (neighborScore > bestScore) { 
                    best = copyWarrior(neighbor);
                    bestScore = neighborScore;
                }
            }
            temperature *= (1.0 - coolingRate);
        }
        return best;
    }

     /**
     * Evaluate a warrior, and give theirs score
     * @param w The warrior to evaluate
     * @return Score of the warrior
     */
    public long evaluate(Warrior w) {
        int score = 0;
        
        int result = this.mars.game(w, this.bomber);//bomber
        if (result == 1) score += 30;
        if (result == 0) score += 7;
        else score += -15;

        result = this.mars.game(w, this.imp);//random
        if (result == 1) score += 20;
        if (result == 0) score += 5;
        else score += -10;

        result = this.mars.game(w, this.dwarf);//dwarf
        if (result == 1) score += 50;
        if (result == 0) score += 10;
        else score += -20;

        return score;
    }

     /**
     * Give a new warrior, neighbors of the warrior in param
     * @param w Gives the neighbors of the warrior
     * @return New warrior
     */
    private Warrior findNeighbour(Warrior w) {
        ArrayList<Instruction> code = new ArrayList<>(w.getCode());
        if (code.isEmpty()) return new Warrior(code);

        int i = random.nextInt(code.size());
        Instruction old = code.get(i);
        int type = random.nextInt(5);

        if (type == 0 && code.size() >= 2) { // swap two instruction
            int j = random.nextInt(code.size());
            while (j == i) {
                j = random.nextInt(code.size());
            }
            Collections.swap(code, i, j);

        } else if (type == 1) { // new opcode
            Instruction.OpCode[] opcodes = Instruction.OpCode.values();
            code.set(i, new Instruction(opcodes[random.nextInt(opcodes.length)], old.getAdresse1(), old.getVal1(), old.getAdresse2(), old.getVal2()));

        } else if (type == 2) { // change val
            int delta = random.nextInt(11) - 5;
            if (random.nextBoolean()) {
                code.set(i, new Instruction(old.getOpcode(), old.getAdresse1(), old.getVal1() + delta, old.getAdresse2(), old.getVal2()));
            } else {
                code.set(i, new Instruction(old.getOpcode(), old.getAdresse1(), old.getVal1(), old.getAdresse2(), old.getVal2() + delta));
            }

        } else if (type == 3) { // change adresse
            Instruction.Adresse[] adresses = Instruction.Adresse.values();
            Instruction.Adresse newAdresses = adresses[random.nextInt(adresses.length)];
            if (random.nextBoolean()) {
                code.set(i, new Instruction(old.getOpcode(), newAdresses, old.getVal1(), old.getAdresse2(), old.getVal2()));
            } else {
                code.set(i, new Instruction(old.getOpcode(), old.getAdresse1(), old.getVal1(), newAdresses, old.getVal2()));
            }

        } else { // created random instruction
            code.set(i, createRandomInstruction());
        }

        return new Warrior(code);
    }

    /**
     * Evaluate whether the neighbor's warrior score is higher than the current warrior score
     * @param current score of current warrior
     * @param neighborS score of neighbor's warrior
     * @return True if neighborS >= current
     */
    private boolean acceptable(long current, long neighborS) {
        if (neighborS >= current) {
            return true;
        }
        double delta = current - neighborS; 
        double proba = Math.exp(-delta / temperature);
        return random.nextDouble() < proba;
    }


     /**
     * Give a copy of the Warrior in param
     * @param w Warrior to copy
     * @return New warrior
     */
    private Warrior copyWarrior(Warrior w) {
        ArrayList<Instruction> newCode = new ArrayList<>();
        for (Instruction inst : w.getCode()) {
            newCode.add(new Instruction(inst.getOpcode(), inst.getAdresse1(), inst.getVal1(), inst.getAdresse2(), inst.getVal2()));
        }
        return new Warrior(newCode);
    }
}