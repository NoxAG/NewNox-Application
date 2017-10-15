package com.noxag.newnox.ui;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Consumer;

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
    private Consumer<String> openPDFBtnCallBack;
    private Consumer<List<String>> analyzeBtnCallBack;
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

        statisticPane = new StatisticPane();
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

    public void registerAnalyzeEvent(Consumer<List<String>> analyzeCallBack) {
        this.analyzeBtnCallBack = analyzeCallBack;
    }

    public void registerOpenPDFEvent(Consumer<String> openPDFCallBack) {
        this.openPDFBtnCallBack = openPDFCallBack;
    }

    // TODO call this method when analyzer button has been pressed
    public void triggerAnalyzeEvent(List<String> algorithms) {
        this.analyzeBtnCallBack.accept(algorithms);
    }

    // TODO call this method when open button has been pressed
    public void triggerOpenPDFEvent(String path) {
        this.openPDFBtnCallBack.accept(path);
    }

    public void setTextanalyzerAlgorithms(List<String> textanalyzerUINames) {
        // TODO configPane.setTextanalyzerAlgorithms(textanalyzerUINames);

    }

    public void setStatisticanalyzerAlgorithms(List<String> statisticanalyzerUINames) {
        // TODO
        // configPane.setStatisticanalyzerAlgorithms(statisticanalyzerUINames);
    }

    public void updatePDFImages(List<BufferedImage> pdfImages) {
        // TODO: pdfPane.setPDFImages(pdfImages)
    }

    public void updateTextMarkupImages(List<BufferedImage> textMarkupImages) {
        // TODO: pdfPane.setTextMarkupImages(textMarkupImages)
    }

    public void updateStatisticView(List<BarChart> charts) {
        // TODO: statisticPane.setCharts(charts)
    }

    public void popupAlert(String alertmessage) {
        // TODO: create alert window with alertmessage
    }

}
