package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Tab;

public class ConfigurationTab extends Tab {
    public ConfigurationTab(String name) {
        this(name, new ArrayList<String>());
    }

    public ConfigurationTab(String name, List<String> AlgorithmUINames) {
        this.setText(name);
        // TODO:add checkboxes for algorithms
    }

    public void addAlgorithms(List<String> AlgorithmUINames) {
        // TODO: add checkboxes for algorithms
    }
}
