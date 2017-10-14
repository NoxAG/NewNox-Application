package com.noxag.newnox.ui.configurationmodule;

import javafx.scene.control.TabPane;

public class ConfigurationTabPane extends TabPane {

    private ConfigurationTab textAlgorithmsTab;
    private ConfigurationTab statisticAlgorithmsTab;

    public ConfigurationTabPane() {
        textAlgorithmsTab = new ConfigurationTab("Textanalysen");
        statisticAlgorithmsTab = new ConfigurationTab("Statistikanalysen");
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
