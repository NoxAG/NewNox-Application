package com.noxag.newnox.textanalyzer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the result of a statistical analysis
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */
public class StatisticFinding extends Finding {
    private static final String VOCABULARY_TITLE = "Vocabulary";
    private static final String PUNCTUATION_TITLE = "Punctuation Character";
    private static final String ABBREVIATION_TITLE = "Abbreviations";
    private static final String FOREIGN_TITLE = "Loanword";
    private static final String WORDING_TITLE = "Wording";
    private static final String SENTENCE_COMPLEXITY_TITLE = "Sentence Length";

    private static final String VOCABULARY_XLABEL = "Word";
    private static final String PUNCTUATION_XLABEL = "Punctuation Mark";
    private static final String ABBREVIATION_XLABEL = "Abbreviations";
    private static final String FOREIGN_XLABEL = "Loanword";
    private static final String WORDING_XLABEL = "Word";
    private static final String SENTENCE_COMPLEXITY_XLABEL = "Complexity";

    private static final String VOCABULARY_YLABEL = "Frequency";
    private static final String PUNCTUATION_YLABEL = "Frequency";
    private static final String ABBREVIATION_YLABEL = "Frequency";
    private static final String FOREIGN_YLABEL = "Frequency";
    private static final String WORDING_YLABEL = "Frequency";
    private static final String SENTENCE_COMPLEXITY_YLABEL = "Frequency";

    private static final String VOCABULARY_DATALINE = "Vocabulary";
    private static final String PUNCTUATION_DATALINE = "Punctuation Character";
    private static final String ABBREVIATION_DATALINE = "Abbreviations";
    private static final String FOREIGN_DATALINE = "Loandword";
    private static final String WORDING_DATALINE = "Wording";
    private static final String SENTENCE_COMPLEXITY_DATALINE = "Sentence Length";

    private StatisticFindingType type;
    private List<StatisticFindingData> statisticData;
    private String chartName, xAxisLabel, yAxisLabel, dataLineLabel;

    public enum StatisticFindingType {
        VOCABULARY_DISTRIBUTION, PUNCTUATION_DISTRIBUTION, COMMON_ABBREVIATION, COMMON_FOREIGN_WORD, POOR_WORDING, SENTENCE_COMPLEXITY;
    }

    public StatisticFinding() {
        this(null, null);
    }

    public StatisticFinding(StatisticFindingType typ) {
        this(typ, new ArrayList<StatisticFindingData>());
    }

    public StatisticFinding(StatisticFindingType typ, List<StatisticFindingData> data) {
        this(typ, data, "Histogramm");
    }

    public StatisticFinding(StatisticFindingType typ, List<StatisticFindingData> data, String chartName) {
        this.type = typ;
        this.statisticData = data;
        this.chartName = chartName;

        assignChartName(this.type);
        assignAxisLabel(this.type);
        assignDataLineLabel(this.type);
    }

    public StatisticFindingType getType() {
        return type;
    }

    public void setType(StatisticFindingType type) {
        this.type = type;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public String getDataLineLabel() {
        return dataLineLabel;
    }

    public void setDataLineLabel(String dataLineLabel) {
        this.dataLineLabel = dataLineLabel;
    }

    public List<StatisticFindingData> getStatisticData() {
        return statisticData;
    }

    public void setStatisticData(List<StatisticFindingData> statisticData) {
        this.statisticData = statisticData;
    }

    public void addStatisticData(StatisticFindingData statisticData) {
        this.statisticData.add(statisticData);
    }

    public void addStatisticData(List<StatisticFindingData> statisticData) {
        this.statisticData.addAll(statisticData);
    }

    private void assignChartName(StatisticFindingType typ) {
        switch (typ) {
        case VOCABULARY_DISTRIBUTION:
            this.chartName = VOCABULARY_TITLE;
            break;
        case PUNCTUATION_DISTRIBUTION:
            this.chartName = PUNCTUATION_TITLE;
            break;
        case COMMON_ABBREVIATION:
            this.chartName = ABBREVIATION_TITLE;
            break;
        case COMMON_FOREIGN_WORD:
            this.chartName = FOREIGN_TITLE;
            break;
        case POOR_WORDING:
            this.chartName = WORDING_TITLE;
            break;
        case SENTENCE_COMPLEXITY:
            this.chartName = SENTENCE_COMPLEXITY_TITLE;
            break;
        }
    }

    private void assignAxisLabel(StatisticFindingType typ) {
        switch (typ) {
        case VOCABULARY_DISTRIBUTION:
            this.xAxisLabel = VOCABULARY_XLABEL;
            this.yAxisLabel = VOCABULARY_YLABEL;
            break;
        case PUNCTUATION_DISTRIBUTION:
            this.xAxisLabel = PUNCTUATION_XLABEL;
            this.yAxisLabel = PUNCTUATION_YLABEL;
            break;
        case COMMON_ABBREVIATION:
            this.xAxisLabel = ABBREVIATION_XLABEL;
            this.yAxisLabel = ABBREVIATION_YLABEL;
            break;
        case COMMON_FOREIGN_WORD:
            this.xAxisLabel = FOREIGN_XLABEL;
            this.yAxisLabel = FOREIGN_YLABEL;
            break;
        case POOR_WORDING:
            this.xAxisLabel = WORDING_XLABEL;
            this.yAxisLabel = WORDING_YLABEL;
            break;
        case SENTENCE_COMPLEXITY:
            this.xAxisLabel = SENTENCE_COMPLEXITY_XLABEL;
            this.yAxisLabel = SENTENCE_COMPLEXITY_YLABEL;
            break;
        }
    }

    private void assignDataLineLabel(StatisticFindingType typ) {
        switch (typ) {
        case VOCABULARY_DISTRIBUTION:
            this.chartName = VOCABULARY_DATALINE;
            break;
        case PUNCTUATION_DISTRIBUTION:
            this.chartName = PUNCTUATION_DATALINE;
            break;
        case COMMON_ABBREVIATION:
            this.chartName = ABBREVIATION_DATALINE;
            break;
        case COMMON_FOREIGN_WORD:
            this.chartName = FOREIGN_DATALINE;
            break;
        case POOR_WORDING:
            this.chartName = WORDING_DATALINE;
            break;
        case SENTENCE_COMPLEXITY:
            this.chartName = SENTENCE_COMPLEXITY_DATALINE;
            break;
        }
    }
}
