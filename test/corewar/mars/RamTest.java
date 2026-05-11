package corewar.mars;

import corewar.redcode.Instruction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RamTest{

    private Ram ram;

    @BeforeEach
    public void preparation(){
        ram = new Ram(8000);
    }

    @Test
    public void testGetSize(){
        assertEquals(8000, ram.getSize(), "The ram size must be 8000.");
    }

    @Test
    public void testGetOwner(){
        for (int i = 0; i < ram.getSize(); i++){
            assertEquals(0, ram.getOwner(i), "The list of owners must be filled out by 0.");
        }
    }

    @Test
    public void testSetOwner(){
        ram.setOwner(10, 1);
        assertEquals(1, ram.getOwner(10), "The owner of case 10 must be 1 after changing it.");
    }

    @Test
    public void testRead(){
        for (int i = 0; i < ram.getSize(); i++){
            assertEquals(new Instruction(Instruction.OpCode.DAT, 0, 0), ram.read(i), "The ram must be filled out by 'Dat 0 0'.");
        }
    }

    @Test
    public void testWrite(){
        ram.write(5, new Instruction(Instruction.OpCode.SUB, 0, 0), 1);
        assertEquals(new Instruction(Instruction.OpCode.SUB, 0, 0), ram.read(5), "The case 5 must be 'SUB 0 0' after changing it.");
        assertEquals(1, ram.getOwner(5), "The owner of case 5 must be 1 after changing it.");
    }
}