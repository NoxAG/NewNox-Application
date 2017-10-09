package com.noxag.newnox.textanalyzer.data;

/**
 * This class represents the result of a analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */

// TODO: How can we model Findings and TextanalyzerAlgorithm, so the result of
// one algorithm may be a textfinding, a statisticsfinding or one of each
public class Finding {

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
