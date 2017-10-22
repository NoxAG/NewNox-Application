package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.TextPosition;

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
import com.noxag.newnox.textanalyzer.data.pdf.PDFParagraph;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

public class SentenceComplexityAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(WordingAnalyzer.class.getName());
    private static final int MAX_WORDS_IN_SENCTENCE = 25;

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }
        Map<PDFParagraph, Integer> sentences = getComplexity(getSentences(pages));
        if (sentences.entrySet().isEmpty()) {
            findings.add(new CommentaryFinding("No sentences found", this.getUIName(), 0, 0));
        } else {
            findings.add(generateStatisticFinding(sentences));
            findings.addAll(generateTextFindings(sentences));
        }
        return findings;
    }

    private Map<PDFParagraph, Integer> getComplexity(List<PDFParagraph> sentences) {
        Map<PDFParagraph, Integer> complexityMapping = new HashMap<>();
        sentences.stream().forEach(sentence -> {
            int wordCount = sentence.getWords().stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word))
                    .collect(Collectors.toList()).size();
            if (wordCount >= 1) {
                complexityMapping.put(sentence, wordCount);
            }
        });
        return complexityMapping;
    }

    private List<PDFParagraph> getSentences(List<PDFPage> pages) {
        List<PDFParagraph> sentences = new ArrayList<>();
        List<PDFParagraph> paragraphs = PDFTextExtractionUtil.extractParagraphs(pages);

        PDFParagraph sentence = new PDFParagraph();
        PDFLine sentenceLine = new PDFLine();
        TextPositionSequence currentWord = null;
        TextPositionSequence nextWord;
        for (PDFParagraph paragraph : paragraphs) {
            ListIterator<PDFLine> lineIterator = paragraph.getLines().stream()
                    .collect(Collectors.toCollection(LinkedList::new)).listIterator();
            while (lineIterator.hasNext()) {
                PDFLine currrentLine = lineIterator.next();
                ListIterator<TextPositionSequence> wordIterator = currrentLine.getWords().stream()
                        .collect(Collectors.toCollection(LinkedList::new)).listIterator();
                while (wordIterator.hasNext()) {
                    currentWord = wordIterator.next();
                    if (wordIterator.hasNext()) {
                        nextWord = wordIterator.next();
                        wordIterator.previous();
                    } else if (lineIterator.hasNext()) {
                        nextWord = lineIterator.next().getFirstWord();
                        lineIterator.previous();
                    } else {
                        nextWord = null;
                    }

                    sentenceLine.getWords().add(currentWord);
                    if (isPunctuationMark(currentWord, nextWord)) {
                        sentence.add(sentenceLine);
                        sentences.add(sentence);
                        sentence = new PDFParagraph();
                        sentenceLine = new PDFLine();

                    }
                }
                sentence.add(sentenceLine);
                sentenceLine = new PDFLine();
            }
            sentences.add(sentence);
            sentence = new PDFParagraph();
        }

        return sentences;

    }

    private boolean isPunctuationMark(TextPositionSequence currentWord, TextPositionSequence nextWord) {
        List<TextPosition> textPositions = currentWord.getTextPositions();
        String[] punctutationMarkers = { ".", ":" };
        boolean correctSize = textPositions.size() == 1;
        boolean puncuationmarkCharacter = Arrays.stream(punctutationMarkers)
                .anyMatch(marker -> textPositions.get(0).toString().equals(marker));
        boolean nextWordIsUpperCase = nextWord != null && Character.isUpperCase(nextWord.charAt(0));

        return correctSize && puncuationmarkCharacter && nextWordIsUpperCase;
    }

    private List<? extends Finding> generateTextFindings(Map<PDFParagraph, Integer> sentences) {
        List<TextFinding> textFindings = new ArrayList<>();
        sentences.entrySet().stream().filter(entry -> entry.getValue() >= MAX_WORDS_IN_SENCTENCE).forEach(entry -> {
            textFindings.add(new TextFinding(entry.getKey().getLines(), TextFindingType.SENTENCE_COMPLEXITY));
        });
        return textFindings;
    }

    private Finding generateStatisticFinding(Map<PDFParagraph, Integer> sentences) {
        List<StatisticFindingData> data = new ArrayList<>();
        Map<Integer, Long> sentencesGroupedByWordcount = sentences.entrySet().stream()
                .collect(Collectors.groupingBy(Entry::getValue, Collectors.counting()));

        // only entries with at least two words and entries that occur at least
        // one time
        List<Entry<Integer, Long>> sentenceMapEntries = sentencesGroupedByWordcount.entrySet().stream()
                .filter(entry -> entry.getKey() > 1).filter(entry -> entry.getValue() >= 1)
                .collect(Collectors.toList());

        sentenceMapEntries.sort(Comparator.comparing(Entry::getKey));

        sentenceMapEntries.stream().forEach(entry -> {
            String word = entry.getKey() >= 2 ? "words" : "word";
            data.add(new StatisticFindingData(entry.getKey() + " " + word, entry.getValue()));
        });

        return new StatisticFinding(StatisticFindingType.SENTENCE_COMPLEXITY, data, false);
    }

    @Override
    public String getUIName() {
        return "Check for complex sentence";
    }

}
