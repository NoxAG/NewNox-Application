package com.noxag.newnox.ui.pdfmodule;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

public class PDFPageDrawer {
    private final static int SCALING_FACTOR = 2;

    public static BufferedImage getPageFromPDFAsImage(PDDocument doc, int pageIndex) {
        PDFRenderer renderer = new MyPDFRenderer(doc);
        try {
            return renderer.renderImage(pageIndex, SCALING_FACTOR);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<BufferedImage> getPagesFromPDFAsImage(PDDocument doc, int pageIndex, int pageIndexOffset) {
        List<BufferedImage> pdfPages = new ArrayList<>();
        for (int i = pageIndex; i < (pageIndex + pageIndexOffset); i++) {
            pdfPages.add(PDFPageDrawer.getPageFromPDFAsImage(doc, i));
        }
        return pdfPages;
    }

    public static List<BufferedImage> getAllPagesFromPDFAsImage(PDDocument doc) {
        return getPagesFromPDFAsImage(doc, 0, doc.getNumberOfPages() - 1);
    }

    private static class MyPDFRenderer extends PDFRenderer {
        MyPDFRenderer(PDDocument document) {
            super(document);
        }

        @Override
        protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
            return new MyPageDrawer(parameters);
        }
    }

    private static class MyPageDrawer extends PageDrawer {
        MyPageDrawer(PageDrawerParameters parameters) throws IOException {
            super(parameters);
        }

        /**
         * Custom annotation rendering.
         */
        @Override
        public void showAnnotation(PDAnnotation annotation) throws IOException {
            if (annotation instanceof PDAnnotationTextMarkup
                    && annotation.getSubtype().equals(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT)) {
                System.out.println("PDAnnotationMarkup");

                PDRectangle annotationRect = annotation.getRectangle();
                Shape bbox = new Rectangle2D.Float(annotationRect.getLowerLeftX(), annotationRect.getLowerLeftY(),
                        annotationRect.getWidth(), annotationRect.getHeight());
                Graphics2D graphics = getGraphics();
                Color color = graphics.getColor();
                Composite composite = graphics.getComposite();
                Shape clip = graphics.getClip();

                // draw
                graphics.setClip(graphics.getDeviceConfiguration().getBounds());
                graphics.setColor(new Color(annotation.getColor().toRGB()));
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        ((PDAnnotationTextMarkup) annotation).getConstantOpacity()));
                graphics.fill(bbox);

                // restore
                graphics.setColor(color);
                graphics.setClip(clip);
                graphics.setComposite(composite);

            }
        }
    }
}
