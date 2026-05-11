package corewar.util;

import java.io.*;
import java.util.*;

/**
 * Utility class for loading configuration parameters from the config.properties file.
 */
public class Config {

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Warning: Can't find config.properties. Use of default values.");
        }
    }

    /**
     * Gets the configured size for the RAM.
     * @return The size of the RAM.
     */
    public static int getRamSize() {
        return Integer.parseInt(properties.getProperty("mars.ram_size", "8000"));
    }

    /**
     * Get the maximum number of loops before a draw.
     * @return The maximum number of loops.
     */
    public static int getMaxLoops() {
        return Integer.parseInt(properties.getProperty("mars.max_loops", "80000"));
    }

    /**
     * Gets the speed for the GUI timer.
     * @return The GUI timer speed in milliseconds.
     */
    public static int getTimerSpeed() {
        return Integer.parseInt(properties.getProperty("gui.timer_speed", "10"));
    }

    /**
     * Gets the population size for the Genetic Algorithm.
     * @return The number of warriors in each generation.
     */
    public static int getGeneticPopulationSize() {
        return Integer.parseInt(properties.getProperty("genetic.population_size", "200"));
    }

    /**
     * Gets the maximum number of instructions for a generated warrior.
     * @return The size of the code.
     */
    public static int getGeneticCodeSize() {
        return Integer.parseInt(properties.getProperty("genetic.code_size", "15"));
    }

    /**
     * Gets the number of generations the algorithm will run.
     * @return The total number of generations.
     */
    public static int getGeneticGenerations() {
        return Integer.parseInt(properties.getProperty("genetic.generations", "300"));
    }

    /**
     * Gets the mutation rate for the Genetic Algorithm.
     * @return The probability of mutation (between 0.0 and 1.0).
     */
    public static double getGeneticMutationRate() {
        return Double.parseDouble(properties.getProperty("genetic.mutation_rate", "0.08"));
    }

    /**
     * Gets the initial temperature for Simulated Annealing.
     * @return The starting temperature.
     */
    public static double getSaTemperature() {
        return Double.parseDouble(properties.getProperty("sa.temperature", "8000.0"));
    }

    /**
     * Gets the minimum temperature for Simulated Annealing.
     * @return The stopping temperature.
     */
    public static double getSaMinTemperature() {
        return Double.parseDouble(properties.getProperty("sa.min_temperature", "0.1"));
    }

    /**
     * Gets the cooling rate for Simulated Annealing.
     * @return The cooling rate multiplier.
     */
    public static double getSaCoolingRate() {
        return Double.parseDouble(properties.getProperty("sa.cooling_rate", "0.001"));
    }
}