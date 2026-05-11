package corewar.util;

import java.util.*;

/**
 * Abstract class for an observable model for MVC pattern.
 * Uses a list of listener and methods to subscribe, unsubscribe and prevent when the state changes
 */
public abstract class ModelListenable{

    /** The list of registered listeners waiting for updates. */
    protected List<ModelListener> listeners;

    /**
     * Constructs a new ModelListenable and initializes the empty list of listeners.
     */
    public ModelListenable(){
        this.listeners = new ArrayList<ModelListener>();
    }

    /**
     * Subscribes a new listener to this model.
     * The listener will be notified of any future changes.
     * @param l The listener to add.
     */
    public void subscribeListener(ModelListener l){
        this.listeners.add(l);
    }

    /**
     * Unsubscribes a listener from this model.
     * @param l The listener to remove.
     */
    public void unsubscribeListener(ModelListener l){
        this.listeners.remove(l);
    }

    /**
     * Notifies all registered listeners that the model's state changed.
     */
    protected void fireChange(){
        for (ModelListener l: this.listeners){
            l.modelUpdated(this);
        }
    }
}