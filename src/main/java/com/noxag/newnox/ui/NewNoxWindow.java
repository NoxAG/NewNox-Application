package com.noxag.newnox.ui;

import java.util.ArrayList;
import java.util.List;

import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textlogic.ChartGenerator;
import com.noxag.newnox.ui.configurationmodule.ConfigurationPane;
import com.noxag.newnox.ui.pdfmodule.PDFPane;
import com.noxag.newnox.ui.statisticmodule.StatisticPane;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewNoxWindow extends Application {
    private static final double PDFPANE_WIDTH_FACTOR = 0.4;
    private static final double LEFT_WIDTH_FACTOR = 0.3;
    private static final double CONFIGPANE_WIDTH_FACTOR = 0.9;
    private static final double CONFIGPANE_HEIGHT_FACTOR = 0.3;
    private static final double STATISTICPANE_WIDTH_FACTOR = 0.9;
    private static final double STATISTICPANE_HEIGHT_FACTOR = 0.7;
    private static final int STATISTICPANE_HEIGHT_INCREASE = -20;
    private StatisticPane statisticPane;
    private ConfigurationPane configPane;
    private PDFPane pdfPane;
    private VBox left;
    private SplitPane main;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        main = new SplitPane();
        initStage(stage);
        initLeftSide();
        initRightSide();

        main.getItems().addAll(left, pdfPane);
        stage.show();
    }

    private void initStage(Stage stage) {
        scene = new Scene(main);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.setTitle("NewNoxAG - PA-Analyzer");
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
                    Number newSceneWidth) {
            }
        });
    }

    private void initRightSide() {
        pdfPane = new PDFPane();
        pdfPane.minWidthProperty().bind(main.widthProperty().multiply(PDFPANE_WIDTH_FACTOR));
    }

    private void initLeftSide() {
        left = new VBox();
        left.setSpacing(10);
        left.minWidthProperty().bind(main.widthProperty().multiply(LEFT_WIDTH_FACTOR));

        configPane = new ConfigurationPane();
        configPane.prefHeightProperty().bind(left.heightProperty().multiply(CONFIGPANE_HEIGHT_FACTOR));
        configPane.prefWidthProperty().bind(left.widthProperty().multiply(CONFIGPANE_WIDTH_FACTOR));

        List<BarChart> testCharts = ChartGenerator.generateBarCharts(createTestFindings());

        statisticPane = new StatisticPane(testCharts);
        statisticPane.prefHeightProperty()
                .bind(left.heightProperty().multiply(STATISTICPANE_HEIGHT_FACTOR).add(STATISTICPANE_HEIGHT_INCREASE));
        statisticPane.maxHeightProperty()
                .bind(left.heightProperty().multiply(STATISTICPANE_HEIGHT_FACTOR).add(STATISTICPANE_HEIGHT_INCREASE));
        statisticPane.prefWidthProperty().bind(left.widthProperty().multiply(STATISTICPANE_WIDTH_FACTOR));

        left.getChildren().addAll(configPane, statisticPane);
    }

    public static Scene getScene() {
        return scene;
    }

    // Only for testing purpose
    private List<StatisticFinding> createTestFindings() {
        List<StatisticFinding> findingList = new ArrayList<StatisticFinding>();
        for (int i = 0; i < 10; i++) {
            StatisticFinding finding = new StatisticFinding(StatisticFindingType.COMMON_ABBREVIATION);
            for (int k = 0; k < 10; k++) {
                StatisticFindingData data = new StatisticFindingData("Test" + k, k);
                finding.addStatisticData(data);
            }
            finding.setChartName("Test");

            findingList.add(finding);
        }

        return findingList;
    }
}
