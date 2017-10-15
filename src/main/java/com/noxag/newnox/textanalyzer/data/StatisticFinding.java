package com.noxag.newnox.textanalyzer.data;

/**
 * This class represents the result of a statistical analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class StatisticFinding extends Finding {
    private static final int MIN_WIDTH = 150;
    private static final int MIN_HEIGHT = 50;
    private static final int PREF_WIDTH = 800;
    private static final int ÜREF_HEIGHT = 600;

    private StatisticFindingType type;
    private StatisticFindingData[] data;
    private String chartName, xAxisLabel, yAxisLabel, dataLineLabel;

    public enum StatisticFindingType {
        VOCABULARY_DISTRIBUTION, PUNCTUATION_DISTRIBUTION, COMMON_ABBREVIATION, COMMON_FOREIGN_WORD, POOR_WORDING, SENTENCE_COMPLEXITY;
    }

    public StatisticFinding() {

    }
    /*
     * final CategoryAxis xAxis = new CategoryAxis(); final NumberAxis yAxis =
     * new NumberAxis(); BarChart bc = new BarChart<String, Number>(xAxis,
     * yAxis); bc.setTitle("Chart " + (index + 1)); bc.setMinWidth(150);
     * bc.setMinHeight(50); bc.setPrefWidth(800); bc.setPrefHeight(600);
     * xAxis.setLabel("Name"); yAxis.setLabel("Value");
     * 
     * XYChart.Series series1 = new XYChart.Series();
     * series1.setName("Dataline I"); series1.getData().add(new
     * XYChart.Data("Eins", generateRndDouble())); series1.getData().add(new
     * XYChart.Data("Zwei", generateRndDouble())); series1.getData().add(new
     * XYChart.Data("Drei", generateRndDouble())); series1.getData().add(new
     * XYChart.Data("Vier", generateRndDouble())); series1.getData().add(new
     * XYChart.Data("Fünf", generateRndDouble()));
     * 
     * XYChart.Series series2 = new XYChart.Series();
     * series2.setName("Dataline II"); series2.getData().add(new
     * XYChart.Data("Eins", generateRndDouble())); series2.getData().add(new
     * XYChart.Data("Zwei", generateRndDouble())); series2.getData().add(new
     * XYChart.Data("Drei", generateRndDouble())); series2.getData().add(new
     * XYChart.Data("Vier", generateRndDouble())); series2.getData().add(new
     * XYChart.Data("Fünf", generateRndDouble()));
     * 
     * bc.getData().addAll(series1, series2);
     * 
     * return bc;
     */

}
