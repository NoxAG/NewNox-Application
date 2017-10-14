package com.noxag.newnox.ui;

import com.noxag.newnox.ui.configurationmodule.ConfigurationPane;
import com.noxag.newnox.ui.pdfmodule.PDFPane;
import com.noxag.newnox.ui.statisticmodule.StatisticPane;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewNoxWindow extends Application {
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
        pdfPane.minWidthProperty().bind(main.widthProperty().multiply(0.4));
    }

    private void initLeftSide() {
        left = new VBox();
        left.setSpacing(10);
        left.minWidthProperty().bind(main.widthProperty().multiply(0.3));

        configPane = new ConfigurationPane();
        configPane.prefHeightProperty().bind(left.heightProperty().multiply(0.3));
        configPane.prefWidthProperty().bind(left.widthProperty().multiply(0.9));

        statisticPane = new StatisticPane();
        statisticPane.prefHeightProperty().bind(left.heightProperty().multiply(0.7).add(-20));
        statisticPane.prefWidthProperty().bind(left.widthProperty().multiply(0.9));

        left.getChildren().addAll(configPane, statisticPane);
    }

    public static Scene getScene() {
        return scene;
    }
}
