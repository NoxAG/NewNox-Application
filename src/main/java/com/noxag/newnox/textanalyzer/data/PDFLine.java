package com.noxag.newnox.textanalyzer.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

public class PDFLine {
    List<TextPositionSequence> words;

    public PDFLine() {
        words = new ArrayList<>();
    }

    public PDFLine(List<TextPositionSequence> words) {
        this.setWords(words);
    }

    public List<TextPositionSequence> getWords() {
        return words;
    }

    public void setWords(List<TextPositionSequence> words) {
        this.words = words;
    }

    public TextPositionSequence getTextPositionSequence() {
        if (words.isEmpty()) {
            return null;
        }
        List<TextPosition> charPositions = new ArrayList<>();
        charPositions.add(words.get(0).getTextPositions().get(0));

        List<TextPosition> charPos = words.get(words.size() - 1).getTextPositions();
        charPositions.add(charPos.get(charPos.size() - 1));
        return new TextPositionSequence(charPositions, words.get(0).getPageIndex());
    }

    public static TextPositionSequence getSentenceTextPositionSequence(List<PDFLine> lines) {

        return null;
    }
}
