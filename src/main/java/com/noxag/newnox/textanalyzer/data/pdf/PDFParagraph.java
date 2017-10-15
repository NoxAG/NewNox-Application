package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

public class PDFParagraph implements PDFObject {
    private List<PDFLine> lines;

    public List<PDFLine> getLines() {
        return lines;
    }

    public void setLines(List<PDFLine> lines) {
        this.lines = lines;
    }

    public PDFLine getFirstLine() {
        return lines.get(0);
    }

    public PDFLine getLastLine() {
        return lines.get(lines.size() - 1);
    }

    @Override
    public TextPositionSequence getTextPositionSequence() {
        if (lines.isEmpty()) {
            return null;
        }
        List<TextPosition> charPositions = new ArrayList<>();
        charPositions.add(this.getFirstLine().getTextPositionSequence().getFirstTextPosition());
        charPositions.add(this.getLastLine().getTextPositionSequence().getLastTextPosition());
        return new TextPositionSequence(charPositions, this.getFirstLine().getTextPositionSequence().getPageIndex());
    }

    @Override
    public List<TextPositionSequence> getWords() {
        List<TextPositionSequence> words = new ArrayList<>();
        lines.stream().forEach(line -> words.addAll(line.getWords()));
        return words;
    }

}
