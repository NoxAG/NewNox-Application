package com.noxag.newnox.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.ui.configurationmodule.ConfigurationPane;
import com.noxag.newnox.ui.pdfmodule.PDFPane;
import com.noxag.newnox.ui.statisticmodule.StatisticPane;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This class builds the User Interface
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */

public class NewNoxWindow extends Application {
    private static final double PDFPANE_MINWIDTH_FACTOR = 0.4;
    private static final double PDFPANE_MAXWIDTH_FACTOR = 0.7;
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
    private Consumer<File> openPDFBtnCallBack;
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
        pdfPane.minWidthProperty().bind(main.widthProperty().multiply(PDFPANE_MINWIDTH_FACTOR));
        pdfPane.maxWidthProperty().bind(main.widthProperty().multiply(PDFPANE_MAXWIDTH_FACTOR));
    }

    private void initLeftSide() {
        left = new VBox();
        left.setSpacing(10);
        left.minWidthProperty().bind(main.widthProperty().multiply(LEFT_WIDTH_FACTOR));

        configPane = createConfigPane();
        // new
        createActionEventsForConfigPane(configPane);
        statisticPane = createStatisticPane();

        left.getChildren().addAll(configPane, statisticPane);
    }

    private void createActionEventsForConfigPane(ConfigurationPane configPane) {
        Button btnOpen = configPane.getOpenButton();
        Button btnRun = configPane.getRunButton();
        FileChooser fileChooser = configPane.getFileChooser();
        createActionEventForRunButton(configPane, btnRun);
        createActionEventForOpenButton(btnOpen, btnRun, fileChooser);
    }

    private void createActionEventForRunButton(ConfigurationPane configPane, Button btnRun) {
        btnRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                triggerAnalyzeEvent(configPane.getSelectedAnalyzers());
            }
        });
    }

    private void createActionEventForOpenButton(Button btnOpen, Button btnRun, FileChooser fileChooser) {
        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                Node source = (Node) e.getSource();
                Window stage = source.getScene().getWindow();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    btnRun.setDisable(false);
                }
                // new
                triggerOpenPDFEvent(file);
            }
        });
    }

    private ConfigurationPane createConfigPane() {
        ConfigurationPane configPane = new ConfigurationPane();
        configPane.prefHeightProperty().bind(left.heightProperty().multiply(CONFIGPANE_HEIGHT_FACTOR));
        configPane.prefWidthProperty().bind(left.widthProperty().multiply(CONFIGPANE_WIDTH_FACTOR));
        return configPane;
    }

    private StatisticPane createStatisticPane() {
        StatisticPane statisticPane = new StatisticPane();
        statisticPane.prefHeightProperty()
                .bind(left.heightProperty().multiply(STATISTICPANE_HEIGHT_FACTOR).add(STATISTICPANE_HEIGHT_INCREASE));
        statisticPane.maxHeightProperty()
                .bind(left.heightProperty().multiply(STATISTICPANE_HEIGHT_FACTOR).add(STATISTICPANE_HEIGHT_INCREASE));
        statisticPane.prefWidthProperty().bind(left.widthProperty().multiply(STATISTICPANE_WIDTH_FACTOR));

        return statisticPane;
    }

    public static Scene getScene() {
        return scene;
    }

    public void registerAnalyzeEvent(Consumer<List<String>> analyzeCallBack) {
        this.analyzeBtnCallBack = analyzeCallBack;
    }

    public void registerOpenPDFEvent(Consumer<File> openPDFCallBack) {
        this.openPDFBtnCallBack = openPDFCallBack;
    }

    // TODO call this method when analyzer button has been pressed
    public void triggerAnalyzeEvent(List<String> algorithms) {
        this.analyzeBtnCallBack.accept(algorithms);
    }

    // TODO call this method when open button has been pressed
    public void triggerOpenPDFEvent(File file) {
        this.openPDFBtnCallBack.accept(file);
        this.pdfPane.setFileDescription(file.getName());

    }

    public void setTextanalyzerAlgorithms(List<String> textanalyzerUINames) {
        configPane.setTextanalyzerUInames(textanalyzerUINames);
    }

    public void setStatisticanalyzerAlgorithms(List<String> statisticanalyzerUINames) {
        configPane.setStatisticanalyzerUInames(statisticanalyzerUINames);
    }

    public void updatePDFImages(List<BufferedImage> pdfTextOverlay) {
        pdfPane.setPDFTextOverlay(pdfTextOverlay);
    }

    public void updateTextMarkupImages(List<BufferedImage> textMarkupImages) {
        pdfPane.setTextMarkupOverlay(textMarkupImages);
    }

    public void updateStatisticView(List<BarChart<String, Number>> charts, List<CommentaryFinding> comments) {
        statisticPane.setCharts(charts);
        statisticPane.setCommentFindings(comments);
    }

    public void popupAlert(String alertmessage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(alertmessage);

        alert.showAndWait();
    }
}
