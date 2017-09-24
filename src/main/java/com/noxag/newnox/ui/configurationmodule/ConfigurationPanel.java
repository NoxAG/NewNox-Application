package com.noxag.newnox.ui.configurationmodule;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ConfigurationPanel extends JPanel {

    private static final long serialVersionUID = 2096561493122050737L;
    private AlgorithmChooserPanel algorithmChooserPanel;
    private FileChooserPanel fileChooserPanel;
    private JButton analyzeButton;

    private JPanel northSidePanel;

    public ConfigurationPanel() {
        this(new ArrayList<String>());
    }

    public ConfigurationPanel(List<String> textanayzerUINames) {
        initializePanelComponents(textanayzerUINames);
    }

    private void initializePanelComponents(List<String> textanayzerUINames) {
        this.setLayout(new GridBagLayout());

        algorithmChooserPanel = new AlgorithmChooserPanel(textanayzerUINames);
        fileChooserPanel = new FileChooserPanel();
        analyzeButton = new JButton("Analyze");
        northSidePanel = new JPanel();

        initalizeNorthSidePanel();
        addComponentColors();

        this.add(northSidePanel, createCustomGridBagConstraint(0, 0, 1, 0.8, GridBagConstraints.BOTH));
        this.add(fileChooserPanel, createCustomGridBagConstraint(0, 1, 1, 0.2, GridBagConstraints.BOTH));
    }

    private void initalizeNorthSidePanel() {
        northSidePanel.setLayout(new GridBagLayout());

        northSidePanel.add(algorithmChooserPanel, createCustomGridBagConstraint(0, 0, 0.9, 1, GridBagConstraints.BOTH));
        northSidePanel.add(analyzeButton, createCustomGridBagConstraint(1, 0, 0.1, 1, GridBagConstraints.BOTH));
    }

    private void addComponentColors() {
        algorithmChooserPanel.setBackground(Color.LIGHT_GRAY);
        northSidePanel.setBackground(Color.DARK_GRAY);
        fileChooserPanel.setBackground(Color.GRAY);
    }

    private GridBagConstraints createCustomGridBagConstraint(int x, int y, double widthx, double heighty, int fill) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = widthx;
        constraints.weighty = heighty;
        constraints.fill = fill;
        return constraints;
    }

}
