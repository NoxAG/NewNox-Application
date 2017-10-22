/**
 * 
 */
package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * This class produces a statistic to show which words have been used often
 * 
 * @author Lars.Dittert@de.ibm.com
 *
 */
public class VocabularyDistributionAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(VocabularyDistributionAnalyzer.class.getName());
    private static final String VOCABULARY_DISTRIBUTION_EXCEPTIONS_PATH = "src/main/resources/analyzer-conf/vocabularydistributionanalyzer-blacklist.csv";
    private List<String> vocabularyDistributionExceptions;
    private static final int MAX_STATISTIC_DATA_FINDINGS = 20;
    private static final String ERROR_MESSAGE_FINDINGS_COULD_NOT_BE_CREATE = "Findings could not be create.";

    public VocabularyDistributionAnalyzer() {
        this(VOCABULARY_DISTRIBUTION_EXCEPTIONS_PATH);
    }

    public VocabularyDistributionAnalyzer(String vocabularyDistributionExceptionsPath) {
        this.vocabularyDistributionExceptions = readExceptionsFile(vocabularyDistributionExceptionsPath);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        try {
            findings.add(generateStatisticFinding(mapWordsWithFrequency(doc)));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, ERROR_MESSAGE_FINDINGS_COULD_NOT_BE_CREATE, e);
            e.printStackTrace();
        }
        return findings;
    }

    @Override
    public String getUIName() {
        return "Vocabulary distribution";
    }

    private StatisticFinding generateStatisticFinding(Map<String, Long> map) {
        List<StatisticFindingData> data = new ArrayList<>();

        List<Entry<String, Long>> hashEntries = map.entrySet().stream().collect(Collectors.toList());
        hashEntries.sort(Collections.reverseOrder(Comparator.comparing(Entry::getValue)));

        hashEntries.stream().limit(MAX_STATISTIC_DATA_FINDINGS)
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));

        return new StatisticFinding(StatisticFindingType.VOCABULARY_DISTRIBUTION, data);
    }

    public Map<String, Long> mapWordsWithFrequency(PDDocument doc) throws IOException {
        List<PDFPage> pages = new ArrayList<>();
        List<TextPositionSequence> words = new ArrayList<>();
        pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
        words = PDFTextExtractionUtil.extractWords(pages);

        List<String> matchesAsString = words.stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word))
                .map(TextPositionSequence::toString).map(String::toLowerCase).collect(Collectors.toList());

        Map<String, Long> matchesGroupedByName = matchesAsString.stream()
                .filter(word -> !vocabularyDistributionExceptions.contains(word))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return matchesGroupedByName;
    }

    private List<String> readExceptionsFile(String vocabularyDistributionExceptionsPath) {
        List<String> vocabularyDistributionExceptions = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(vocabularyDistributionExceptionsPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).map(String::toLowerCase).forEach(vocabularyDistributionExceptions::add);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return vocabularyDistributionExceptions;
    }

}
