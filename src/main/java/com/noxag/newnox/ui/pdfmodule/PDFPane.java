package com.noxag.newnox.ui.pdfmodule;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PDFPane extends VBox {
    private ScrollPane scrollPane;
    private BorderPane fileLocationPane;
    private List<java.awt.Image> textHighlightingOverlay;
    private List<java.awt.Image> pdfTextOverlay;

    public PDFPane() {
        this(new ArrayList<java.awt.Image>());
    }

    public PDFPane(List<java.awt.Image> pdfTextOverlay) {
        this(pdfTextOverlay, new ArrayList<java.awt.Image>());
    }

    public PDFPane(List<java.awt.Image> pdfTextOverlay, List<java.awt.Image> textHighlightingOverlay) {
        this.pdfTextOverlay = pdfTextOverlay;
        this.textHighlightingOverlay = textHighlightingOverlay;

        scrollPane = createScrollPane();
        fileLocationPane = createFileLocationPane();

        this.getChildren().addAll(fileLocationPane, scrollPane);
    }

    private ScrollPane createScrollPane() {
        VBox pdfPane = createPDFPane();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: #ECDFE1;");
        scrollPane.setContent(pdfPane);

        return scrollPane;
    }

    private VBox createPDFPane() {
        VBox pdfPane = new VBox();
        pdfPane.prefHeightProperty().bind(this.heightProperty().multiply(0.9));
        List<StackPane> imageStackPanes = getListsAndCreateStacks();
        pdfPane.getChildren().addAll(imageStackPanes);
        return pdfPane;
    }

    public List<StackPane> getListsAndCreateStacks() {
        List<StackPane> StackList = new ArrayList<StackPane>();

        for (int i = 0; i < pdfTextOverlay.size(); i++) {
            StackList.add(createStackPane(textHighlightingOverlay.get(i), pdfTextOverlay.get(i)));
        }
        return StackList;
    }

    private StackPane createStackPane(java.awt.Image textHighlightingOverlay, java.awt.Image pdfTextOverlay) {
        BufferedImage imageBackground = createBackgroundImage(pdfTextOverlay.getWidth(null),
                pdfTextOverlay.getHeight(null));

        ImageView backgroundImageView = createImageView(imageBackground);

        ImageView textHighlightingImageView = textHighlightingOverlay == null ? new ImageView()
                : createImageView(textHighlightingOverlay);

        ImageView pdfTextImageView = createImageView(pdfTextOverlay);

        return stackImageViews(backgroundImageView, textHighlightingImageView, pdfTextImageView);
    }

    private BufferedImage createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.drawRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    private ImageView createImageView(java.awt.Image image) {
        Image img = SwingFXUtils.toFXImage((BufferedImage) image, null);
        return new ImageView(img);
    }

    public StackPane stackImageViews(ImageView backgroundImageView, ImageView textHighlightingImageView,
            ImageView pdfTextImageView) {
        StackPane imageStackPane = new StackPane();
        imageStackPane.getChildren().addAll(backgroundImageView, textHighlightingImageView, pdfTextImageView);
        imageStackPane.setPadding(new Insets(10, 50, 10, 50));
        return imageStackPane;
    }

    private BorderPane createFileLocationPane() {
        BorderPane fileLocationPane = new BorderPane();
        fileLocationPane.setCenter(new Label("Path of the imported File: "));
        return fileLocationPane;
    }

    public List<java.awt.Image> getTextHighlightingOverlay() {
        return textHighlightingOverlay;
    }

    public void setTextHighlightingOverlay(List<java.awt.Image> textHighlightingOverlay) {
        this.textHighlightingOverlay = textHighlightingOverlay;
    }

    public List<java.awt.Image> getPdfTextOverlay() {
        return pdfTextOverlay;
    }

    public void setPdfTextOverlay(List<java.awt.Image> pdfTextOverlay) {
        this.pdfTextOverlay = pdfTextOverlay;
    }

    public void setPath(String path) {
        fileLocationPane.getChildren().clear();
        fileLocationPane.setCenter(new Label("Path of the imported File: " + path));
    }
}
