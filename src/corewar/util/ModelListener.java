package corewar.util;

/**
 * The listener (observer) interface for receiving model update events.
 * Used for the view (MVC pattern)
 */
public interface ModelListener{

    /*
     * Used when model state changed to update the GUI.
     * @param source The object (the model) which is modified
    */
    public void modelUpdated(Object source);

} 