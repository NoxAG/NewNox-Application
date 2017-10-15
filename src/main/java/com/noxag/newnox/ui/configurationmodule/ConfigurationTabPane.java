package com.noxag.newnox.ui.configurationmodule;

import javafx.scene.control.TabPane;

public class ConfigurationTabPane extends TabPane {

    private ConfigurationTab textAlgorithmsTab;
    private ConfigurationTab statisticAlgorithmsTab;
    // TODO: 2 Testlisten anlegen (textanalysen & statistikanalysen) und
    // entsprechend bei "new ConfigurationTab("...") als Parameter �bergeben
    // private ArrayList<String> names = new ArrayList<String>();

    public ConfigurationTabPane() {
        /*
         * names.add("Akademische Aufrichtigkeitserklärung");
         * names.add("Bibliography Analyzer");
         * names.add("Coomon Abbreviation Analyzer");
         * names.add("Font Analyzer"); names.add("Line Spacing Analyzer");
         */
        textAlgorithmsTab = new ConfigurationTab("Textanalysen"); // "Textanalysen"
        statisticAlgorithmsTab = new ConfigurationTab("Statistikanalysen"); // "Statistikanalysen"
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
