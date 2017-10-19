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

public class RepetitivWordingAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static final String REPETITIV_WORDING_EXCEPTION_PATH = "src/main/resources/analyzer-conf/repetitiv-wording-exceptions.csv";

    private static final int AMOUNT_OF_WORDS_TO_COMPARE = 20;
    private static final int ALLOWED_REPETITIONS_BY_DEFAULT = 2;

    private Map<String, Integer> repretitivWordingExceptions;

    public RepetitivWordingAnalyzer() {
        repretitivWordingExceptions = readRepetitiveWordingExceptionFile(REPETITIV_WORDING_EXCEPTION_PATH);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<PDFPage> pages = new ArrayList<>();
        List<Finding> findings = new ArrayList<>();

        try {
            pages = PDFTextExtractionUtil.extractContentPages(PDFTextExtractionUtil.extractText(doc));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        findings.addAll(getRepetitionsInWordBlock(pages));
        return findings;
    }

    private List<Finding> getRepetitionsInWordBlock(List<PDFPage> pages) {
        List<TextPositionSequence> wordInPDF = PDFTextExtractionUtil.extractWords(pages);
        List<Finding> findings = new ArrayList<>();

        Map<String, Integer> repetitivWordMap = new HashMap<>();
        List<TextPositionSequence> actuallyWordBlock = new ArrayList<>();

        wordInPDF.stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word) && !isInteger(word.toString()))
                .forEach(nextWord -> {
                    if (actuallyWordBlock.size() < AMOUNT_OF_WORDS_TO_COMPARE) {
                        repetitivWordMap.put(nextWord.toString(), calculateValueOfKey(nextWord, repetitivWordMap));
                        actuallyWordBlock.add(nextWord);
                    } else {
                        removeKeyOutOfMap(repetitivWordMap, actuallyWordBlock.get(0).toString());
                        repetitivWordMap.put(nextWord.toString(), calculateValueOfKey(nextWord, repetitivWordMap));
                        actuallyWordBlock.remove(0);
                        actuallyWordBlock.add(nextWord);
                    }
                    findings.addAll(addMultiplesToFindings(repetitivWordMap, actuallyWordBlock));
                });
        return findings;
    }

    private Integer calculateValueOfKey(TextPositionSequence nextWord, Map<String, Integer> repetitivWordMap) {
        return (repetitivWordMap.containsKey(nextWord.toString()) ? repetitivWordMap.get(nextWord.toString()) : 0) + 1;
    }

    private void removeKeyOutOfMap(Map<String, Integer> map, String key) {
        map.put(key, map.get(key) - 1);
        if (map.get(key) == 0) {
            map.remove(key);
        }
    }

    private List<Finding> addMultiplesToFindings(Map<String, Integer> repetitivWordMap,
            List<TextPositionSequence> actuallyWordBlock) {
        List<Finding> findings = new ArrayList<>();
        repetitivWordMap.entrySet().stream().filter(entry -> {
            if (repretitivWordingExceptions.containsKey(entry.getKey())) {
                return entry.getValue() > repretitivWordingExceptions.get(entry.getKey());
            } else {
                return entry.getValue() > ALLOWED_REPETITIONS_BY_DEFAULT;
            }
        }).forEach(repetitiveEntry -> {
            findings.addAll(getAllPositionSequencesThroughString(actuallyWordBlock, repetitiveEntry));
        });
        return findings;
    }

    private List<Finding> getAllPositionSequencesThroughString(List<TextPositionSequence> actuallyWordBlock,
            Entry<String, Integer> repetitiveEntry) {
        List<Finding> foundPositionSequences = new ArrayList<>();
        actuallyWordBlock.stream().filter(wordsOfBlock -> wordsOfBlock.toString().equals(repetitiveEntry.getKey()))
                .forEach(entryPositionSequence -> {
                    foundPositionSequences
                            .add(new TextFinding(entryPositionSequence, TextFindingType.REPETITIV_WORDING));
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
        return RepetitivWordingAnalyzer.class.getSimpleName();
    }

}
