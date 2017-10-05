package com.noxag.newnox.textanalyzer.data;

/**
 * This class represents the result of a text analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class TextFinding extends Finding {
    private TextPositionSequence textPositionSequence;
    private TextFindingType type;

    public enum TextFindingType {
        BAD_WORDING, SENTENCE_COMPLEXITY, WORD_FREQUENCE, PAGINATION, BIBLIOGRAPHY;
    }
}
