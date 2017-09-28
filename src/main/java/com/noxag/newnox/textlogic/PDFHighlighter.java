package com.noxag.newnox.textlogic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextPositionSequence;

public class PDFHighlighter {

    public static void highlight(PDDocument pdfDoc, List<TextFinding> textFindings) {

    }

    /**
     * Adds an Annotation to the document to highlights the given text sequence
     * 
     * @param doc
     * @param annotationPosition
     * @throws IOException
     * 
     */
    private static void addTextMarkupAnnotation(PDDocument doc, TextPositionSequence annotationPosition)
            throws IOException {
        List<PDAnnotation> pageAnnotations = doc.getPage(annotationPosition.getPageNum() - 1).getAnnotations();
        pageAnnotations.add(generateTextMarkupAnnotation(annotationPosition));
    }

    private static PDAnnotation generateTextMarkupAnnotation(TextPositionSequence annotationPosition) {
        PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
        txtMark.setColor(new PDColor(new float[] { 1, 1, 0 }, PDDeviceRGB.INSTANCE));
        txtMark.setConstantOpacity((float) 0.5);
        txtMark.setRectangle(new PDRectangle(annotationPosition.getX(), annotationPosition.getY(),
                annotationPosition.getWidth(), annotationPosition.getHeight()));
        return txtMark;
    }

}
