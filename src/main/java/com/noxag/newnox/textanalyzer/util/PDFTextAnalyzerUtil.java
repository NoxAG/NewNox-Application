package com.noxag.newnox.textanalyzer.util;

import static com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil.extractWords;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.pdfbox.text.TextPosition;

import com.noxag.newnox.textanalyzer.data.pdf.PDFObject;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;

/**
 * This utils class contains methods to analyze {@link PDFObject}s
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFTextAnalyzerUtil {
    private static String[] punctuationMarks = { ",", ".", ":", ";", "!", "?", "(", ")", "-", "–", "—", "\"", "'",
            "\u2022", "\u2023", "\u25E6", "\u2043", "\u2219" };
    private static String[] bulletPoints = { "\u2022", "\u2023", "\u25E6", "\u2043", "\u2219" };

    public static int getPunctuationMarkIndex(List<TextPosition> textPositions) {
        int index = 0;
        for (TextPosition pos : textPositions) {
            if (Arrays.asList(punctuationMarks).contains(pos.toString())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static boolean containsPunctuationMark(List<TextPosition> textPositions) {
        return textPositions.stream().anyMatch(textPosition -> {
            return Arrays.stream(punctuationMarks).anyMatch(textPosition.toString()::contains);
        });
    }

    public static boolean containsBulletPoint(List<TextPosition> textPositions) {
        return textPositions.stream().anyMatch(textPosition -> {
            return Arrays.stream(bulletPoints).anyMatch(textPosition.toString()::contains);
        });
    }

    public static boolean isPunctuationMark(TextPositionSequence posSequence) {
        return posSequence.getTextPositions().size() == 1 && containsPunctuationMark(posSequence.getTextPositions());
    }

    public static boolean isBulletPoint(TextPositionSequence textPositionSequence) {
        return textPositionSequence.getTextPositions().size() == 1
                && containsBulletPoint(textPositionSequence.getTextPositions());
    }

    /**
     * This method applies the finder function for every word contained in the
     * pdfObjects.
     * 
     * <p>
     * Example:<br>
     * {@code 
     *  List<PDFPage> pages = PDFTextExtractionUtil.extractText(doc);} <br>
     * {@code hits = PDFTextAnalyzerUtil.find(pages, searchTerm, PDFTextAnalyzerUtil::findWordIgnoreCase);}
     * 
     * </p>
     * 
     * @param pdfObjects
     * @param searchTerm
     * @param finder
     * @return
     */
    public static <T extends PDFObject> List<TextPositionSequence> find(List<T> pdfObjects, String searchTerm,
            BiFunction<List<TextPositionSequence>, String, List<TextPositionSequence>> finder) {
        return finder.apply(extractWords(pdfObjects), searchTerm);
    }

    /**
     * Finds every match of the searchTerm in a list of words
     * 
     * This finder is case sensitive.
     * 
     * @param document
     *            the document to searched through
     * @param searchTerm
     *            the word to searched for
     * @returns a list of all occurrences of the word
     */
    public static List<TextPositionSequence> findWord(List<TextPositionSequence> words, String searchTerm) {
        return findWord(words, searchTerm::compareTo);
    }

    /**
     * Finds every match of the searchTerm in a list of words
     * 
     * This finder ignores case differences.
     * 
     * @param document
     *            the document to searched through
     * @param searchTerm
     *            the word to searched for
     * @returns a list of all occurrences of the word
     */
    public static List<TextPositionSequence> findWordIgnoreCase(List<TextPositionSequence> words, String searchTerm) {
        return findWord(words, searchTerm::compareToIgnoreCase);
    }

    /**
     * Finds every occurrence of the given char sequence, even if the sequence
     * is only one part of a word in the document.
     * 
     * This finder is case sensitive.
     * 
     * <p>
     * This method won't find a multiple of words! Every time there is a
     * whitespace in the given searchTerm this method will return an empty list
     * </p>
     * 
     * @param words
     *            the words to search through
     * @param searchTerm
     *            the sequence to searched for
     * @returns a list with all occurrences of the given sequence
     */
    public static List<TextPositionSequence> findCharSequence(List<TextPositionSequence> words, String searchTerm) {
        return words.stream().filter(word -> word.contains(searchTerm)).collect(Collectors.toList());
    }

    private static List<TextPositionSequence> findWord(List<TextPositionSequence> words,
            Function<String, Integer> compareTo) {
        return words.stream().filter(word -> compareTo.apply(word.toString()) == 0).collect(Collectors.toList());
    }

    private PDFTextAnalyzerUtil() {
        // hide constructor, because this is a completely static class
    }

}
