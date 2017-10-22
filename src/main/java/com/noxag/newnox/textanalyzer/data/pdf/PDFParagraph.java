package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

public class PDFParagraph implements PDFObject {
    private List<PDFLine> lines;

    public PDFParagraph() {
        lines = new ArrayList<>();
    }

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
    public List<TextPositionSequence> getWords() {
        List<TextPositionSequence> words = new ArrayList<>();
        lines.stream().forEach(line -> words.addAll(line.getWords()));
        return words;
    }

    public void add(PDFLine pdfLine) {
        if (!pdfLine.getWords().isEmpty()) {
            this.getLines().add(pdfLine);
        }
    }

    public void removeLastLine() {
        this.getLines().remove(this.getLines().size() - 1);
    }

}
