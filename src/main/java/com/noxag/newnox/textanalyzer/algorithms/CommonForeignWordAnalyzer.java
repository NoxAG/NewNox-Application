package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
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

public class CommonForeignWordAnalyzer implements TextanalyzerAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(CommonForeignWordAnalyzer.class.getName());
    private static final String FOREIGN_WORDS_PATH = "src/main/resources/analyzer-conf/common-foreign-words.csv";
    private List<String> foreignWords;

    public CommonForeignWordAnalyzer() {
        this(FOREIGN_WORDS_PATH);
    }

    public CommonForeignWordAnalyzer(String foreignWordsPath) {
        this.foreignWords = readForeignWordFile(foreignWordsPath);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }
        List<PDFLine> matches = findMatches(PDFTextExtractionUtil.extractWords(pages), foreignWords);
        if (matches.isEmpty()) {
            findings.add(new CommentaryFinding("No foreign words found", this.getUIName(), 0, 0));
        } else {
            findings.addAll(generateTextFindings(matches));
            findings.add(generateStatisticFinding(matches));
        }
        return findings;
    }

    private List<PDFLine> findMatches(List<TextPositionSequence> words, List<String> foreignWordPhrases) {
        List<PDFLine> matches = new ArrayList<>();
        ListIterator<TextPositionSequence> wordsIterator = words.stream()
                .collect(Collectors.toCollection(LinkedList::new)).listIterator();

        while (wordsIterator.hasNext()) {
            TextPositionSequence currentWord = wordsIterator.next();

            foreignWordPhrases.stream().forEach(phrase -> {
                List<String> foreignWordPhrase = Arrays.asList(phrase.split(" "));
                List<TextPositionSequence> wordsToCompare = prependTo(currentWord,
                        getNextWords(wordsIterator, foreignWordPhrase.size() - 1));

                List<String> stringsToCompare = wordsToCompare.stream().map(TextPositionSequence::toString)
                        .map(String::toLowerCase).collect(Collectors.toList());
                if (foreignWordPhrase.equals(stringsToCompare)) {
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

    private List<? extends Finding> generateTextFindings(List<PDFLine> textPositions) {
        List<TextFinding> textFindings = new ArrayList<>();
        textPositions.stream().forEach(textPosition -> textFindings
                .add(new TextFinding(TextFindingType.FOREIGN_WORDS, textPosition.getWords())));
        return textFindings;
    }

    private Finding generateStatisticFinding(List<PDFLine> matches) {
        List<StatisticFindingData> data = new ArrayList<>();
        List<String> matchesAsLowercase = matches.stream().map(PDFLine::toString).map(String::toLowerCase)
                .collect(Collectors.toList());

        Map<String, Long> matchesGroupedByName = matchesAsLowercase.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        matchesGroupedByName.entrySet().stream()
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));

        return new StatisticFinding(StatisticFindingType.FOREIGN_WORDS, data);
    }

    private String appendWhiteSpace(String str) {
        return str + " ";
    }

    private List<String> readForeignWordFile(String foreignWordsPath) {
        List<String> foreignWords = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(foreignWordsPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).map(String::toLowerCase).forEach(foreignWords::add);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return foreignWords;
    }

    public String getUIName() {
        return "Search for foreign words";
    }

}
