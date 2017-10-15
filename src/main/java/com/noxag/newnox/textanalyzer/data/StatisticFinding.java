package com.noxag.newnox.textanalyzer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the result of a statistical analysis
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class StatisticFinding extends Finding {
    private static final int MIN_WIDTH = 150;
    private static final int MIN_HEIGHT = 50;
    private static final int PREF_WIDTH = 800;
    private static final int ÜREF_HEIGHT = 600;

    private static final String VOCABULARY_TITLE = "Vokabular";
    private static final String PUNCTUATION_TITLE = "Satzzeichen";
    private static final String ABBREVIATION_TITLE = "Abkürzungen";
    private static final String FOREIGN_TITLE = "Fremdwörter";
    private static final String WORDING_TITLE = "Wording";
    private static final String SENTENCE_COMPLEXITY_TITLE = "Satzkomplexität";

    private static final String VOCABULARY_XLABEL = "Wort";
    private static final String PUNCTUATION_XLABEL = "Zeichen";
    private static final String ABBREVIATION_XLABEL = "Abkürzung";
    private static final String FOREIGN_XLABEL = "Fremdwort";
    private static final String WORDING_XLABEL = "Wort";
    private static final String SENTENCE_COMPLEXITY_XLABEL = "Komplexität";

    private static final String VOCABULARY_YLABEL = "Häufigkeit";
    private static final String PUNCTUATION_YLABEL = "Häufigkeit";
    private static final String ABBREVIATION_YLABEL = "Häufigkeit";
    private static final String FOREIGN_YLABEL = "Häufigkeit";
    private static final String WORDING_YLABEL = "Häufigkeit";
    private static final String SENTENCE_COMPLEXITY_YLABEL = "Häufigkeit";

    private static final String VOCABULARY_DATALINE = "Vokabular";
    private static final String PUNCTUATION_DATALINE = "Satzzeichen";
    private static final String ABBREVIATION_DATALINE = "Abkürzungen";
    private static final String FOREIGN_DATALINE = "Fremdwörter";
    private static final String WORDING_DATALINE = "Wording";
    private static final String SENTENCE_COMPLEXITY_DATALINE = "Satzkomplexität";

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
