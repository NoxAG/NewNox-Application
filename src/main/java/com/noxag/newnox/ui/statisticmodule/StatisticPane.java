package com.noxag.newnox.ui.statisticmodule;

import java.util.Random;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class StatisticPane extends BorderPane {
    Button incrementButton, decrementButton;
    Pagination pager;

    private BarChart[] chartArray;

    int counter = 1;

    public StatisticPane(BarChart[] barChart) {
        chartArray = barChart;

        initIncrButton();
        initDecrButton();
        initPager();

        this.setLeft(decrementButton);
        this.setRight(incrementButton);
        this.setCenter(pager);
    }

    // Only for testing purpose
    public StatisticPane() {
        this(createCharts(15));
    }

    private void initIncrButton() {
        incrementButton = new Button("+");
        incrementButton.setOnAction((event) -> {
            counter *= 2;
            reloadPage();
        });
    }

    private void initDecrButton() {
        decrementButton = new Button("-");
        decrementButton.setOnAction((event) -> {
            counter /= 2;
            reloadPage();
        });
    }

    // check if Increment Button should be activated
    public void checkIncrButton() {
        if (counter >= 3) {
            incrementButton.setDisable(true);
        } else {
            incrementButton.setDisable(false);
        }
    }

    // check if Decrement Button should be activated
    public void checkDecrButton() {
        if (counter <= 1) {
            decrementButton.setDisable(true);
        } else {
            decrementButton.setDisable(false);
        }
    }

    private void initPager() {
        pager = new Pagination();
        apendPagesToPager();
        reloadPage();
    }

    // Sets a callback Function for appending a page to every 'tab' of the pager
    // --> callback is called always if number of pages has been changed
    private void apendPagesToPager() {
        pager.setPageFactory(new Callback<Integer, Node>() {
            public Node call(Integer pageIndex) {
                GridPane page = new GridPane();
                buildCharts(page, pageIndex);
                return page;
            }
        });
    }

    // Page reload actions
    private void reloadPage() {
        setPager();
        checkIncrButton();
        checkDecrButton();
    }

    // Calculate pages to be shown
    private void setPager() {
        if (chartArray != null) {
            pager.setPageCount((int) Math.ceil((float) chartArray.length / (float) counter));
        } else {
            pager.setPageCount(1);
        }
    }

    // Adding charts to given page
    private void buildCharts(GridPane page, int index) {
        page.getChildren().clear();
        for (int i = 0; i < counter; i++) {
            if (i + index * counter < chartArray.length)
                page.add(chartArray[i + index * counter], (int) Math.ceil((i + 1) / 2.0), 2 - ((i + 1) % 2));
        }
    }

    // Following section is only for testing purposes
    public static BarChart[] createCharts(int num) {
        BarChart[] sampleChart = new BarChart[num];
        for (int i = 0; i < num; i++) {
            sampleChart[i] = createSampleChart(i);
        }
        return sampleChart;
    }

    public static BarChart createSampleChart(int index) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Chart " + (index + 1));
        bc.setMinWidth(150);
        bc.setMinHeight(50);
        bc.setPrefWidth(800);
        bc.setPrefHeight(600);
        xAxis.setLabel("Name");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Dataline I");
        series1.getData().add(new XYChart.Data("Eins", generateRndDouble()));
        series1.getData().add(new XYChart.Data("Zwei", generateRndDouble()));
        series1.getData().add(new XYChart.Data("Drei", generateRndDouble()));
        series1.getData().add(new XYChart.Data("Vier", generateRndDouble()));
        series1.getData().add(new XYChart.Data("Fünf", generateRndDouble()));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Dataline II");
        series2.getData().add(new XYChart.Data("Eins", generateRndDouble()));
        series2.getData().add(new XYChart.Data("Zwei", generateRndDouble()));
        series2.getData().add(new XYChart.Data("Drei", generateRndDouble()));
        series2.getData().add(new XYChart.Data("Vier", generateRndDouble()));
        series2.getData().add(new XYChart.Data("Fünf", generateRndDouble()));

        bc.getData().addAll(series1, series2);

        return bc;
    }

    public static double generateRndDouble() {
        Random r = new Random();
        return 1 + 1000 * r.nextDouble();
    }
}
