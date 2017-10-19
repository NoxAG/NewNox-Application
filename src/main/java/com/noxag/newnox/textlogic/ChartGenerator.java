package com.noxag.newnox.textlogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * This class generates a chart by given StatisticFindings
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */
public class ChartGenerator {
    public static final int MIN_WIDTH = 150;
    public static final int PREF_WIDTH = 800;
    public static final int MIN_HEIGHT = 100;
    public static final int PREF_HEIGHT = 600;

    public static List<BarChart<String, Number>> generateBarCharts(List<StatisticFinding> statisticFindings) {
        List<BarChart<String, Number>> barCharts = new ArrayList<BarChart<String, Number>>();

        statisticFindings.stream().forEach(finding -> {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            yAxis.setTickUnit(1);

            BarChart<String, Number> bc = generateBarChart(finding.getChartName(), xAxis, yAxis);
            setLabelsToAxis(xAxis, yAxis, finding);
            XYChart.Series<String, Number> series = createSeriesForChart(finding);
            bc.getData().addAll(series);
            barCharts.add(bc);
        });
        return barCharts;
    }

    private static BarChart<String, Number> generateBarChart(String chartName, CategoryAxis xAxis, NumberAxis yAxis) {
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle(chartName);
        bc.setMinWidth(MIN_WIDTH);
        bc.setPrefWidth(PREF_WIDTH);
        bc.setMinHeight(MIN_HEIGHT);
        bc.setPrefHeight(PREF_HEIGHT);
        return bc;
    }

    private static void setLabelsToAxis(CategoryAxis xAxis, NumberAxis yAxis, StatisticFinding finding) {
        xAxis.setLabel(finding.getxAxisLabel());
        yAxis.setLabel(finding.getyAxisLabel());
    }

    private static Series<String, Number> createSeriesForChart(StatisticFinding finding) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(finding.getDataLineLabel());
        addDataToSeries(series, finding.getStatisticData());
        return series;
    }

    private static void addDataToSeries(XYChart.Series<String, Number> series,
            List<StatisticFindingData> statisticData) {
        statisticData.sort(Collections.reverseOrder(Comparator.comparing(StatisticFindingData::getValue)));
        statisticData.stream().forEach(data -> {
            series.getData().add(new XYChart.Data<String, Number>(data.getDesignation(), data.getValue()));
        });
    }

}
