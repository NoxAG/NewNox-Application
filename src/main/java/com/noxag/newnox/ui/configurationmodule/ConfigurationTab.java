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

        createCheckboxAndSetAlgorithms(AlgorithmUINames);
        createScrollPane();

        this.setContent(scrollPane);
    }

    public void createCheckboxAndSetAlgorithms(List<String> AlgorithmUINames) {
        checkboxPane = new VBox();

        AlgorithmUINames.stream().forEach(element -> {
            CheckBox checkbox = new CheckBox(element);
            checkbox.prefHeightProperty()
                    .bind(checkboxPane.heightProperty().multiply(1 / (1 + AlgorithmUINames.size())));
            VBox.setMargin(checkbox, new Insets(2.0, 2.0, 2.0, 2.0));
            checkboxPane.getChildren().add(checkbox);
        });
    }

    private void createScrollPane() {
        scrollPane = new ScrollPane(checkboxPane);
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
