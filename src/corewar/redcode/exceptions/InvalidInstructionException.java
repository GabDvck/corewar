package corewar.redcode.exceptions;

/**
 * Exception thrown to indicate that a Redcode instruction is malformed or invalid.
 * This typically occurs during the parsing phase (e.g., in the InstructionFactory) 
 */
public class InvalidInstructionException extends Exception{

    /**
     * Constructs a new InvalidInstructionException with the specified detail message.
     * @param s The detail message explaining exactly why the instruction is invalid 
     */
    public InvalidInstructionException (String s){
        super(s);
    }

}