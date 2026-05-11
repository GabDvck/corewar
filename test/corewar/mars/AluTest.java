package corewar.mars;

import corewar.redcode.Instruction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AluTest {

    private Alu alu;

    @BeforeEach
    public void setUp() {
        alu = new Alu(8000);
    }

    @Test
    public void testModulo() {
        assertEquals(5, alu.modulo(5), "Positive number within range should remain unchanged");
        assertEquals(0, alu.modulo(8000), "8000 modulo 8000 should wrap around to 0");
        assertEquals(7999, alu.modulo(-1), "Negative numbers should wrap around backwards");
        assertEquals(5, alu.modulo(8005), "Numbers larger than ramSize should be reduced correctly");
    }

    @Test
    public void testIncrementePc() {
        assertEquals(1, alu.incrementePc(0), "Incrementing 0 should give 1");
        assertEquals(0, alu.incrementePc(7999), "Incrementing the last memory address (7999) should wrap around to 0");
    }

    @Test
    public void testGoTo() {
        assertEquals(10, alu.goTo(5, 5), "5 + 5 should be 10");
        assertEquals(7995, alu.goTo(5, -10), "5 - 10 should wrap around backwards to 7995");
        assertEquals(5, alu.goTo(7995, 10), "7995 + 10 should wrap around forwards to 5");
    }

    @Test
    public void testAdd() {
        Instruction source = new Instruction(Instruction.OpCode.ADD, 10, 20);
        Instruction target = new Instruction(Instruction.OpCode.DAT, 30, 40);

        alu.add(source, target, true);
        
        assertEquals(30, target.getVal1(), "Target val1 should remain unchanged in immediate mode");
        assertEquals(50, target.getVal2(), "Target val2 should be incremented by source val1 (40 + 10)");

        target.setVal2(40);

        alu.add(source, target, false);
        
        assertEquals(40, target.getVal1(), "Target val1 should be incremented by source val1 (30 + 10)");
        assertEquals(60, target.getVal2(), "Target val2 should be incremented by source val2 (40 + 20)");
    }

    @Test
    public void testSub() {
        Instruction source = new Instruction(Instruction.OpCode.SUB, 10, 20);
        Instruction target = new Instruction(Instruction.OpCode.DAT, 30, 40);

        alu.sub(source, target, true);
        assertEquals(30, target.getVal1(), "Target val1 should remain unchanged");
        assertEquals(30, target.getVal2(), "Target val2 should be decremented by source val1 (40 - 10)");

        target.setVal2(40);

        alu.sub(source, target, false);
        assertEquals(20, target.getVal1(), "Target val1 should be decremented by source val1 (30 - 10)");
        assertEquals(20, target.getVal2(), "Target val2 should be decremented by source val2 (40 - 20)");
    }

    @Test
    public void testDjn() {
        Instruction target1 = new Instruction(Instruction.OpCode.DAT, 0, 5);
        boolean result1 = alu.djn(target1);
        assertEquals(4, target1.getVal2(), "Target val2 should be decremented from 5 to 4");
        assertTrue(result1, "Should return true because the new value (4) is not zero");

        Instruction target2 = new Instruction(Instruction.OpCode.DAT, 0, 1);
        boolean result2 = alu.djn(target2);
        assertEquals(0, target2.getVal2(), "Target val2 should be decremented from 1 to 0");
        assertFalse(result2, "Should return false because the new value is exactly zero");
    }

    @Test
    public void testCmp() {
        Instruction source = new Instruction(Instruction.OpCode.CMP, 10, 20);
        Instruction target1 = new Instruction(Instruction.OpCode.CMP, 10, 20);
        Instruction target2 = new Instruction(Instruction.OpCode.DAT, 15, 10);

        assertTrue(alu.cmp(source, target2, true), "Should be true because source val1 (10) == target2 val2 (10)");
        
        assertTrue(alu.cmp(source, target1, false), "Should be true because all fields match exactly between source and target1");
        assertFalse(alu.cmp(source, target2, false), "Should be false because fields do not match");
    }

    @Test
    public void testSlt() {
        Instruction source = new Instruction(Instruction.OpCode.SLT, 10, 15);
        Instruction target = new Instruction(Instruction.OpCode.DAT, 20, 20);

        assertTrue(alu.slt(source, target, true), "Should be true because source val1 (10) < target val2 (20)");

        assertTrue(alu.slt(source, target, false), "Should be true because source val2 (15) < target val2 (20)");
        
        Instruction source2 = new Instruction(Instruction.OpCode.SLT, 30, 30);
        assertFalse(alu.slt(source2, target, false), "Should be false because source val2 (30) is NOT strictly less than target val2 (20)");
    }
}