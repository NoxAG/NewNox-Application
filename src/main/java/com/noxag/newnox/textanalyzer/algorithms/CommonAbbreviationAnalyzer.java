package com.noxag.newnox.textanalyzer.algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
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
    private static final String ABBREVIATION_LIST_PATH = "config/common-abbreviation-list.csv";
    private List<String> abbreviationList;

    public CommonAbbreviationAnalyzer() {
        abbreviationList = this
                .readAbreviationListFile(CommonAbbreviationAnalyzer.class.getResource(ABBREVIATION_LIST_PATH));
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        List<TextPositionSequence> words = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
            words = PDFTextExtractionUtil.extractWords(pages);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        List<PDFLine> matches = findMatches(words, abbreviationList);
        if (matches.isEmpty()) {
            findings.add(new CommentaryFinding("No abbreviations found", this.getUIName(), 0, 0));
        } else {
            findings.add(generateStatisticFinding(matches));
            findings.addAll(generateTextFindings(matches));
        }
        return findings;
    }

    private List<Finding> generateTextFindings(List<PDFLine> matches) {
        return matches.stream().map(line -> new TextFinding(line, TextFindingType.COMMON_ABBREVIATION))
                .collect(Collectors.toList());
    }

    private List<PDFLine> findMatches(List<TextPositionSequence> words, List<String> abbreviationList) {
        List<PDFLine> matches = new ArrayList<>();
        ListIterator<TextPositionSequence> wordsIterator = words.stream()
                .collect(Collectors.toCollection(LinkedList::new)).listIterator();

        while (wordsIterator.hasNext()) {
            TextPositionSequence currentWord = wordsIterator.next();

            abbreviationList.stream().forEach(abbreviation -> {
                List<String> abbreviationParts = divideAbbreviation(abbreviation);
                List<TextPositionSequence> wordsToCompare = prependTo(currentWord,
                        getNextWords(wordsIterator, abbreviationParts.size() - 1));

                List<String> stringsToCompare = wordsToCompare.stream().map(TextPositionSequence::toString)
                        .map(String::toLowerCase).collect(Collectors.toList());
                if (abbreviationParts.equals(stringsToCompare)) {
                    matches.add(new PDFLine(wordsToCompare));
                }
            });
        }

        return matches;
    }

    private List<TextPositionSequence> prependTo(TextPositionSequence currentWord,
            List<TextPositionSequence> nextWords) {
        List<TextPositionSequence> allWords = new ArrayList<>();
        allWords.add(currentWord);
        allWords.addAll(nextWords);
        return allWords;
    }

    private List<String> divideAbbreviation(String abbreviation) {
        List<String> words = new ArrayList<>();
        List<String> abbreviationWithoutDot = Arrays.asList(abbreviation.split("\\."));
        abbreviationWithoutDot.forEach(abbreviationPart -> {
            words.add(abbreviationPart);
            words.add(".");
        });
        return words;
    }

    private List<TextPositionSequence> getNextWords(ListIterator<TextPositionSequence> wordsIterator,
            int abbreviationWordCount) {
        List<TextPositionSequence> nextWords = new ArrayList<>();
        for (int i = 0; i < abbreviationWordCount; i++) {
            if (wordsIterator.hasNext()) {
                nextWords.add(wordsIterator.next());
            }
        }
        for (int i = 0; i < nextWords.size(); i++) {
            wordsIterator.previous();
        }
        return nextWords;
    }

    private Finding generateStatisticFinding(List<PDFLine> matches) {
        List<StatisticFindingData> data = new ArrayList<>();

        List<String> abbreviationMatches = matches.stream().map(PDFLine::toString).map(String::toLowerCase)
                .collect(Collectors.toList());
        Map<String, Long> matchesGroupeByCount = abbreviationMatches.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        matchesGroupeByCount.entrySet()
                .forEach(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));

        return new StatisticFinding(StatisticFindingType.COMMON_ABBREVIATION, data);
    }

    @Override
    public String getUIName() {
        return "Search for abbreviations";
    }

    private List<String> readAbreviationListFile(URL abbreviationListURL) {
        List<String> abbreviationList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(
                    new BufferedReader(new InputStreamReader(abbreviationListURL.openStream())));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).map(str -> str.replaceAll(" ", "")).map(String::toLowerCase)
                        .forEach(abbreviationList::add);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return abbreviationList;
    }
}
