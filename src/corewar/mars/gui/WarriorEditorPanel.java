package corewar.mars.gui;

import corewar.util.*;
import corewar.redcode.exceptions.*;
import corewar.generation.genetic.*;
import corewar.generation.simulatedannealing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A dedicated panel for editing and generating Redcode for a single warrior.
 * This component includes a text area for manual code entry and buttons 
 * to automatically generate code using AI algorithms (Genetic and Simulated Annealing).
 */
public class WarriorEditorPanel extends JPanel implements ActionListener{

    private JTextArea textArea;
    private JButton generateGenetic;
    private JButton generateSimulated;

    /**
     * Constructs the editor panel.
     * Initializes the text area, the generation buttons, and sets up the layout.
     */
    public WarriorEditorPanel(){
        super();

        this.textArea = new JTextArea(5, 20);
        this.generateGenetic = new JButton("Generate with genetic algo");
        this.generateSimulated = new JButton("Generate with simulated annealing algo");

        JPanel bPanel = new JPanel();
        bPanel.setLayout(new GridLayout(2, 1));
        bPanel.add(this.generateGenetic);
        bPanel.add(this.generateSimulated);

        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
        this.add(bPanel, BorderLayout.EAST);

        this.generateGenetic.addActionListener(this);
        this.generateSimulated.addActionListener(this);
    }

    /**
     * Retrieves the current Redcode instructions from the text area.
     * @return The Redcode string written in the editor.
     */
    public String getCode(){
        return this.textArea.getText();
    }

    /**
     * Replaces the content of the text area with the specified Redcode.
     * @param code The new Redcode string to display.
     */
    public void setCode(String code){
        this.textArea.setText(code);
    }

    /**
     * Gets the Genetic Algorithm generation button.
     * @return The JButton for genetic generation.
     */
    public JButton getGeneticButton(){
        return this.generateGenetic;
    }

    /**
     * Gets the Simulated Annealing generation button.
     * @return The JButton for simulated annealing generation.
     */
    public JButton getSimulatedButton(){
        return this.generateSimulated;
    }

    /**
     * Handles the actions triggered by the generation buttons.
     * Executes the appropriate AI algorithm and updates the text area with the winning code.
     * @param e The ActionEvent containing the source of the click.
     */
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if (source == this.generateGenetic){
            GeneticAlgo genAlgo = new GeneticAlgo(Config.getGeneticPopulationSize(), Config.getGeneticCodeSize(), Config.getGeneticGenerations(), Config.getGeneticMutationRate());
            try{
                this.setCode(genAlgo.evolve().toString());
            }
            catch (InvalidInstructionException exception){
                JOptionPane.showMessageDialog(this, "Generation failed: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } 
        }
        else if (source == this.generateSimulated){
            SimulatedAnnealing simAlgo = new SimulatedAnnealing(10);
            this.setCode(simAlgo.run().toString());
        }
    }

}