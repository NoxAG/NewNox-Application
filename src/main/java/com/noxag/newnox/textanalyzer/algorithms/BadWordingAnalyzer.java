package com.noxag.newnox.textanalyzer.algorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.TextanalyzerUtil;

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
public class BadWordingAnalyzer implements TextanalyzerAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(BadWordingAnalyzer.class.getName());
    private static final String BLACKLIST_PATH = "src/main/resources/analyzer-conf/wording-blacklist.csv";
    private List<String> wordingBlacklist;

    public BadWordingAnalyzer() {
        this(BLACKLIST_PATH);
    }

    public BadWordingAnalyzer(String wordingBlacklistPath) {
        this.wordingBlacklist = readBadWordingBlackListFile(wordingBlacklistPath);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        wordingBlacklist.stream().forEach(word -> findings.addAll(findWordInDocument(doc, word)));
        return findings;
    }

    public static String getUIName() {
        return BadWordingAnalyzer.class.getSimpleName();
    }

    private List<TextFinding> findWordInDocument(PDDocument doc, String searchTerm) {
        List<TextPositionSequence> textPositions = new ArrayList<>();
        try {
            textPositions = TextanalyzerUtil.findInDocument(doc, searchTerm,
                    TextanalyzerUtil::findWordOnPageIgnoreCase);

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not search through document", e);
        }
        return generateTextFindings(textPositions);
    }

    private List<TextFinding> generateTextFindings(List<TextPositionSequence> textPositions) {
        List<TextFinding> textFindings = new ArrayList<>();
        textPositions.stream()
                .forEach(textPosition -> textFindings.add(new TextFinding(textPosition, TextFindingType.BAD_WORDING)));
        return textFindings;
    }

    private List<String> readBadWordingBlackListFile(String wordingBlacklistPath) {
        List<String> wordingBlacklist = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BLACKLIST_PATH));) {
            String line = "";
            while ((line = br.readLine()) != null) {
                wordingBlacklist.addAll(Arrays.stream(line.split(",")).collect(Collectors.toList()));
            }
            // remove all whitespaces
            wordingBlacklist.stream().forEach(word -> word.replaceAll("\\s", ""));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be found", e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return wordingBlacklist;
    }

}
