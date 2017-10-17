package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
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
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }

        return generateTextFindings(findMatches(pages, wordingBlacklist));
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

    private List<Finding> generateTextFindings(List<TextPositionSequence> textPositions) {
        List<Finding> textFindings = new ArrayList<>();
        textPositions.stream()
                .forEach(textPosition -> textFindings.add(new TextFinding(textPosition, TextFindingType.POOR_WORDING)));
        return textFindings;
    }

    private List<String> readWordingBlackListFile(String wordingBlacklistPath) {
        List<String> wordingBlacklist = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(BLACKLIST_PATH));
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
