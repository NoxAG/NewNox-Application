package com.noxag.newnox.ui.configurationmodule;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class AlgorithmChooserPanel extends JPanel {

    private static final long serialVersionUID = -2719883287770709954L;

    public AlgorithmChooserPanel(List<String> textanayzerUINames) {
        initializePanelComponents(textanayzerUINames);
    }

    private void initializePanelComponents(List<String> textanayzerUINames) {
        this.setLayout(new GridLayout(0, 2));
        setTextanalyzerAlgorithms(textanayzerUINames);
    }

    public void setTextanalyzerAlgorithms(List<String> textanayzerUINames) {
        for (String uiName : textanayzerUINames) {
            this.add(new JCheckBox(uiName));
        }
    }
}
