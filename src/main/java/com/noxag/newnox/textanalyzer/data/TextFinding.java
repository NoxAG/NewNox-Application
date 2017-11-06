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

    public TextFinding(TextFindingType type, List<TextPositionSequence> textPositionSequences) {
        this.type = type;
        this.textPositionSequences = textPositionSequences;
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
        PAGINATION("Pagination"), WORDING("Wording"), FONT_TYPE("Font Type"), FONT_SIZE(
                "Font Size"), COMMON_ABBREVIATION("Abbreviations"), BIBLIOGRAPHY(
                        "References to Bibliography without Entry"), POSITIVE_BIBLIOGRAPHY(
                                "Found Reference in Bibliography"), LINE_SPACING("Line Spacing"), SENTENCE_COMPLEXITY(
                                        "Sentence Complexity"), REPETITIVE_WORDING(
                                                "Repetitiv Wording"), FOREIGN_WORDS("Foreign Word");
        String fieldDescriptor;

        TextFindingType(String value) {
            fieldDescriptor = value;

        }

        public String getFieldDescriptor() {
            return fieldDescriptor;
        }
    }
}
