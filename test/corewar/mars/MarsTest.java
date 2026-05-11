package corewar.mars;

import corewar.redcode.*;
import corewar.redcode.exceptions.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MarsTest {

    private Mars mars;

    @BeforeEach
    public void setUp() {
        mars = new Mars(8000);
    }

    @Test
    public void testInitialization() {
        assertNotNull(mars.getRam());
        assertEquals(8000, mars.getRam().getSize());
    }

    @Test
    public void testStepWithoutLoading() {
        assertEquals(-1, mars.step());
    }

    @Test
    public void testGameWithStrings() {
        String code1 = "DAT 0 0";
        String code2 = "DAT 0 0";
        
        assertDoesNotThrow(() -> {
            int result = mars.game(code1, code2);
            assertTrue(result == 0 || result == 1 || result == 2);
        });
    }

    @Test
    public void testGameWithWarriors() {
        Warrior w1 = new Warrior();
        Warrior w2 = new Warrior();
        
        w1.addInstruction(new Instruction(Instruction.OpCode.DAT, 0, 0));
        w2.addInstruction(new Instruction(Instruction.OpCode.DAT, 0, 0));

        int result = mars.game(w1, w2);
        assertTrue(result == 0 || result == 1 || result == 2);
    }
}