package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * This class produces a statistic to show which abbreviations have been used
 * often
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class CommonAbbreviationAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static final String ABBREVIATION_LIST_PATH = "src/main/resources/analyzer-conf/common-abbreviation-list.csv";
    private List<String> abbreviationList;

    public CommonAbbreviationAnalyzer() {
        this(ABBREVIATION_LIST_PATH);
    }

    public CommonAbbreviationAnalyzer(String abbreviationListPath) {
        this.abbreviationList = readAbreviationListFile(abbreviationListPath);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        String documentText = "";
        try {
            documentText = PDFTextExtractionUtil.runTextStripper(doc).toLowerCase();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        findings.add(generateStatisticFinding(findMatches(documentText, abbreviationList)));
        return findings;
    }

    private Map<String, Integer> findMatches(String documentText, List<String> abbreviationList) {
        Map<String, Integer> matches = new HashMap<>();
        abbreviationList.stream().forEach(abbreviation -> {
            String[] documentSplited = documentText.split(getAbbreviationRegex(abbreviation));
            matches.put(abbreviation, (documentSplited.length - 1));
        });

        return matches;
    }

    private <T extends Finding> T generateStatisticFinding(Map<String, Integer> matches) {
        List<StatisticFindingData> data = new ArrayList<>();
        matches.entrySet().stream()
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));
        return (T) new StatisticFinding(StatisticFindingType.COMMON_ABBREVIATION, data);
    }

    private String getAbbreviationRegex(String abbreviation) {
        return abbreviation.toLowerCase();
    }

    @Override
    public String getUIName() {
        return CommonAbbreviationAnalyzer.class.getSimpleName();
    }

    private List<String> readAbreviationListFile(String abbreviationListPath) {
        List<String> abbreviationList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(abbreviationListPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).forEach(abbreviationList::add);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return abbreviationList;
    }
}
