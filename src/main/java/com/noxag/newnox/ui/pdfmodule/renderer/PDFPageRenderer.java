package com.noxag.newnox.ui.pdfmodule.renderer;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFPageRenderer {
    private final static int SCALING_FACTOR = 2;

    public static List<Image> getTextHighlightingOverlayFromDocument(PDDocument doc) {
        return PDFPageRenderer.getTextHighlightingOverlayForPages(doc, 0, doc.getNumberOfPages() - 1);
    }

    public static List<Image> getTextHighlightingOverlayForPages(PDDocument doc, int pageIndex, int pageIndexOffset) {
        List<Image> overlayImages = new ArrayList<>();
        for (int i = pageIndex; i <= (pageIndex + pageIndexOffset); i++) {
            overlayImages.add(PDFPageRenderer.getTextHighlightingOverlayForPage(doc, i));
        }
        return overlayImages;
    }

    public static Image getTextHighlightingOverlayForPage(PDDocument doc, int pageIndex) {
        TextMarkupRenderer renderer = new TextMarkupRenderer(doc);
        try {
            return renderer.renderImage(pageIndex, SCALING_FACTOR);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image getPageFromPDFAsImage(PDDocument doc, int pageIndex) {
        PDFRenderer renderer = new PDFRenderer(doc);
        try {
            return renderer.renderImage(pageIndex, SCALING_FACTOR, ImageType.ARGB);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Image> getPagesFromPDFAsImage(PDDocument doc, int pageIndex, int pageIndexOffset) {
        List<Image> pdfPages = new ArrayList<>();
        for (int i = pageIndex; i <= (pageIndex + pageIndexOffset); i++) {
            pdfPages.add(PDFPageRenderer.getPageFromPDFAsImage(doc, i));
        }
        return pdfPages;
    }

    public static List<Image> getAllPagesFromPDFAsImage(PDDocument doc) {
        return getPagesFromPDFAsImage(doc, 0, doc.getNumberOfPages() - 1);
    }
}
