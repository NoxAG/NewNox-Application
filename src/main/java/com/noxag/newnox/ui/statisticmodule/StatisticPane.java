package com.noxag.newnox.ui.statisticmodule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.noxag.newnox.textanalyzer.data.CommentaryFinding;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/**
 * This class represents the StatisticPane for the User Interface
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */

public class StatisticPane extends BorderPane {
    private static final int MAX_CHARTS = 4;
    private static final int MIN_CHARTS = 1;
    private static final int COMMENTS_PER_PAGE = 10;
    private static final double TABLE_VIEW_WIDTH_FACTOR = 0.8;
    private static final double TABLE_VIEW_HEIGHT_FACTOR = 0.9;

    private Button incrementButton, decrementButton;
    private Pagination pager;

    private List<BarChart> charts;
    private List<CommentaryFinding> commentFindings;
    private int chartsPerPageCounter = 1;

    public StatisticPane() {
        this(new ArrayList<BarChart>());
    }

    public StatisticPane(List<BarChart> barChart) {
        this(barChart, new ArrayList<CommentaryFinding>());
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

    private void initPager() {
        pager = new Pagination();
        apendPagesToPager();
        reloadPage();
    }

    private void checkIncrButton() {
        if (chartsPerPageCounter < MAX_CHARTS && chartsPerPageCounter < (charts.size())) {
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

    // Sets a callback Function for appending a page to every 'tab' of the pager
    // --> callback is called always when a new page should be oppened
    private void apendPagesToPager() {
        pager.setPageFactory(new Callback<Integer, Node>() {
            public Node call(Integer pageIndex) {
                // As long as we have unadded charts left, they will be added.
                // If all charts are already added, commentPages will be added.
                if ((int) Math.ceil((float) charts.size() / (float) chartsPerPageCounter) > pageIndex) {
                    GridPane page = new GridPane();
                    page.setAlignment(Pos.CENTER);
                    buildCharts(page, pageIndex);
                    return page;
                } else {
                    StackPane page = new StackPane();
                    page.setAlignment(Pos.CENTER);
                    buildCommentPage(page, pageIndex);
                    return page;
                }
            }
        });
    }

    // Page reload actions
    private void reloadPage() {
        calculatePager();
        checkIncrButton();
        checkDecrButton();
    }

    // Calculate number of pages to be shown
    private void calculatePager() {
        if (charts != null) {
            int numOfChartPages = (int) Math.ceil(charts.size() / (float) chartsPerPageCounter);
            if (numOfChartPages != 0) {
                pager.setPageCount(numOfChartPages + 1);
            } else {
                pager.setPageCount(1);
            }
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

    public void buildCommentPage(StackPane page, int index) {
        page.getChildren().clear();

        TableView<CommentaryFinding> commentTableView = createTableView();
        commentTableView.setItems(createDataForTable(index));
        commentTableView.getColumns().addAll(createTableColumns(commentTableView, commentTableView.widthProperty()));

        page.getChildren().add(commentTableView);
    }

    private TableView<CommentaryFinding> createTableView() {
        TableView<CommentaryFinding> commentTableView = new TableView<>();
        commentTableView.setEditable(false);
        commentTableView.prefWidthProperty().bind(this.widthProperty().multiply(TABLE_VIEW_WIDTH_FACTOR));
        commentTableView.prefHeightProperty().bind(this.heightProperty().multiply(TABLE_VIEW_HEIGHT_FACTOR));
        commentTableView.maxWidthProperty().bind(this.widthProperty().multiply(TABLE_VIEW_WIDTH_FACTOR));
        commentTableView.maxHeightProperty().bind(this.heightProperty().multiply(TABLE_VIEW_HEIGHT_FACTOR));
        return commentTableView;
    }

    // Collect all data, which has to be added to the TabbleView, in a List
    // and sets this as Item of TableView
    private ObservableList<CommentaryFinding> createDataForTable(int index) {
        List<CommentaryFinding> comments = new ArrayList<CommentaryFinding>();

        for (int i = 0; i < commentFindings.size(); i++) {
            comments.add(commentFindings.get(i));
        }

        ObservableList<CommentaryFinding> data = FXCollections.observableArrayList(comments);
        return data;
    }

    // create Head Columns for TableView
    private List<TableColumn<CommentaryFinding, ?>> createTableColumns(TableView<CommentaryFinding> tableView,
            ReadOnlyDoubleProperty widthProperty) {
        TableColumn<CommentaryFinding, Integer> numOfPageColumn = createTableColumn(widthProperty, "Seite", "page",
                0.1);
        TableColumn<CommentaryFinding, Integer> lineColumn = createTableColumn(widthProperty, "Zeile", "line", 0.1);
        TableColumn<CommentaryFinding, String> typeColumn = createTableColumn(widthProperty, "Typ", "type", 0.2);
        TableColumn<CommentaryFinding, String> commentColumn = createTableColumn(widthProperty, "Kommentar", "comment",
                0.6);

        List<TableColumn<CommentaryFinding, ?>> columnList = new ArrayList<TableColumn<CommentaryFinding, ?>>();
        Collections.addAll(columnList, numOfPageColumn, lineColumn, typeColumn, commentColumn);
        return columnList;
    }

    private <T> TableColumn<CommentaryFinding, T> createTableColumn(ReadOnlyDoubleProperty widthProperty,
            String textInField, String varNameOfCommentaryFindingObject, double widthInPercent) {
        TableColumn<CommentaryFinding, T> Column = new TableColumn<>(textInField);
        Column.setCellValueFactory(new PropertyValueFactory<CommentaryFinding, T>(varNameOfCommentaryFindingObject));
        Column.prefWidthProperty().bind(widthProperty.multiply(widthInPercent));
        return Column;
    }

    public void setCharts(List<BarChart> charts) {
        this.charts = charts;
        reloadPage();
    }

    public void setCommentFindings(List<CommentaryFinding> commentFindings) {
        this.commentFindings = commentFindings;
        reloadPage();
    }

}
