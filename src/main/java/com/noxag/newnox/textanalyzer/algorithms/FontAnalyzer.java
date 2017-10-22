package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
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
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

/**
 * 
 * This class finds abnormalities of font sizes and font types
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */

public class FontAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<TextPositionSequence> contentWords = new ArrayList<>();

        try {
            List<PDFPage> contentPages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
            contentWords = PDFTextExtractionUtil.extractWords(contentPages);
            contentWords = contentWords.stream().filter(TextPositionSequence::isNotBulletPoint)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        findings.addAll(getWordsWithCorruptFontSize(contentWords));
        findings.addAll(getWordsWithCorruptFontType(contentWords));
        return findings;
    }

    private List<Finding> getWordsWithCorruptFontSize(List<TextPositionSequence> contentWords) {
        Function<TextPositionSequence, Float> fontSizeFunction = (x) -> x.getFirstTextPosition().getFontSize();
        return getFontFindings(contentWords, fontSizeFunction, TextFindingType.FONT_SIZE);
    }

    private List<Finding> getWordsWithCorruptFontType(List<TextPositionSequence> contentWords) {
        Function<TextPositionSequence, String> fontTypeFunction = (x) -> x.getFirstTextPosition().getFont()
                .getFontDescriptor().getFontName();
        return getFontFindings(contentWords, fontTypeFunction, TextFindingType.FONT_TYPE);
    }

    private <T> List<Finding> getFontFindings(List<TextPositionSequence> contentWords,
            Function<TextPositionSequence, T> fontFunction, TextFindingType findingType) {
        Map<T, List<TextPositionSequence>> wordFontSizeMap = mapFont(contentWords, fontFunction);
        List<Entry<T, List<TextPositionSequence>>> wordFontSizeEntryList = sortList(wordFontSizeMap);

        return generateTextFindings(getAbnormalitiesOfList(wordFontSizeEntryList), findingType);
    }

    private <T> Map<T, List<TextPositionSequence>> mapFont(List<TextPositionSequence> contentWords,
            Function<TextPositionSequence, T> classifier) {

        Map<T, List<TextPositionSequence>> wordSizeMap = contentWords.stream()
                .collect(Collectors.groupingBy(classifier));

        return wordSizeMap;
    }

    private <T> List<Entry<T, List<TextPositionSequence>>> sortList(Map<T, List<TextPositionSequence>> wordSizeMap) {

        List<Entry<T, List<TextPositionSequence>>> wordSizeEntryList = wordSizeMap.entrySet().stream()
                .collect(Collectors.toList());

        wordSizeEntryList.sort(Comparator.comparing(entry -> {
            return entry.getValue().size();
        }));

        return wordSizeEntryList;
    }

    private <T> List<TextPositionSequence> getAbnormalitiesOfList(
            List<Entry<T, List<TextPositionSequence>>> wordSizeEntryList) {
        List<TextPositionSequence> abnormalityList = new ArrayList<>();

        wordSizeEntryList.stream().limit(wordSizeEntryList.size() - 1).forEach(entry -> {
            entry.getValue().stream().forEach(word -> {
                abnormalityList.add(word);
            });
        });

        return abnormalityList;
    }

    private List<Finding> generateTextFindings(List<TextPositionSequence> textPositions, TextFindingType findingType) {
        List<Finding> textFindings = new ArrayList<>();
        textPositions = combineIfSameLine(textPositions);
        textPositions.stream().forEach(textPosition -> textFindings.add(new TextFinding(textPosition, findingType)));
        return textFindings;
    }

    private List<TextPositionSequence> combineIfSameLine(List<TextPositionSequence> textPositions) {
        List<TextPositionSequence> words = new ArrayList<>();
        // if second word same posY as firstWord --> return combination
        // else return first word and combine second and third
        TextPositionSequence firstWord = textPositions.get(0);
        if (textPositions.size() == 1) {
            words.add(firstWord);
            return words;
        }
        TextPositionSequence secondWord = textPositions.get(1);
        @SuppressWarnings("unused")
        float res = Math.abs(firstWord.getFirstTextPosition().getY() - secondWord.getFirstTextPosition().getY());
        if (firstWord.getPageIndex() == secondWord.getPageIndex()
                && Math.abs(firstWord.getFirstTextPosition().getY() - secondWord.getFirstTextPosition().getY()) <= 1
                && Math.abs(firstWord.getLastTextPosition().getX() - secondWord.getFirstTextPosition().getX()) <= 15) {
            TextPositionSequence newFirstWord = new PDFLine(firstWord, secondWord).getTextPositionSequence();
            if (textPositions.size() > 2) {
                List<TextPositionSequence> newTextPositions = new ArrayList<>();
                newTextPositions.add(newFirstWord);
                newTextPositions.addAll(textPositions.subList(2, textPositions.size()));
                words.addAll(combineIfSameLine(newTextPositions));
            }
            return words;
        } else {
            words.add(firstWord);
            words.addAll(combineIfSameLine(textPositions.subList(1, textPositions.size())));
            return words;
        }
    }

    @Override
    public String getUIName() {
        return "Review font";
    }

}
