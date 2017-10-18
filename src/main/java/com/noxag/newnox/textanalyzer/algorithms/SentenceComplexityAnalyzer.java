package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.TextPosition;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
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
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

public class SentenceComplexityAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(WordingAnalyzer.class.getName());
    private static final int MAX_WORDS_IN_SENCTENCE = 20;

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }
        Map<PDFParagraph, Integer> sentences = getComplexity(getSentences(pages));
        findings.addAll(generateTextFindings(sentences));
        findings.add(generateStatisticFinding(sentences));
        return findings;
    }

    private Map<PDFParagraph, Integer> getComplexity(List<PDFParagraph> sentences) {
        Map<PDFParagraph, Integer> complexityMapping = new HashMap<>();
        sentences.stream().forEach(sentence -> complexityMapping.put(sentence, sentence.getWords().size()));
        return complexityMapping;
    }

    private List<PDFParagraph> getSentences(List<PDFPage> pages) {
        List<PDFParagraph> sentences = new ArrayList<>();
        List<PDFLine> lines = PDFTextExtractionUtil.extractLines(pages);

        PDFParagraph sentence = new PDFParagraph();
        PDFLine sentenceLine = new PDFLine();
        TextPositionSequence currentWord = null;
        for (PDFLine line : lines) {
            for (TextPositionSequence nextWord : line.getWords()) {
                if (currentWord != null) {
                    sentenceLine.getWords().add(currentWord);
                    if (isPunctuationMark(currentWord, nextWord)) {
                        sentences.add(sentence);
                        sentence = new PDFParagraph();
                    }
                }
                currentWord = nextWord;
            }
            sentenceLine.getWords().add(currentWord);
            sentence.getLines().add(sentenceLine);
            sentenceLine = new PDFLine();
            currentWord = null;
        }
        // add the last word
        sentenceLine.getWords().add(currentWord);
        sentence.getLines().add(sentenceLine);
        sentences.add(sentence);

        return sentences;
    }

    private boolean isPunctuationMark(TextPositionSequence currentWord, TextPositionSequence nextWord) {
        List<TextPosition> textPositions = currentWord.getTextPositions();
        return textPositions.size() == 1 && textPositions.get(0).getUnicode().equals(".")
                && Character.isUpperCase(nextWord.charAt(0));
    }

    private List<? extends Finding> generateTextFindings(Map<PDFParagraph, Integer> sentences) {
        List<TextFinding> textFindings = new ArrayList<>();
        sentences.entrySet().stream().filter(entry -> entry.getValue() >= MAX_WORDS_IN_SENCTENCE).forEach(entry -> {
            textFindings.add(new TextFinding(entry.getKey().getLines(), TextFindingType.SENTENCE_COMPLEXITY));
        });
        return textFindings;
    }

    private <T extends Finding> T generateStatisticFinding(Map<PDFParagraph, Integer> sentences) {
        List<StatisticFindingData> data = new ArrayList<>();
        Map<Integer, Long> sentencesGroupedByWordcount = sentences.entrySet().stream()
                .collect(Collectors.groupingBy(Entry::getValue, Collectors.counting()));

        sentencesGroupedByWordcount.entrySet().stream().forEach(entry -> {
            String word = entry.getKey() >= 2 ? "words" : "word";
            data.add(new StatisticFindingData(entry.getKey() + " " + word, entry.getValue()));
        });

        return (T) new StatisticFinding(StatisticFindingType.SENTENCE_COMPLEXITY, data);
    }

    @Override
    public String getUIName() {
        return SentenceComplexityAnalyzer.class.getSimpleName();
    }

}
