package com.noxag.newnox.ui.configurationmodule;

import java.util.ArrayList;

import javafx.scene.control.TabPane;

public class ConfigurationTabPane extends TabPane {

    private ConfigurationTab textAlgorithmsTab;
    private ConfigurationTab statisticAlgorithmsTab;
    // TODO: 2 Testlisten anlegen (textanalysen & statistikanalysen) und
    // entsprechend bei "new ConfigurationTab("...") als Parameter ï¿½bergeben

    public ConfigurationTabPane() {
        // Only for testing purposes
        ArrayList<String> names = createTestArrayList();

        textAlgorithmsTab = createAlgorithmTab("Textanalysen", names);
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

    // Only for testing purposes
    private ArrayList<String> createTestArrayList() {
        ArrayList<String> names = new ArrayList<String>();

        names.add("Akademische Aufrichtigkeitserklärung");
        names.add("Bibliography Analyzer");
        names.add("Coomon Abbreviation Analyzer");
        names.add("Font Analyzer");
        names.add("Line Spacing Analyzer");

        return names;
    }
}
