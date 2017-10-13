package com.noxag.newnox.ui.statisticmodule;

import java.util.Random;

import com.noxag.newnox.ui.NewNoxWindow;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StatisticPane extends VBox {
    private GridPane chartPane;
    private BorderPane pagerPane;
    int column = 1, row = 1, maxrow = 1, maxcolumn = 1;
    int counter = 1, size = 1;
    Button incrementButton, decrementButton;

    public StatisticPane() {
        chartPane = new GridPane();
        initPagerPane();

        // Only for Testing
        buildCharts();

        this.getChildren().addAll(chartPane, pagerPane);
    }

    // initialize PagerPane with Buttons and Pager
    private void initPagerPane() {
        pagerPane = new BorderPane();

        initIncrButton();
        initDecrButton();
        pagerPane.setLeft(decrementButton);
        pagerPane.setRight(incrementButton);
        // TODO: Draw Pager
        pagerPane.setCenter(new Label("Pager"));
    }

    // initialize Decrement Button
    private void initDecrButton() {
        decrementButton = new Button("-");
        decrementButton.setOnAction((event) -> {
            counter--;
            buildCharts();
            checkDecrButton();
            checkIncrButton();
        });
        addListenerForSizeChanging(NewNoxWindow.getScene().widthProperty());
        addListenerForSizeChanging(NewNoxWindow.getScene().heightProperty());
    }

    // initialize Increment Button
    private void initIncrButton() {
        incrementButton = new Button("+");
        incrementButton.setOnAction((event) -> {
            counter++;
            buildCharts();
            checkIncrButton();
        });
    }

    // check if Increment Button should be activated
    public void checkIncrButton() {
        // Only for testing
        System.out.println((size + 1) * 150 + ">=?" + this.getWidth());
        // TODO: Fixen
        if ((150 * (size + 1)) >= this.getWidth() || (50 * (size + 1)) >= this.getHeight()) {
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

    // add sizechanging-Listeners for checking Increment Button activation state

    private void addListenerForSizeChanging(ReadOnlyDoubleProperty property) {
        property.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                checkIncrButton();
            }
        });
    }

    // ONLY-FOR-TESTING-SECTION (build random Charts)

    private void buildCharts() {
        size = counter;
        row = column = 1;
        chartPane.getChildren().clear();
        while (column <= size) {
            chartPane.add(createSampleChart(), row, column);
            row++;
            if (row > size) {
                row = 1;
                column++;
            }
        }
    }

    public static BarChart createSampleChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Chart");
        bc.setMinWidth(150);
        bc.setMinHeight(50);
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
