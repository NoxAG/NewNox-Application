package com.noxag.newnox.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.noxag.newnox.textanalyzer.TextlogicFacade;
import com.noxag.newnox.ui.configurationmodule.ConfigurationPanel;
import com.noxag.newnox.ui.pdfmodule.PDFPanel;
import com.noxag.newnox.ui.statisticmodule.StatisticPanel;

public class NewNoxWindow extends JFrame {

    private static final long serialVersionUID = 668695870448644732L;

    private TextlogicFacade textlogicFacade;

    private ConfigurationPanel configPanel;
    private PDFPanel pdfPanel;
    private StatisticPanel statisticPanel;

    private JPanel leftSidePanel;

    public NewNoxWindow() {
        this(null);
    }

    public NewNoxWindow(TextlogicFacade textlogicFacade) {
        this.textlogicFacade = textlogicFacade;

        initializeWindowAppearance();
        initializeWindowComponents();
        initializeWindowBehaviour();
    }

    private void initializeWindowAppearance() {
        // set minimal window size
        // set preferred window size
        // set window title
        // set window icon
        // force window ratio ?
        // set window background ?
        // set layout manager
        // set visible
    }

    private void initializeWindowComponents() {
        this.setLayout(new GridLayout(1, 2));

        instanziateComponentes();
        initalizeLeftSidePanel();

        addComponentColors();
        addComponentBorders();

        this.add(leftSidePanel);
        this.add(pdfPanel);

    }

    private void instanziateComponentes() {
        configPanel = new ConfigurationPanel(this.textlogicFacade.getTextanayzerUINames());
        pdfPanel = new PDFPanel();
        statisticPanel = new StatisticPanel();
        leftSidePanel = new JPanel();
    }

    private void initalizeLeftSidePanel() {
        leftSidePanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.33;
        constraints.fill = GridBagConstraints.BOTH;
        leftSidePanel.add(configPanel, constraints);

        constraints.gridy = 1;
        constraints.weighty = 0.67;
        leftSidePanel.add(statisticPanel, constraints);
    }

    private void addComponentColors() {
        pdfPanel.setBackground(Color.LIGHT_GRAY);
        leftSidePanel.setBackground(Color.DARK_GRAY);
        configPanel.setBackground(Color.ORANGE);
        statisticPanel.setBackground(Color.YELLOW);
    }

    private void addComponentBorders() {
        pdfPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftSidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        configPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statisticPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    }

    private void initializeWindowBehaviour() {
        // set default close operation
        // add window listener --> on close
    }

}
