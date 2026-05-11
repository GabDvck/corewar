package corewar.mars;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessQueueTest{

    private ProcessQueue fifo;

    @BeforeEach
    public void preparation(){
        fifo = new ProcessQueue();
    }

    @Test
    public void testEnqueue(){
        assertTrue(fifo.isEmpty(), "The queue must be empty before adding an item.");
        Process p1 = new Process(150, 1);
        fifo.enqueue(p1);
        assertFalse(fifo.isEmpty(), "The queue should no longer be empty after the addition.");
        assertEquals(1, fifo.size(), "The queue size must be 1.");
        assertEquals(p1, fifo.peek(), "The process at the top must be p1.");
    }

    @Test
    public void testEnqueueOrder(){
        Process p1 = new Process(100, 1);
        Process p2 = new Process(200, 2);

        fifo.enqueue(p1);
        fifo.enqueue(p2);

        assertEquals(2, fifo.size(), "The queue size must be 2.");
        assertEquals(p1, fifo.peek(), "The queue must follow the FIFO order.");
    }

    public void testDequeue() {
        Process p1 = new Process(100, 1);
        Process p2 = new Process(200, 2);

        fifo.enqueue(p1);
        fifo.enqueue(p2);

        Process removed = fifo.dequeue();

        assertEquals(p1, removed, "The process being removed must be the first one entered (p1).");
        assertEquals(1, fifo.size(), "The length of the queue must have decreased to 1.");
        assertEquals(p2, fifo.peek(), "The process at the top of the list should now be p2.");
    }

    @Test
    public void testDequeueVide() {
        Process removed = fifo.dequeue();
        assertNull(removed, "Removing an element from an empty queue should return null.");
    }
}