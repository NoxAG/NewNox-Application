package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class ConfigurationTab extends Tab {
    public ConfigurationTab(String name) {
        this(name, new ArrayList<String>());
    }

    private CheckBox cb;
    private VBox checkboxPane;
    private ScrollPane scrollPane;
    public List<String> selectedAlgorithms;

    public ConfigurationTab(String name, List<String> AlgorithmUINames) {
        this.setText(name);
        checkboxPane = new VBox();
        setAlgorithms(AlgorithmUINames);
        scrollPane = new ScrollPane(checkboxPane);
        scrollPane.setFitToHeight(true);
        this.setContent(scrollPane);
    }

    public VBox setAlgorithms(List<String> AlgorithmUINames) {

        for (String element : AlgorithmUINames) {
            cb = new CheckBox(element);
            checkboxPane.getChildren().add(cb);
            VBox.setMargin(cb, new Insets(2.0, 2.0, 2.0, 2.0));
            cb.setOnAction(e -> getTextanalyzer(e));
        }
        return checkboxPane;

    }

    public List<String> getTextanalyzer(ActionEvent e) { // Falsch!!!
        selectedAlgorithms = new ArrayList<String>();
        if (cb.isSelected() == true) {
            selectedAlgorithms.add(cb.getText());
        }
        System.out.println(selectedAlgorithms);
        return selectedAlgorithms;
    }
}
