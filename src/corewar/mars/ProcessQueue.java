package corewar.mars;

import java.util.*;

/**
 * Manage a FIFO queue of active processes.
 * Processes run in circular order.
 */
public class ProcessQueue {
    private Queue<Process> fifo;

    /**
     * Create an empty process queue.
     */
    public ProcessQueue() {
        this.fifo = new LinkedList<>();
    }

    /**
     * Add a process to the end of the queue.
     * @param process The process to add
     */
    public void enqueue(Process process){
        fifo.add(process);
    }

    /**
     * Remove and returns the process at the front of the queue.
     * @return The process at the front, or null if the queue is empty
     */
    public Process dequeue(){
        return fifo.poll(); // poll over remove to return null if fifo is empty
    }

    /**
     * Return the process at the front without removing it.
     * @return The process at the front, or null if the queue is empty
     */
    public Process peek(){
        return fifo.peek(); //over .element() to return null if fifo is empty
    }

    /**
     * Check if the queue is empty.
     * @return true if the queue contains no processes, false otherwise
     */
    public boolean isEmpty() {
        return fifo.isEmpty();
    }

    /**
     * Get the number of processes in the queue.
     * @return The size of the queue
     */
    public int size() { //get how many elements are in fifo
        return fifo.size();
    }

}