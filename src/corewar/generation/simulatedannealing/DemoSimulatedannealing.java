package corewar.generation.simulatedannealing;

import corewar.redcode.*;

public class DemoSimulatedannealing {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(15);
        Warrior initial = sa.getInitial(); 
        Warrior best = sa.run();

        System.out.println("Warrior initial");
        for (Instruction inst : initial.getCode()) {
            System.out.println(inst);
        }
        System.out.println("\nOptimisation terminée");
        for (Instruction inst : best.getCode()) {
            System.out.println(inst);
        }
        System.out.println("\nScore Initial : " + sa.evaluate(initial));
        System.out.println("Score Final   : " + sa.evaluate(best));
    }
}