package com.noxag.newnox.ui.pdfmodule.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFPageRenderer {
    private final static int SCALING_FACTOR = 2;

    public static List<Image> getTextHighlightingOverlay(PDDocument doc) throws IOException {
        return PDFPageRenderer.getTextHighlightingOverlay(doc, 0, doc.getNumberOfPages() - 1);
    }

    public static List<Image> getTextHighlightingOverlay(PDDocument doc, int pageIndex, int pageIndexOffset)
            throws IOException {
        List<Image> overlayImages = new ArrayList<>();
        for (int i = pageIndex; i <= (pageIndex + pageIndexOffset); i++) {
            overlayImages.add(PDFPageRenderer.getTextHighlightingOverlay(doc, i));
        }
        return overlayImages;
    }

    public static Image getTextHighlightingOverlay(PDDocument doc, int pageIndex) throws IOException {
        return new TextHighlightingRenderer(doc).renderImage(pageIndex, SCALING_FACTOR);
    }

    public static List<Image> getPDFTextOverlay(PDDocument doc) throws IOException {
        return getPDFTextOverlay(doc, 0, doc.getNumberOfPages() - 1);
    }

    public static List<Image> getPDFTextOverlay(PDDocument doc, int pageIndex, int pageIndexOffset) throws IOException {
        List<Image> pdfPages = new ArrayList<>();
        for (int i = pageIndex; i <= (pageIndex + pageIndexOffset); i++) {
            pdfPages.add(PDFPageRenderer.getPDFTextOverlay(doc, i));
        }
        return pdfPages;
    }

    public static Image getPDFTextOverlay(PDDocument doc, int pageIndex) throws IOException {
        return new PDFRenderer(doc).renderImage(pageIndex, SCALING_FACTOR, ImageType.ARGB);
    }

    public static Image getBackgroundImage(PDDocument doc) {
        PDPage page = doc.getPage(0);
        PDRectangle cropbBox = page.getCropBox();
        int widthPx = Math.round(cropbBox.getWidth() * SCALING_FACTOR);
        int heightPx = Math.round(cropbBox.getHeight() * SCALING_FACTOR);

        return createBackgroundImage(widthPx, heightPx);
    }

    private static Image createBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
        return image;
    }
}
