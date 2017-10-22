package com.noxag.newnox.ui.pdfmodule;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textlogic.PDFTextMarker;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * This class represents the PDFPane for the User Interface
 * 
 * @author Lars.Dittert@de.ibm.com
 *
 */

public class PDFPane extends VBox {
    private static final String LEGEND_BACKGROUND = "#8c8c8c";
    private ScrollPane scrollPane;
    private StackPane pdfLegendStack;
    private BorderPane fileLocationPane;
    private List<BufferedImage> textMarkupOverlay;
    private List<BufferedImage> pdfTextOverlay;
    public List<TextFindingType> textAnalyzer;

    public PDFPane() {
        this(new ArrayList<BufferedImage>());
    }

    public PDFPane(List<BufferedImage> pdfTextOverlay) {
        this(pdfTextOverlay, new ArrayList<BufferedImage>());
    }

    public PDFPane(List<BufferedImage> pdfTextOverlay, List<BufferedImage> textHighlightingOverlay) {
        this.pdfTextOverlay = pdfTextOverlay;
        this.textMarkupOverlay = textHighlightingOverlay;
        textAnalyzer = Arrays.asList(TextFindingType.values());

        initPDFPaneComponents();
    }

    public void initPDFPaneComponents() {
        scrollPane = createScrollPane();
        fileLocationPane = createFileLocationPane();
        pdfLegendStack = createPDFLegendStack();

        this.getChildren().clear();
        this.getChildren().addAll(fileLocationPane, pdfLegendStack);
    }

    private void updateScrollPane() {
        scrollPane.setContent(createPDFPane());
    }

    private ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: #CCCCCC; -fx-focus-color: transparent;");
        scrollPane.setContent(createPDFPane());

        return scrollPane;
    }

    private VBox createPDFPane() {
        VBox pdfPane = new VBox();
        pdfPane.prefHeightProperty().bind(this.heightProperty().multiply(0.95));

        List<StackPane> imageStackPanes = getListsAndCreateStacks();
        pdfPane.getChildren().addAll(imageStackPanes);
        return pdfPane;
    }

    private StackPane createPDFLegendStack() {
        StackPane pdfLegendStack = new StackPane();

        ScrollPane legendScrollPane = createLegendScrollPane();
        HBox invisiblePane = createInvisiblePane(legendScrollPane);

        pdfLegendStack.getChildren().addAll(scrollPane, invisiblePane, legendScrollPane);
        return pdfLegendStack;
    }

    private ScrollPane createLegendScrollPane() {
        ScrollPane legendScrollPane = new ScrollPane();
        legendScrollPane.setStyle("-fx-background: " + LEGEND_BACKGROUND + "; -fx-focus-color: transparent;");
        TilePane textAnalyzerLegend = createTextAnalyzerLegendPane();
        legendScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        legendScrollPane.setContent(textAnalyzerLegend);
        StackPane.setAlignment(legendScrollPane, Pos.BOTTOM_LEFT);
        legendScrollPane.setMaxHeight(100);
        legendScrollPane.maxWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        legendScrollPane.setVisible(false);
        return legendScrollPane;
    }

    private TilePane createTextAnalyzerLegendPane() {
        TilePane textAnalyzerLegendPane = new TilePane();
        textAnalyzer.stream().forEach(analyzer -> {
            Text analyzerText = new Text(analyzer.getFieldDescriptor());
            String strikeout = "false", underline = "false";
            String highlight = "transparent";

            String hexColor = getHexColorOfAnalyzer(analyzer);
            String textColor = hexColor;

            switch (PDFTextMarker.toTextMarkupSubType(analyzer)) {
            case "StrikeOut":
                strikeout = "true";
                break;
            case "Underline":
                underline = "true";
                break;
            }

            analyzerText.setStyle("-fx-padding: 5px; -fx-border-insets: 5px;  -fx-background-insets: 5px; -fx-fill: "
                    + textColor + "; -fx-underline: " + underline + "; -fx-strikethrough: " + strikeout
                    + "; -fx-font: 14pt \"Segoe UI\"; -fx-font-weight: 600");
            textAnalyzerLegendPane.setMargin(analyzerText, new Insets(5, 5, 5, 5));

            textAnalyzerLegendPane.getChildren().add(analyzerText);
        });

        return textAnalyzerLegendPane;

    }

    private String getHexColorOfAnalyzer(TextFindingType analyzer) {
        String hexColor;
        try {
            hexColor = String.format("#%06X", (0xFFFFFF & PDFTextMarker.toColor(analyzer).toRGB()));
        } catch (IOException e) {
            hexColor = "#FF000";
        }
        return hexColor;
    }

    private HBox createInvisiblePane(ScrollPane legendScrollPane) {
        HBox invisiblePane = new HBox();
        invisiblePane.maxWidthProperty().bind(legendScrollPane.widthProperty());
        invisiblePane.maxHeightProperty().bind(legendScrollPane.heightProperty());

        createOnHoverEvents(invisiblePane, legendScrollPane);

        StackPane.setAlignment(invisiblePane, Pos.BOTTOM_LEFT);

        return invisiblePane;
    }

    private void createOnHoverEvents(HBox invisiblePane, ScrollPane legendScrollPane) {
        invisiblePane.hoverProperty().addListener((observable, oldValue, show) -> {
            if (textMarkupOverlay.size() != 0) {
                legendScrollPane.setVisible(true);
            }
        });

        scrollPane.hoverProperty().addListener((observable, oldValue, show) -> {
            legendScrollPane.setVisible(false);
        });
    }

    public List<StackPane> getListsAndCreateStacks() {
        List<StackPane> stackList = new ArrayList<StackPane>();

        for (int i = 0; i < pdfTextOverlay.size(); i++) {
            if (textMarkupOverlay.size() <= i) {
                stackList.add(createStackPane(null, pdfTextOverlay.get(i)));
            } else {
                stackList.add(createStackPane(textMarkupOverlay.get(i), pdfTextOverlay.get(i)));
            }
        }
        return stackList;
    }

    private StackPane createStackPane(BufferedImage textHighlightingOverlay, BufferedImage pdfTextOverlay) {
        BufferedImage imageBackground = createBackgroundImage(pdfTextOverlay.getWidth(null),
                pdfTextOverlay.getHeight(null));

        ImageView textHighlightingImageView = textHighlightingOverlay == null ? new ImageView()
                : createImageView(textHighlightingOverlay);

        ImageView backgroundImageView = createImageView(imageBackground);
        ImageView pdfTextImageView = createImageView(pdfTextOverlay);

        return stackImageViews(backgroundImageView, textHighlightingImageView, pdfTextImageView);
    }

    private BufferedImage createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    private ImageView createImageView(BufferedImage image) {
        Image img = SwingFXUtils.toFXImage((BufferedImage) image, null);
        ImageView imgView = new ImageView(img);
        imgView.fitWidthProperty().bind(this.widthProperty().subtract(15));
        imgView.setPreserveRatio(true);
        return imgView;
    }

    public StackPane stackImageViews(ImageView backgroundImageView, ImageView textHighlightingImageView,
            ImageView pdfTextImageView) {
        StackPane imageStackPane = new StackPane();
        imageStackPane.getChildren().addAll(backgroundImageView, textHighlightingImageView, pdfTextImageView);
        imageStackPane.setPadding(new Insets(10, 0, 10, 0));

        return imageStackPane;
    }

    private BorderPane createFileLocationPane() {
        BorderPane fileLocationPane = new BorderPane();
        fileLocationPane.setCenter(new Label("No document attached"));
        fileLocationPane.minHeightProperty().bind(this.heightProperty().multiply(0.03));
        fileLocationPane.maxHeightProperty().bind(this.heightProperty().multiply(0.03));
        return fileLocationPane;
    }

    public List<BufferedImage> getTextMarkupOverlay() {
        return textMarkupOverlay;
    }

    public void setTextMarkupOverlay(List<BufferedImage> textMarkupOverlay) {
        this.textMarkupOverlay = textMarkupOverlay;
        updateScrollPane();
    }

    public List<BufferedImage> getPDFTextOverlay() {
        return pdfTextOverlay;
    }

    public void setPDFTextOverlay(List<BufferedImage> pdfTextOverlay) {
        this.pdfTextOverlay = pdfTextOverlay;
        this.textMarkupOverlay = new ArrayList<BufferedImage>();
        updateScrollPane();
    }

    public void setFileDescription(String description) {
        fileLocationPane.getChildren().clear();
        fileLocationPane.setCenter(new Label(description));
    }
}
