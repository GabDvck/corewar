package corewar.mars;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessTest{

    private Process process;

    @BeforeEach
    public void preparation(){
        process = new Process(10, 1);
    }

    @Test
    public void testGetPc(){
        assertEquals(10, process.getPc(), "Process pc must be 10.");
    }

    @Test
    public void testSetPc(){
        process.setPc(2);
        assertEquals(2, process.getPc(), "Process pc must be 2 after changing it.");
    }

    @Test
    public void testGetWarriorId(){
        assertEquals(1, process.getWarriorsId(), "Process warrior id must be 1.");
    }

}