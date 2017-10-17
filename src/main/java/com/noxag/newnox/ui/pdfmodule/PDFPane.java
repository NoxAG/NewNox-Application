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
    private List<BufferedImage> textMarkupOverlay;
    private List<BufferedImage> pdfTextOverlay;

    public PDFPane() {
        this(new ArrayList<BufferedImage>());
    }

    public PDFPane(List<BufferedImage> pdfTextOverlay) {
        this(pdfTextOverlay, new ArrayList<BufferedImage>());
    }

    public PDFPane(List<BufferedImage> pdfTextOverlay, List<BufferedImage> textHighlightingOverlay) {
        this.pdfTextOverlay = pdfTextOverlay;
        this.textMarkupOverlay = textHighlightingOverlay;

        reloadPage();
    }

    // new
    public void reloadPage() {
        scrollPane = createScrollPane();
        fileLocationPane = createFileLocationPane();

        this.getChildren().clear();
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
            // new
            if (textMarkupOverlay.size() <= i) {
                StackList.add(createStackPane(pdfTextOverlay.get(i)));
            } else {
                StackList.add(createStackPane(textMarkupOverlay.get(i), pdfTextOverlay.get(i)));
            }
        }
        return StackList;
    }

    // new(to refactor)
    private StackPane createStackPane(BufferedImage pdfTextOverlay) {
        BufferedImage imageBackground = createBackgroundImage(pdfTextOverlay.getWidth(null),
                pdfTextOverlay.getHeight(null));

        ImageView backgroundImageView = createImageView(imageBackground);

        ImageView pdfTextImageView = createImageView(pdfTextOverlay);

        return stackImageViews(backgroundImageView, new ImageView(), pdfTextImageView);
    }

    private StackPane createStackPane(BufferedImage textHighlightingOverlay, BufferedImage pdfTextOverlay) {
        BufferedImage imageBackground = createBackgroundImage(pdfTextOverlay.getWidth(null),
                pdfTextOverlay.getHeight(null));

        ImageView backgroundImageView = createImageView(imageBackground);

        ImageView textHighlightingImageView = textHighlightingOverlay == null ? new ImageView()
                : createImageView(textHighlightingOverlay);

        ImageView pdfTextImageView = createImageView(pdfTextOverlay);

        return stackImageViews(backgroundImageView, textHighlightingImageView, pdfTextImageView);
    }

    private BufferedImage createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    private ImageView createImageView(BufferedImage image) {
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
        fileLocationPane.setCenter(new Label("No document attached"));
        return fileLocationPane;
    }

    public List<BufferedImage> getTextMarkupOverlay() {
        return textMarkupOverlay;
    }

    public void setTextMarkupOverlay(List<BufferedImage> textMarkupOverlay) {
        this.textMarkupOverlay = textMarkupOverlay;
        reloadPage();
    }

    public List<BufferedImage> getPDFTextOverlay() {
        return pdfTextOverlay;
    }

    public void setPDFTextOverlay(List<BufferedImage> pdfTextOverlay) {
        this.pdfTextOverlay = pdfTextOverlay;
        reloadPage();
    }

    public void setFileDescription(String description) {
        fileLocationPane.getChildren().clear();
        fileLocationPane.setCenter(new Label(description));
    }
}
