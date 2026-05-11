package corewar.generation.genetic;

import corewar.redcode.*;
import corewar.redcode.exceptions.*;

public class GeneticAlgoDemo {
    public static void main(String[] args) throws InvalidInstructionException {
        
        GeneticAlgo algo = new GeneticAlgo(200, 10, 300, 0.10);

        System.out.println("Evolve in progress");

        Warrior best = algo.evolve();

        System.out.println("The best is :\n");
        System.out.println(best.toString());
    }
}