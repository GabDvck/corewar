package corewar.redcode;

import corewar.redcode.exceptions.InvalidInstructionException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class InstructionFactoryTest {

    private InstructionFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new InstructionFactory();
    }

    @Test
    public void testStringToInstruction() {
        assertDoesNotThrow(() -> {
            Instruction i1 = factory.stringToInstruction("DAT 0 0");
            assertNotNull(i1, "The instruction should not be null");

            Instruction i2 = factory.stringToInstruction("MOV # 1 @ 2");
            assertNotNull(i2, "The instruction with complex address modes should be parsed");
        }, "Valid Redcode instructions should not throw any exceptions.");

        String badCode = "TOTO 4 5";

        assertThrows(InvalidInstructionException.class, () -> {
            factory.stringToInstruction(badCode);
        }, "Parsing a fake OpCode should throw an InvalidInstructionException.");
    }

    @Test
    public void testCreateWarriorFromString() {
        String code = "ADD 4 5\nMOV 0 1\nDAT 0 0";

        assertDoesNotThrow(() -> {
            Warrior warrior = factory.createWarriorFromString(code);
            assertNotNull(warrior, "The Warrior object should be created successfully.");
        }, "Creating a warrior from a valid multi-line string should not throw exceptions.");
    }
}