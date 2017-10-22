package com.noxag.newnox.textanalyzer.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;

/**
 * This class represents the result of a text analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class TextFinding extends Finding {
    private List<TextPositionSequence> textPositionSequences;
    private TextFindingType type;

    public TextFinding() {
        super();
    }

    public TextFinding(TextPositionSequence textPositionSequence) {
        this(textPositionSequence, null);
    }

    public TextFinding(PDFLine line) {
        this(line, null);
    }

    public TextFinding(TextPositionSequence textPositionSequence, TextFindingType type) {
        this.textPositionSequences = new ArrayList<>();
        this.textPositionSequences.add(textPositionSequence);
        this.type = type;
    }

    public TextFinding(PDFLine lines, TextFindingType type) {
        this(new ArrayList<PDFLine>(Arrays.asList(lines)), type);
    }

    public TextFinding(List<PDFLine> lines, TextFindingType type) {
        this.type = type;
        this.textPositionSequences = new ArrayList<>();
        lines.stream().map(PDFLine::getTextPositionSequence).forEach(textPositionSequences::add);

    }

    public List<TextPositionSequence> getTextPositionSequences() {
        return textPositionSequences;
    }

    public void setTextPositionSequences(List<TextPositionSequence> textPositionSequences) {
        this.textPositionSequences = textPositionSequences;
    }

    public void addTextPositionSequence(TextPositionSequence textPositionSequence) {
        this.textPositionSequences.add(textPositionSequence);
    }

    public TextFindingType getType() {
        return type;
    }

    public void setType(TextFindingType type) {
        this.type = type;
    }

    public enum TextFindingType {
        WORDING, SENTENCE_COMPLEXITY, REPETITIV_WORDING, PAGINATION, BIBLIOGRAPHY, POSITIVE_BIBLIOGRAPHY, TABLE_OF_CONTENT, LIST_OF_ABBREVIATIONS, TABLE_OF_FIGURES, FONT_SIZE, FONT_TYPE, LINE_SPACING;
    }
}
