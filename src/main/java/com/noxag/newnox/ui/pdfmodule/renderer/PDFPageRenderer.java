package com.noxag.newnox.ui.pdfmodule.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFPageRenderer {
    private final static float SCALING_FACTOR = 1.5f;

    public static List<BufferedImage> renderTextMarkupOverlay(PDDocument doc) {
        return PDFPageRenderer.renderTextMarkupOverlay(doc, 0, doc.getNumberOfPages() - 1);
    }

    public static List<BufferedImage> renderTextMarkupOverlay(PDDocument doc, int pageIndex, int pageIndexOffset) {
        List<BufferedImage> overlayImages = new ArrayList<>();
        for (int i = pageIndex; i <= (pageIndex + pageIndexOffset); i++) {
            overlayImages.add(PDFPageRenderer.renderTextMarkupOverlay(doc, i));
        }
        return overlayImages;
    }

    public static BufferedImage renderTextMarkupOverlay(PDDocument doc, int pageIndex) {
        TextMarkupRenderer renderer = new TextMarkupRenderer(doc);
        try {
            return renderer.renderImage(pageIndex, SCALING_FACTOR);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage renderPDFTextOverlay(PDDocument doc, int pageIndex) {
        PDFRenderer renderer = new PDFRenderer(doc);
        try {
            return renderer.renderImage(pageIndex, SCALING_FACTOR, ImageType.ARGB);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<BufferedImage> renderPDFTextOverlay(PDDocument doc, int pageIndex, int pageIndexOffset) {
        List<BufferedImage> pdfPages = new ArrayList<>();
        for (int i = pageIndex; i <= (pageIndex + pageIndexOffset); i++) {
            pdfPages.add(PDFPageRenderer.renderPDFTextOverlay(doc, i));
        }
        return pdfPages;
    }

    public static List<BufferedImage> renderPDFTextOverlay(PDDocument doc) {
        return renderPDFTextOverlay(doc, 0, doc.getNumberOfPages() - 1);
    }
}
