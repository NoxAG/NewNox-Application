package com.noxag.newnox.ui.statisticmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.noxag.newnox.textanalyzer.data.CommentaryFinding;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class StatisticPane extends BorderPane {
    private static final int MAX_CHARTS = 4;
    private static final int MIN_CHARTS = 1;
    private static final int COMMENTS_PER_PAGE = 10;

    private Button incrementButton, decrementButton;
    private Pagination pager;

    private List<BarChart> charts;
    private List<CommentaryFinding> commentFindings;
    private int chartsPerPageCounter = 1;

    // Only for testing purpose
    public StatisticPane() {
        this(createCharts(15), createComments(15));
    }

    public StatisticPane(List<BarChart> barChart) {
        this(barChart, null);
    }

    public StatisticPane(List<BarChart> barChart, List<CommentaryFinding> commentaryFindings) {
        charts = barChart;
        commentFindings = commentaryFindings;

        initIncrButton();
        initDecrButton();
        initPager();

        this.setLeft(decrementButton);
        this.setRight(incrementButton);
        this.setCenter(pager);
    }

    public void setCharts(List<BarChart> charts) {
        this.charts = charts;
    }

    private void initIncrButton() {
        incrementButton = new Button("+");
        incrementButton.setOnAction((event) -> {
            chartsPerPageCounter *= 2;
            reloadPage();
        });
    }

    private void initDecrButton() {
        decrementButton = new Button("-");
        decrementButton.setOnAction((event) -> {
            chartsPerPageCounter /= 2;
            reloadPage();
        });
    }

    // check if Increment Button should be activated
    private void checkIncrButton() {
        if (chartsPerPageCounter < MAX_CHARTS) {
            incrementButton.setDisable(false);
        } else {
            incrementButton.setDisable(true);
        }
    }

    private void checkDecrButton() {
        if (chartsPerPageCounter > MIN_CHARTS) {
            decrementButton.setDisable(false);
        } else {
            decrementButton.setDisable(true);
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
                if ((int) Math.ceil((float) charts.size() / (float) chartsPerPageCounter) > pageIndex) {
                    GridPane page = new GridPane();
                    page.setAlignment(Pos.CENTER);
                    buildCharts(page, pageIndex);
                    return page;
                } else {
                    StackPane page = new StackPane();
                    page.setAlignment(Pos.CENTER);
                    buildComments(page, pageIndex);
                    return page;
                }
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
        if (charts != null) {
            int numOfChartPages = (int) Math.ceil(charts.size() / (float) chartsPerPageCounter);
            int numOfCommentPages = (int) Math.ceil((commentFindings.size() / (float) COMMENTS_PER_PAGE));
            pager.setPageCount(numOfChartPages + numOfCommentPages);
        } else {
            pager.setPageCount(1);
        }
    }

    // Adding charts to given page
    private void buildCharts(GridPane page, int index) {
        page.getChildren().clear();
        for (int i = 0; i < chartsPerPageCounter; i++) {
            if (i + index * chartsPerPageCounter < charts.size()) {
                int column = (int) Math.ceil((i + 1) / 2.0);
                int row = 2 - ((i + 1) % 2);
                page.add(charts.get(i + index * chartsPerPageCounter), column, row);
            }
        }
    }

    public void buildComments(StackPane page, int index) {
        page.getChildren().clear();

        TableView<CommentaryFinding> commentTableView = new TableView<>();
        commentTableView.setEditable(false);
        commentTableView.prefWidthProperty().bind(this.widthProperty().multiply(0.8));
        commentTableView.prefHeightProperty().bind(this.heightProperty().multiply(0.9));
        commentTableView.maxWidthProperty().bind(this.widthProperty().multiply(0.8));
        commentTableView.maxHeightProperty().bind(this.heightProperty().multiply(0.9));

        List<CommentaryFinding> comments = new ArrayList<CommentaryFinding>();

        for (int i = 0; i < COMMENTS_PER_PAGE; i++) {
            int lastPageOfCharts = (int) Math.ceil((float) charts.size() / (float) chartsPerPageCounter);
            int commentFindingsIndex = i + (index - lastPageOfCharts) * COMMENTS_PER_PAGE;

            if (commentFindingsIndex >= commentFindings.size()) {
                break;
            }

            comments.add(commentFindings.get(commentFindingsIndex));
        }

        ObservableList<CommentaryFinding> data = FXCollections.observableArrayList(comments);
        commentTableView.setItems(data);

        TableColumn<CommentaryFinding, Integer> numOfPageColumn = new TableColumn<>("Seite");
        numOfPageColumn.setCellValueFactory(new PropertyValueFactory<CommentaryFinding, Integer>("page"));
        numOfPageColumn.prefWidthProperty().bind(commentTableView.widthProperty().multiply(0.1));

        TableColumn<CommentaryFinding, Integer> lineColumn = new TableColumn<>("Zeile");
        lineColumn.setCellValueFactory(new PropertyValueFactory<CommentaryFinding, Integer>("line"));
        lineColumn.prefWidthProperty().bind(commentTableView.widthProperty().multiply(0.1));

        TableColumn<CommentaryFinding, String> typeColumn = new TableColumn<>("Typ");
        typeColumn.setCellValueFactory(new PropertyValueFactory<CommentaryFinding, String>("type"));
        typeColumn.prefWidthProperty().bind(commentTableView.widthProperty().multiply(0.2));

        TableColumn<CommentaryFinding, String> commentColumn = new TableColumn<>("Kommentar");
        commentColumn.setCellValueFactory(new PropertyValueFactory<CommentaryFinding, String>("comment"));
        commentColumn.prefWidthProperty().bind(commentTableView.widthProperty().multiply(0.6));

        commentTableView.getColumns().addAll(numOfPageColumn, lineColumn, typeColumn, commentColumn);

        page.getChildren().add(commentTableView);
    }

    // Following section is only for testing purposes
    private static List<BarChart> createCharts(int num) {
        List<BarChart> sampleChart = new ArrayList<BarChart>();
        for (int i = 0; i < num; i++) {
            sampleChart.add(createSampleChart(i));
        }
        return sampleChart;
    }

    private static BarChart createSampleChart(int index) {
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

    private static double generateRndDouble() {
        Random r = new Random();
        return 1 + 1000 * r.nextDouble();
    }

    private static List<CommentaryFinding> createComments(int num) {
        List<CommentaryFinding> comments = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            comments.add(new CommentaryFinding("BLABLBALBASPODJPAOIAS\nDOI", "Typ " + i, i, i * 10));
        }
        return comments;
    }
}
