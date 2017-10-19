package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * This class can be used to find all words that shouldn't be used in an
 * scientific paper.
 * <p>
 * Those words have to be configured in the
 * <a href = "../../../../../../resources/analyzer-conf/wording-blacklist.csv">
 * wording-blacklist.csv file </a>
 * </p>
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class WordingAnalyzer implements TextanalyzerAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(WordingAnalyzer.class.getName());
    private static final String BLACKLIST_PATH = "src/main/resources/analyzer-conf/wording-blacklist.csv";
    private List<String> wordingBlacklist;

    public WordingAnalyzer() {
        this(BLACKLIST_PATH);
    }

    public WordingAnalyzer(String wordingBlacklistPath) {
        this.wordingBlacklist = readWordingBlackListFile(wordingBlacklistPath);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }
        List<TextPositionSequence> matches = findMatches(pages, wordingBlacklist);
        findings.addAll(generateTextFindings(matches));
        findings.add(generateStatisticFinding(matches));
        return findings;
    }

    private List<TextPositionSequence> findMatches(List<PDFPage> pages, List<String> wordsToFind) {
        List<TextPositionSequence> hits = new ArrayList<>();
        wordsToFind.stream().forEach(word -> hits.addAll(findMatches(pages, word)));
        return hits;
    }

    private List<TextPositionSequence> findMatches(List<PDFPage> pages, String word) {
        List<TextPositionSequence> hits = new ArrayList<>();
        hits = PDFTextAnalyzerUtil.find(pages, word, PDFTextAnalyzerUtil::findWordIgnoreCase);
        return hits;
    }

    private List<? extends Finding> generateTextFindings(List<TextPositionSequence> textPositions) {
        List<TextFinding> textFindings = new ArrayList<>();
        textPositions.stream()
                .forEach(textPosition -> textFindings.add(new TextFinding(textPosition, TextFindingType.WORDING)));
        return textFindings;
    }

    private <T extends Finding> T generateStatisticFinding(List<TextPositionSequence> matches) {
        List<StatisticFindingData> data = new ArrayList<>();

        List<String> matchesAsLowercase = matches.stream().map(TextPositionSequence::toString).map(String::toLowerCase)
                .collect(Collectors.toList());

        Map<String, Long> matchesGroupedByName = matchesAsLowercase.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        matchesGroupedByName.entrySet().stream()
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));

        return (T) new StatisticFinding(StatisticFindingType.WORDING, data);
    }

    private List<String> readWordingBlackListFile(String wordingBlacklistPath) {
        List<String> wordingBlacklist = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(wordingBlacklistPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).forEach(wordingBlacklist::add);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return wordingBlacklist;
    }

    public String getUIName() {
        return WordingAnalyzer.class.getSimpleName();
    }

}
