package com.noxag.newnox.textanalyzer.data;

/**
 * This class represents the result of a statistical analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class StatisticFinding extends Finding {
    private StatisticFindingType type;

    public enum StatisticFindingType {
        VOCABULARY_DISTRIBUTION, PUNCTUATION_DISTRIBUTION, COMMON_ABBREVIATION, COMMON_FOREIGN_WORD, POOR_WORDING, SENTENCE_COMPLEXITY;
    }

}
