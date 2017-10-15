package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class ConfigurationTab extends Tab {
    public ConfigurationTab(String name) {
        this(name, new ArrayList<String>());
    }

    public ConfigurationTab(String name, List<String> AlgorithmUINames) {
        this.setText(name);
        FlowPane checkboxPane = new FlowPane();
        // TODO: F�r jeden String aus AlogrithmUINames Liste eine Chechkbox
        // hinzuf�gen
        this.setContent(checkboxPane);
    }

    public void addAlgorithms(List<String> AlgorithmUINames) {
        HBox hbox = new HBox();
        this.setContent(hbox);

        /*
         * CheckBox hb = new CheckBox(); hb.setText("First");
         * hb.setSelected(false); hbox.getChildren().add(hb); //
         * AlgorithmUINames.get(i)
         */

        // TODO: add checkboxes for algorithms
        /*
         * for (int i = 0; i < AlgorithmUINames.size(); i++) {
         * hbox.getChildren().add(new CheckBox(AlgorithmUINames.get(i))); }
         */

    }
}
