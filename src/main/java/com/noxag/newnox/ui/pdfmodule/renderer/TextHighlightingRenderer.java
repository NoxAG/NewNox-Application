package com.noxag.newnox.ui.pdfmodule.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

public class TextHighlightingRenderer extends PDFRenderer {
    TextHighlightingRenderer(PDDocument document) {
        super(document);
    }

    @Override
    public BufferedImage renderImage(int pageIndex, float scale) throws IOException {
        return renderImage(pageIndex, scale, ImageType.ARGB);
    }

    @Override
    protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
        return new TextHighlightingDrawer(parameters);
    }
}
