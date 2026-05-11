package corewar.redcode;

import java.io.*;
import java.util.*;
import corewar.redcode.exceptions.*;

/**
 * A factory class responsible for parsing Redcode text and generating 
 * Instruction and Warrior objects. It handles both file reading and raw string parsing.
 */
public class InstructionFactory {

    public InstructionFactory() {
    }

    /**
     * Creates a BufferedReader for a given file name.
     * @param nomFichier The path to the file.
     * @return A BufferedReader ready to read the file.
     */
    public BufferedReader getReader(String nomFichier) throws FileNotFoundException {
        return new BufferedReader(new FileReader(nomFichier));
    }
    
    /**
     * Reads a file line by line into a list of strings.
     * @param nomFichier The path to the file.
     * @return A list of strings, where each string is a line from the file.
     */
    public List<String> lireLignes(String nomFichier) throws IOException {
        List<String> lignes = new ArrayList<>();
        
        try (BufferedReader br = getReader(nomFichier)) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                lignes.add(ligne);
            }
        }

        return lignes;
    }

    /**
     * Converts a string symbol into its corresponding Address mode enum.
     * @param s The string symbol (e.g., "#", "@", "<").
     * @return The corresponding Instruction.Adresse enum.
     */
    private Instruction.Adresse decodeAdresse(String s) {
        switch (s) {
            case "@": return Instruction.Adresse.INDIRECT;
            case "#": return Instruction.Adresse.IMMEDIATE;
            case "<": return Instruction.Adresse.PRECEDENT;
            default:  return Instruction.Adresse.DIRECT;
        }
    }

    /**
     * Parses a single line of Redcode and converts it into an Instruction object.
     * @param code The Redcode instruction string (e.g., "MOV #0, @1").
     * @return The parsed Instruction object, or null if the line is empty.
     */
    public Instruction stringToInstruction(String code) throws InvalidInstructionException{

        String cleanCode = code.toUpperCase().replace(",", " ").replaceAll("([#@<])", "$1 ");
        String[] ll = cleanCode.trim().split(" +");
        Instruction.OpCode opcode = null;

        try{
            opcode = Instruction.OpCode.valueOf(ll[0]);
        }
        catch(Exception e){
            throw new InvalidInstructionException("Invalid instruction: " + ll[0]);
        }

        if (ll.length == 2){
            int v1 = Integer.parseInt(ll[1]);

            if (opcode == Instruction.OpCode.DAT) {
                return new Instruction(opcode, 0, v1); 
            } 

            else {
                return new Instruction(opcode, v1, 0); 
            }
        }

        if (ll.length == 3) {
            if (Character.isDigit(ll[1].charAt(0)) || ll[1].charAt(0) == '-'){
                int v1 = Integer.parseInt(ll[1]);
                int v2 = Integer.parseInt(ll[2]);
                return new Instruction(opcode, v1, v2);
            }
            else{
                Instruction.Adresse a1 = decodeAdresse(ll[1]);
                int v1 = Integer.parseInt(ll[2]);
                return new Instruction(opcode, a1, v1);
            }
        }

        if (ll.length == 4) {
            if (Character.isDigit(ll[1].charAt(0)) || ll[1].charAt(0) == '-'){
                int v1 = Integer.parseInt(ll[1]);
                Instruction.Adresse a1 = decodeAdresse(ll[2]);
                int v2 = Integer.parseInt(ll[3]);
                return new Instruction(opcode, v1, a1, v2);
            }
            else{
                Instruction.Adresse a1 = decodeAdresse(ll[1]);
                int v1 = Integer.parseInt(ll[2]);
                int v2 = Integer.parseInt(ll[3]);
                return new Instruction(opcode, a1, v1, v2);
            }
        }

        if (ll.length == 5) {
            Instruction.Adresse a1 = decodeAdresse(ll[1]);
            int v1 = Integer.parseInt(ll[2]);
            Instruction.Adresse a2 = decodeAdresse(ll[3]);
            int v2 = Integer.parseInt(ll[4]);
            return new Instruction(opcode, a1, v1, a2, v2);
        }

        return null;
    }

    /**
     * Parses a Redcode file into a list of instructions.
     * @param nomFichier The path to the file.
     * @return An ArrayList containing the parsed instructions.
     */
    public ArrayList<Instruction> ListeInstruction(String nomFichier) throws InvalidInstructionException{
        ArrayList<Instruction> ls = new ArrayList<>();
        try {
            List<String> lsString = this.lireLignes(nomFichier);
            for (String ligne : lsString) {
                ls.add(this.stringToInstruction(ligne));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ls;
    }

    /**
     * Parses a multi-line string block of Redcode into a list of instructions.
     * @param code The multi-line Redcode string.
     * @return An ArrayList containing the parsed instructions.
     */
    public ArrayList<Instruction> ListeInstructionFromString(String code) throws InvalidInstructionException {
        ArrayList<Instruction> ls = new ArrayList<>();
        String[] tab = code.split("\n");
        for (String ligne: tab){
            ls.add(stringToInstruction(ligne));
        }
        return ls;
    }

    /**
     * Creates a new Warrior object from a Redcode file.
     * @param nomFichier The path to the file.
     * @return A fully constructed Warrior.
     */
    public Warrior createWarrior(String nomFichier) throws InvalidInstructionException{
        ArrayList<Instruction> instruct = ListeInstruction(nomFichier);
        return new Warrior(instruct);
    }

    /**
     * Creates a new Warrior object from a multi-line Redcode string.
     * @param code The multi-line Redcode string.
     * @return A fully constructed Warrior.
     */
    public Warrior createWarriorFromString(String code) throws InvalidInstructionException{
        ArrayList<Instruction> instruct = ListeInstructionFromString(code);
        return new Warrior(instruct);
    }
}
