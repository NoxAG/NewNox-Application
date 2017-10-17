package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

/**
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFLine implements PDFObject {
    List<TextPositionSequence> words;

    public PDFLine() {
        words = new ArrayList<>();
    }

    public PDFLine(List<TextPositionSequence> words) {
        this.setWords(words);
    }

    @Override
    public List<TextPositionSequence> getWords() {
        return words;
    }

    public void setWords(List<TextPositionSequence> words) {
        this.words = words;
    }

    public TextPositionSequence getFirstWord() {
        return words.get(0);
    }

    public TextPositionSequence getLastWord() {
        return words.get(words.size() - 1);
    }

    @Override
    public TextPositionSequence getTextPositionSequence() {
        if (words.isEmpty()) {
            return null;
        }
        List<TextPosition> charPositions = new ArrayList<>();
        charPositions.add(this.getFirstWord().getFirstTextPosition());
        charPositions.add(this.getLastWord().getLastTextPosition());
        return new TextPositionSequence(charPositions, this.getFirstWord().getPageIndex());
    }

    public void add(List<TextPositionSequence> words) {
        if (!words.isEmpty()) {
            this.getWords().addAll(words);
        }
    }

}
