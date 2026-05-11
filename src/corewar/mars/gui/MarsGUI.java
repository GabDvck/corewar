package corewar.mars.gui;

import corewar.mars.*;
import corewar.util.*;

import javax.swing.*;
import java.awt.*;

/**
 * The main GUI window for the Core War.
 * It assembles the MarsPanel et the SecondPanel.
 */
public class MarsGUI extends JFrame{

    private Mars mars;
    private MarsPanel marsPanel;
    private SecondPanel secondPanel;

    /**
     * Constructs the main application window.
     */
    public MarsGUI(){
        super("CoreWar");
        this.mars = new Mars(Config.getRamSize());
        this.marsPanel = new MarsPanel(mars);
        this.secondPanel = new SecondPanel(mars);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(marsPanel, BorderLayout.CENTER);
        this.getContentPane().add(secondPanel, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}