package com.noxag.newnox.ui;

import java.io.Serializable;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NewNoxWindow extends Application implements Serializable {
    private static final long serialVersionUID = 668695870448644732L;

    @Override
    public void start(Stage s) throws Exception {
        // TODO Auto-generated method stub
        SplitPane parent = new SplitPane();
        VBox left = new VBox();
        left.setSpacing(10);

        BorderPane configPane = addConfigPane();
        configPane.prefHeightProperty().bind(left.heightProperty().multiply(0.3));
        configPane.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox statisticPane = addStatisticPane();
        statisticPane.prefHeightProperty().bind(left.heightProperty().multiply(0.7).add(-20));
        statisticPane.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox PDFPane = addPDFPane();

        left.getChildren().addAll(configPane, statisticPane);
        parent.getItems().addAll(left, PDFPane);

        Scene scene = new Scene(parent);
        s.setWidth(800);
        s.setHeight(600);
        s.setScene(scene);
        s.setTitle("NewNoxAG - PA-Analyzer");

        s.show();
    }

    private VBox addPDFPane() {
        VBox PDFPane = new VBox();
        PDFPane.getChildren().add(new Label("PDF"));
        return PDFPane;
    }

    private VBox addStatisticPane() {
        VBox statisticPane = new VBox();
        statisticPane.getChildren().add(new Label("Statistiken"));
        return statisticPane;
    }

    private BorderPane addConfigPane() {
        BorderPane configPane = new BorderPane();

        TabPane tabbar = addTabPaneToConfigPane();
        configPane.setCenter(tabbar);

        Button run = new Button("Run");
        run.prefWidthProperty().bind(configPane.widthProperty().multiply(0.3));
        run.prefHeightProperty().bind(configPane.heightProperty().multiply(1.0));
        configPane.setRight(run);

        return configPane;
    }

    private TabPane addTabPaneToConfigPane() {
        TabPane tabbar = new TabPane();
        Tab textTab = new Tab();
        textTab.setText("Textanalysen");
        Tab statTab = new Tab();
        statTab.setText("Statistikanalysen");

        VBox textAlgPane = createTextAlgPane();
        textTab.setContent(textAlgPane);
        VBox statAlgPane = createStatAlgPane();
        statTab.setContent(statAlgPane);

        tabbar.getTabs().add(textTab);
        tabbar.getTabs().add(statTab);

        tabbar.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        return tabbar;
    }

    private VBox createTextAlgPane() {
        VBox textAlgPane = new VBox();
        textAlgPane.getChildren().add(new Label("Textalgorithmen"));
        return textAlgPane;
    }

    private VBox createStatAlgPane() {
        VBox textStatPane = new VBox();
        textStatPane.getChildren().add(new Label("Statistikalgorithmen"));
        return textStatPane;
    }

    /*
     * 
     * 
     * private ConfigurationPanel configPanel; private PDFPanel pdfPanel;
     * private StatisticPanel statisticPanel;
     * 
     * public void start(Stage s) throws Exception { VBox parent = new VBox();
     * Label test = new Label("TEST"); parent.getChildren().add(test); Scene
     * scene = new Scene(parent); s.setScene(scene); s.show(); }
     */
}
