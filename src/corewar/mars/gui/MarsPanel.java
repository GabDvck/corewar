package corewar.mars.gui;
import corewar.mars.*;
import corewar.util.*;

import javax.swing.*;
import java.awt.*;

/**
 * The visual representation of the Core War arena (RAM).
 * This panel acts as a View in the MVC pattern, observing the Mars model
 * and repainting the memory grid whenever an instruction cycle is executed.
 */
public class MarsPanel extends JPanel implements ModelListener{

    private Mars mars;
    private int cols;

    /**
     * Constructs the Mars display panel.
     * Subscribes to the given Mars model to receive state updates,
     * and initializes the preferred dimensions and background color.
     * @param mars The Mars model to observe and display.
     */
    public MarsPanel(Mars mars){
        super();
        this.mars = mars;
        this.cols = 100;
        mars.subscribeListener(this);
        setPreferredSize(new Dimension(600, 500));
        this.setBackground(Color.BLACK);
    }

    /**
     * Renders the memory array as a 2D grid of colored cells.
     * This method is automatically called by the Swing framework whenever 
     * repaint() is invoked.
     * @param g The Graphics context used for drawing.
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Ram ram = this.mars.getRam();
        int ramSize = ram.getSize();
        int lines = ramSize / this.cols;
        int cellWidth = this.getWidth() / this.cols; 
        int cellHeight = this.getHeight() / lines;
        for (int i = 0; i < ramSize; i++){
            int x = (i % this.cols) * cellWidth;
            int y = (i / this.cols) * cellHeight;
            g.setColor(chooseColor(i));
            g.fillRect(x, y, cellWidth, cellHeight);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellWidth, cellHeight);
        }
    }

    /**
     * Triggered when the observed Mars model fires a state change event.
     * Forces the panel to schedule a redraw of the memory grid.
     * @param source The model that triggered the update (usually the Mars instance).
     */
    @Override
    public void modelUpdated(Object source){
        this.repaint();
    }

    /**
     * Determines the color of a specific memory cell based on its owner.
     * Blue for Warrior 1, Red for Warrior 2, and Dark Gray for empty/neutral cells.
     * @param i The memory address to check.
     * @return The Color corresponding to the cell's owner.
     */
    private Color chooseColor(int i){
        int owner = this.mars.getRam().getOwner(i);
        if (owner == 1){
            return Color.BLUE;
        }
        else if (owner == 2){
            return Color.RED;
        }
        else{
            return Color.DARK_GRAY;
        }
    }

}