/*
 * scroll pane, imageviews aneinander (liste), 
 * mehrere views übereinander, unterste kopieren, 
 * scrollen, 3fkt. set background, set pdf images, set texthightlight  
 */

package com.noxag.newnox.ui.pdfmodule;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.ui.pdfmodule.renderer.PDFPageRenderer;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PDFPane extends VBox {
    ScrollPane scrollPane;
    GridPane pdfPane;
    BorderPane fileLocationPane;
    // pane to fileLocationPane
    VBox box;
    StackPane stacky = new StackPane();
    private PDDocument pdfDocument;

    public PDFPane() {
        scrollPane = new ScrollPane();
        pdfPane = new GridPane();
        fileLocationPane = new BorderPane();
        box = new VBox();

        scrollPane.setContent(pdfPane);
        fileLocationPane.setCenter(new Label("Name of the imported file (getPath)"));
        scrollPane.setStyle("-fx-background: #ECDFE1;");
        getListAndStartNextProcedure();
        this.getChildren().addAll(fileLocationPane, scrollPane);
        scrollPane.setContent(stacky);
    }

    /**
     * This method gets two lists of all Images. (Every list has one image per
     * side). Also this method calls the method "putThreeImagesInOneStackPane()"
     * with two images. At the end, the method adds the images to the Pane.
     * 
     */

    public void getListAndStartNextProcedure() {
        List<java.awt.Image> textHighlightingOverlay = PDFPageRenderer
                .getTextHighlightingOverlayFromDocument(this.pdfDocument);
        List<java.awt.Image> pdfTextOverlay = PDFPageRenderer.getAllPagesFromPDFAsImage(this.pdfDocument);

        for (int i = 0; i < textHighlightingOverlay.size(); i++) {
            putThreeImagesInOneStackPane(textHighlightingOverlay.get(i), pdfTextOverlay.get(i));
            getChildren().addAll(fileLocationPane, scrollPane, stacky);
        }
    }

    /**
     * This method gets two images and create a new one whilst the first image
     * lay above the second one. Also this method creates the background for an
     * image.
     * 
     * @return stacky the StackPane which includes one PDF-Side
     */

    public StackPane putThreeImagesInOneStackPane(java.awt.Image textHighlightingOverlay,
            java.awt.Image pdfTextOverlay) {

        WritableImage imageBackgroundWritable = null;

        // generates canvas with white background + cast canvas to JavaFxImage
        final Canvas canvas = new Canvas(250, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        canvas.snapshot(null, (WritableImage) imageBackgroundWritable);
        BufferedImage bImage = SwingFXUtils.fromFXImage(imageBackgroundWritable, null);
        Image imageBackground = SwingFXUtils.toFXImage((BufferedImage) bImage, null);

        // Cast BufferedImage to JavaFxImage
        Image imageTextHighlighting = SwingFXUtils.toFXImage((BufferedImage) textHighlightingOverlay, null);
        Image imageText = SwingFXUtils.toFXImage((BufferedImage) pdfTextOverlay, null);

        ImageView imageViewText = new ImageView(imageText);
        ImageView imageViewBackground = new ImageView(imageBackground);
        ImageView imageViewTextHighlighting = new ImageView(imageTextHighlighting);

        stacky.getChildren().addAll(imageViewBackground, imageViewTextHighlighting, imageViewText);
        stacky.setPadding(new Insets(10, 50, 10, 50));
        return stacky;
    }
}
