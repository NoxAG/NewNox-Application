package com.noxag.newnox.textanalyzer.data;

public class TextFinding extends Finding {
    private TextPositionSequence textPositionSequence;
    private TextFindingType type;

    public enum TextFindingType {
        BAD_WORDING, SENTENCE_COMPLEXITY, WORD_FREQUENCE, PAGINATION, BIBLIOGRAPHY;
    }
}
