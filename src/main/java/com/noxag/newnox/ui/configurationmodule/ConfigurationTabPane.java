package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TabPane;

public class ConfigurationTabPane extends TabPane {

    private ConfigurationTab textAlgorithmsTab;
    private ConfigurationTab statisticAlgorithmsTab;

    public ConfigurationTabPane() {

        textAlgorithmsTab = createAlgorithmTab("Text Analyses");
        statisticAlgorithmsTab = createAlgorithmTab("Statistic Analyses");

        addTabs();
    }

    public List<String> getSelectedAnalyzers() {
        List<String> selectedAnalyzer = textAlgorithmsTab.getSelectedAnalyzers();
        selectedAnalyzer.addAll(statisticAlgorithmsTab.getSelectedAnalyzers());
        return selectedAnalyzer;
    }

    private ConfigurationTab createAlgorithmTab(String text) {
        return createAlgorithmTab(text, new ArrayList<String>());
    }

    private ConfigurationTab createAlgorithmTab(String text, ArrayList<String> algorithmList) {
        ConfigurationTab newTab = new ConfigurationTab(text, algorithmList);
        newTab.updateHeight(this.heightProperty());
        return newTab;
    }

    private void addTabs() {
        this.getTabs().add(textAlgorithmsTab);
        this.getTabs().add(statisticAlgorithmsTab);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    }

    public void setTextanalyzerUInames(List<String> AlgorithmUINames) {
        this.textAlgorithmsTab.setAlgorithms(AlgorithmUINames);
        textAlgorithmsTab.updateHeight(this.heightProperty());
    }

    public void setStatisticanalyzerUInames(List<String> AlgorithmUINames) {
        this.statisticAlgorithmsTab.setAlgorithms(AlgorithmUINames);
        textAlgorithmsTab.updateHeight(this.heightProperty());
    }
}
