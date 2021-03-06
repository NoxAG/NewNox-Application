package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * Algorithm for finding word repetitons
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */

public class RepetitiveWordingAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static final String REPETITIVE_WORDING_EXCEPTION_PATH = "src/main/resources/analyzer-conf/repetitive-wording-exceptions.csv";

    private static final int AMOUNT_OF_WORDS_TO_COMPARE = 20;
    private static final int ALLOWED_REPETITIONS_BY_DEFAULT = 2;

    private Map<String, Integer> repretitivWordingExceptions;

    public RepetitiveWordingAnalyzer() {
        repretitivWordingExceptions = readRepetitiveWordingExceptionFile(REPETITIVE_WORDING_EXCEPTION_PATH);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<PDFPage> pages = new ArrayList<>();

        try {
            pages = PDFTextExtractionUtil.extractContentPages(PDFTextExtractionUtil.extractText(doc));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        List<Finding> findings = getRepetitionsInWordBlock(pages);
        if (findings.isEmpty()) {
            findings.add(new CommentaryFinding("No word repetitions found", this.getUIName(), 0, 0));
        }
        return findings;
    }

    private List<Finding> getRepetitionsInWordBlock(List<PDFPage> pages) {
        List<TextPositionSequence> wordInPDF = PDFTextExtractionUtil.extractWords(pages);
        List<Finding> findings = new ArrayList<>();

        Map<String, Integer> repetitiveWordMap = new HashMap<>();
        // This var saves last 20 words as TextPositionSequence, which were
        // analyzed in HashMap
        List<TextPositionSequence> actuallyWordBlock = new ArrayList<>();

        wordInPDF.stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word) && !isInteger(word.toString()))
                .forEach(nextWord -> {
                    if (actuallyWordBlock.size() < AMOUNT_OF_WORDS_TO_COMPARE) {
                        repetitiveWordMap.put(nextWord.toString(), calculateValueOfKey(nextWord, repetitiveWordMap));
                        actuallyWordBlock.add(nextWord);
                    } else {
                        removeKeyOutOfMap(repetitiveWordMap, actuallyWordBlock.get(0).toString());
                        repetitiveWordMap.put(nextWord.toString(), calculateValueOfKey(nextWord, repetitiveWordMap));
                        actuallyWordBlock.remove(0);
                        actuallyWordBlock.add(nextWord);
                    }
                    findings.addAll(addRepetitiveWordsToFindings(repetitiveWordMap, actuallyWordBlock));
                });
        return findings;
    }

    private Integer calculateValueOfKey(TextPositionSequence nextWord, Map<String, Integer> repetitiveWordMap) {
        return (repetitiveWordMap.containsKey(nextWord.toString()) ? repetitiveWordMap.get(nextWord.toString()) + 1
                : 1);
    }

    private void removeKeyOutOfMap(Map<String, Integer> map, String key) {
        map.put(key, map.get(key) - 1);
        if (map.get(key) == 0) {
            map.remove(key);
        }
    }

    private List<Finding> addRepetitiveWordsToFindings(Map<String, Integer> repetitiveWordMap,
            List<TextPositionSequence> actuallyWordBlock) {
        List<Finding> findings = new ArrayList<>();
        repetitiveWordMap.entrySet().stream().filter(entry -> {
            return getAllowedRepetitionsForEntry(entry);
        }).forEach(repetitiveEntry -> {
            findings.addAll(getAllPositionSequencesThroughString(actuallyWordBlock, repetitiveEntry));
        });
        return findings;
    }

    private boolean getAllowedRepetitionsForEntry(Entry<String, Integer> entry) {
        if (repretitivWordingExceptions.containsKey(entry.getKey())) {
            return entry.getValue() > repretitivWordingExceptions.get(entry.getKey());
        } else {
            return entry.getValue() > ALLOWED_REPETITIONS_BY_DEFAULT;
        }
    }

    private List<Finding> getAllPositionSequencesThroughString(List<TextPositionSequence> actuallyWordBlock,
            Entry<String, Integer> repetitiveEntry) {
        List<Finding> foundPositionSequences = new ArrayList<>();
        actuallyWordBlock.stream().filter(wordsOfBlock -> wordsOfBlock.toString().equals(repetitiveEntry.getKey()))
                .forEach(entryPositionSequence -> {
                    foundPositionSequences
                            .add(new TextFinding(entryPositionSequence, TextFindingType.REPETITIVE_WORDING));
                });
        return foundPositionSequences;
    }

    private Map<String, Integer> readRepetitiveWordingExceptionFile(String repetitiveWordingExceptionPath) {
        Map<String, Integer> exceptionMap = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(repetitiveWordingExceptionPath));

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (isInteger(line[1])) {
                    exceptionMap.put(line[0], Integer.parseInt(line[1]));
                }
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return exceptionMap;
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUIName() {
        return "Mark repetitive words";
    }

}
