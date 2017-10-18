package com.noxag.newnox.textanalyzer.data;

import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;

/**
 * This class represents the result of a text analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class TextFinding extends Finding {
    private TextPositionSequence textPositionSequence;
    private TextFindingType type;

    public TextFinding() {
        super();
    }

    public TextFinding(TextPositionSequence textPositionSequence) {
        this(textPositionSequence, null);
    }

    public TextFinding(TextPositionSequence textPositionSequence, TextFindingType type) {
        this.textPositionSequence = textPositionSequence;
        this.type = type;
    }

    public TextPositionSequence getTextPositionSequence() {
        return textPositionSequence;
    }

    public void setTextPositionSequence(TextPositionSequence textPositionSequence) {
        this.textPositionSequence = textPositionSequence;
    }

    public TextFindingType getType() {
        return type;
    }

    public void setType(TextFindingType type) {
        this.type = type;
    }

    public enum TextFindingType {
        WORDING, SENTENCE_COMPLEXITY, REPETITIV_WORDING, PAGINATION, BIBLIOGRAPHY, TABLE_OF_CONTENT, LIST_OF_ABBREVIATIONS, TABLE_OF_FIGURES;
    }
}
