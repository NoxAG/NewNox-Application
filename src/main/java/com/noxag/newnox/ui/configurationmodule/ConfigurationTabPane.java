package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;

import javafx.scene.control.TabPane;

public class ConfigurationTabPane extends TabPane {

    private ConfigurationTab textAlgorithmsTab;
    private ConfigurationTab statisticAlgorithmsTab;

    public ConfigurationTabPane() {

        textAlgorithmsTab = createAlgorithmTab("Textanalysen");
        statisticAlgorithmsTab = createAlgorithmTab("Statistikanalysen");

        addTabs();
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

    public ConfigurationTab getTextAlgorithmsTab() {
        return textAlgorithmsTab;
    }

    public void setTextAlgorithmsTab(ConfigurationTab textAlgorithmsTab) {
        this.textAlgorithmsTab = textAlgorithmsTab;
    }

    public ConfigurationTab getStatisticAlgorithmsTab() {
        return statisticAlgorithmsTab;
    }

    public void setStatisticAlgorithmsTab(ConfigurationTab statisticAlgorithmsTab) {
        this.statisticAlgorithmsTab = statisticAlgorithmsTab;
    }
}
