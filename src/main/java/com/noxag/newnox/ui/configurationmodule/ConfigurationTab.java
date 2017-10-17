package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class ConfigurationTab extends Tab {
    private VBox checkboxPane;
    private ScrollPane scrollPane;

    public ConfigurationTab(String name) {
        this(name, new ArrayList<String>());
    }

    public ConfigurationTab(String name, List<String> AlgorithmUINames) {
        this.setText(name);

        createScrollPane();
        setAlgorithms(AlgorithmUINames);

        this.setContent(scrollPane);
    }

    public void setAlgorithms(List<String> AlgorithmUINames) {
        checkboxPane = new VBox();

        AlgorithmUINames.stream().forEach(element -> {
            CheckBox checkbox = new CheckBox(element);
            VBox.setMargin(checkbox, new Insets(2.0, 2.0, 2.0, 2.0));
            checkboxPane.getChildren().add(checkbox);
        });
        scrollPane.setContent(checkboxPane);
    }

    private void createScrollPane() {
        scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToHeight(true);
    }

    public List<String> getSelectedAnalyzers() {
        List<String> selectedAlgorithms = new ArrayList<String>();

        checkboxPane.getChildren().stream().filter(CheckBox.class::isInstance).collect(Collectors.toList()).stream()
                .forEach(node -> {
                    CheckBox box = (CheckBox) node;
                    if (box.isSelected()) {
                        selectedAlgorithms.add(box.getText());
                    }
                });
        return selectedAlgorithms;
    }

    public void updateHeight(ReadOnlyDoubleProperty parentProperty) {
        checkboxPane.prefHeightProperty().bind(parentProperty);
        checkboxPane.getChildren().stream().forEach(node -> {
            CheckBox box = (CheckBox) node;
            box.styleProperty().bind(Bindings.concat("-fx-font-size: ", parentProperty.multiply(0.1)));
        });
    }
}
