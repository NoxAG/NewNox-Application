package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.pdf.PDFObject;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;

public class PDFTextExtractionUtil {

    public static List<TextPositionSequence> extractText(PDDocument document) throws IOException {
        return extractText(document, 1, document.getNumberOfPages());
    }

    public static List<TextPositionSequence> extractText(PDDocument document, int pageIndex) throws IOException {
        return extractText(document, pageIndex, pageIndex);
    }

    public static List<TextPositionSequence> extractText(PDDocument document, int pageStartIndex, int pageEndIndex)
            throws IOException {
        final List<TextPositionSequence> words = new ArrayList<>();

        PDFTextPositionSequenceStripper stripper = new PDFTextPositionSequenceStripper() {
            @Override
            public String getText(PDDocument doc) throws IOException {
                String result = super.getText(document);
                words.addAll(PDFObject.getWords(this.getPDFPages()));
                return result;
            }
        };

        runTextStripper(stripper, document, pageStartIndex, pageEndIndex);
        return words;
    }

    /**
     * Finds every occurrence of the given char sequence, even if the sequence
     * is only one part of a word in the document
     * 
     * <p>
     * This method won't find a multiple of words! Every time there is a
     * whitespace in the given searchTerm this method will return an empty list
     * </p>
     * 
     * 
     * @param document
     *            the document that is searched through
     * @param page
     *            the page number of the document that is searched through
     * @param searchTerm
     *            the char sequence that is searched for
     * @return a list with all occurrences of the given searchTerm
     * @throws IOException
     */
    public static List<TextPositionSequence> findCharSequence(List<TextPositionSequence> words, String searchTerm)
            throws IOException {

        return null;
    }

    /**
     * Finds the exact word in the document on the given page
     * 
     * @param document
     *            the document that is searched through
     * @param pageIndex
     *            the page number of the document that is searched through
     * @param searchTerm
     *            the word that is searched for
     * @return a list with all occurrences of the given word
     * @throws IOException
     */
    public static List<TextPositionSequence> findWord(PDDocument document, int pageIndex, String searchTerm)
            throws IOException {
        return findWord(extractText(document, pageIndex), searchTerm);
    }

    public static List<TextPositionSequence> findWord(PDDocument document, String searchTerm) throws IOException {
        return findWord(extractText(document), searchTerm);
    }

    public static List<TextPositionSequence> findWord(List<TextPositionSequence> words, String searchTerm)
            throws IOException {
        return findWord(words, searchTerm::compareTo);
    }

    /**
     * Finds the exact word in the document on the given page, ignoring case
     * differences
     * 
     * @param document
     *            the document that is searched through
     * @param pageIndex
     *            the page number of the document that is searched through
     * @param searchTerm
     *            the word that is searched for
     * @return a list with all occurrences of the given word
     * @throws IOException
     */
    public static List<TextPositionSequence> findWordIgnoreCase(PDDocument document, int pageIndex, String searchTerm)
            throws IOException {
        return findWord(extractText(document, pageIndex), searchTerm);
    }

    public static List<TextPositionSequence> findWordIgnoreCase(PDDocument document, String searchTerm)
            throws IOException {
        return findWord(extractText(document), searchTerm);
    }

    public static List<TextPositionSequence> findWordIgnoreCase(List<TextPositionSequence> words, String searchTerm)
            throws IOException {
        return findWord(words, searchTerm::compareToIgnoreCase);
    }

    private static List<TextPositionSequence> findWord(List<TextPositionSequence> words,
            Function<String, Integer> compareTo) throws IOException {

        return words.stream().filter(word -> compareTo.apply(word.toString()) == 0).collect(Collectors.toList());

    }

    private static void runTextStripper(PDFTextStripper stripper, PDDocument document, int pageStartIndex,
            int pageEndIndex) throws IOException {
        stripper.setSortByPosition(true);
        stripper.setStartPage(pageStartIndex);
        stripper.setEndPage(pageEndIndex);
        stripper.getText(document);
    }

    // Test function
    // TODO: remove
    public static List<TextFinding> getTextFindings(List<TextPositionSequence> textPositions) {
        List<TextFinding> findings = new ArrayList<>();
        textPositions.stream().forEach(e -> findings.add(new TextFinding(e)));
        return findings;
    }

}