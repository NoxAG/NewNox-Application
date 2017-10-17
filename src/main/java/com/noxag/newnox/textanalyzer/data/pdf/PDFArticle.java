package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

public class PDFArticle implements PDFObject {
    private List<PDFParagraph> paragraphs;

    public PDFArticle() {
        this.paragraphs = new ArrayList<>();
    }

    public PDFArticle(List<PDFParagraph> paragraphs) {
        setParagraphs(paragraphs);
    }

    public List<PDFParagraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<PDFParagraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public List<TextPositionSequence> getWords() {
        List<TextPositionSequence> words = new ArrayList<>();
        paragraphs.stream().forEach(paragraph -> words.addAll(paragraph.getWords()));
        return words;
    }

    public PDFParagraph getFirstParagraph() {
        return paragraphs.get(0);
    }

    public PDFParagraph getLastParagraph() {
        return paragraphs.get(paragraphs.size() - 1);
    }

    @Override
    public TextPositionSequence getTextPositionSequence() {
        if (paragraphs.isEmpty()) {
            return null;
        }
        List<TextPosition> charPositions = new ArrayList<>();
        charPositions.add(this.getFirstParagraph().getTextPositionSequence().getFirstTextPosition());
        charPositions.add(this.getLastParagraph().getTextPositionSequence().getLastTextPosition());
        return new TextPositionSequence(charPositions,
                this.getFirstParagraph().getTextPositionSequence().getPageIndex());
    }

    public void add(PDFParagraph pdfParagraph) {
        if (!pdfParagraph.getLines().isEmpty()) {
            this.getParagraphs().add(pdfParagraph);
        }
    }
}
