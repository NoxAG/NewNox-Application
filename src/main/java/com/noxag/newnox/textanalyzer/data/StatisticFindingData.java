package com.noxag.newnox.textanalyzer.data;

/**
 * This class represents the resulting data of a statistical analysis
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */
public class StatisticFindingData {
    private String designation;
    private double value;

    public StatisticFindingData() {
        this(null, 0);
    }

    public StatisticFindingData(String designation) {
        this(designation, 0);
    }

    public StatisticFindingData(String designation, double value) {
        this.designation = designation;
        this.value = value;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
