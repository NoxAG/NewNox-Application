package com.noxag.newnox.textlogic;

import java.util.ArrayList;
import java.util.List;

import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartGenerator {
    public static final int MIN_WIDTH = 150;
    public static final int PREF_WIDTH = 800;
    public static final int MIN_HEIGHT = 100;
    public static final int PREF_HEIGHT = 600;

    public static List<BarChart> generateBarCharts(List<StatisticFinding> statisticFindings) {
        List<BarChart> barCharts = new ArrayList<BarChart>();
        for (StatisticFinding finding : statisticFindings) {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();

            BarChart bc = new BarChart<String, Number>(xAxis, yAxis);
            bc.setTitle(finding.getChartName());
            bc.setMinWidth(MIN_WIDTH);
            bc.setPrefWidth(PREF_WIDTH);
            bc.setMinHeight(MIN_HEIGHT);
            bc.setPrefHeight(PREF_HEIGHT);

            xAxis.setLabel(finding.getxAxisLabel());
            yAxis.setLabel(finding.getyAxisLabel());

            XYChart.Series series = new XYChart.Series();
            series.setName(finding.getDataLineLabel());

            for (StatisticFindingData data : finding.getStatisticData()) {
                series.getData().add(new XYChart.Data(data.getDesignation(), data.getValue()));
            }

            bc.getData().addAll(series);

            barCharts.add(bc);
        }

        return barCharts;
    }

}
