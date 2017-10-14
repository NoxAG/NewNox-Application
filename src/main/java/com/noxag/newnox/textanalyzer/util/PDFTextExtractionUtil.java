package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.noxag.newnox.textanalyzer.data.PDFPage;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextPositionSequence;

public class PDFTextExtractionUtil {

    /**
     * Searches through the whole document using the given finder function.
     * <p>
     * To be used like this:<br>
     * {@code findInDocument(pdfDoc, "word", PDFUtil::findWordOnPage)}
     * </p>
     * 
     * @param document
     *            the document that is searched through
     * @param searchTerm
     *            the term that is searched for
     * @param finder
     *            the function that is used to find the term. This should be one
     *            of the find* methods of the {@link TextanalyzerUtil} Class:
     *            <ul>
     *            <li>{@link #findSubwordsOnPage}</li>
     *            <li>{@link #findWordOnPage}</li>
     *            <li>{@link #findWordOnPageIgnoreCase}</li>
     *            </ul>
     * @throws IOException
     * @returns a list with all occurrences of the given word in the whole
     *          document
     */
    public static List<TextPositionSequence> findInDocument(PDDocument document, String searchTerm,
            com.noxag.newnox.textanalyzer.util.Function<PDDocument, Integer, String, List<TextPositionSequence>> finder)
            throws IOException {
        List<TextPositionSequence> allHits = new ArrayList<>();

        for (int pageNum = 1; pageNum <= document.getNumberOfPages(); pageNum++) {
            allHits.addAll(finder.apply(document, pageNum, searchTerm));
        }
        return allHits;
    }

    public static List<PDFPage> getCompleteText(PDDocument document) throws IOException {
        List<TextPositionSequence> words = new ArrayList<>();
        for (int pageNum = 1; pageNum <= document.getNumberOfPages(); pageNum++) {
            words.addAll(getCompleteText(document, pageNum));
        }
        return PDFTextAnalyzerUtil.generatePDFPages(words);
    }

    public static List<TextPositionSequence> getCompleteText(PDDocument document, int pageIndex) throws IOException {
        return findWord(document, pageIndex, e -> 0);
    }

    /**
     * Finds every occurrence of the given char sequence, even if the sequence
     * is only one part of a word in the document
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
    public static List<TextPositionSequence> findCharSequence(PDDocument document, int page, String searchTerm)
            throws IOException {

        List<TextPositionSequence> line = new ArrayList<>();

        PDFTextStripper stripper = new PDFTextStripper() {

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPositionSequence word = new TextPositionSequence(textPositions, page);
                int fromIndex = 0;
                int index;
                while ((index = text.indexOf(searchTerm, fromIndex)) > -1) {
                    line.add(word.subSequence(index, index + searchTerm.length() - 1));
                    fromIndex = index + 1;
                }
                super.writeString(text, textPositions);
            }
        };
        runTextStripper(stripper, document, page);
        return line;
    }

    /**
     * Finds the exact word in the document on the given page
     * 
     * @param document
     *            the document that is searched through
     * @param page
     *            the page number of the document that is searched through
     * @param searchTerm
     *            the word that is searched for
     * @return a list with all occurrences of the given word
     * @throws IOException
     */
    public static List<TextPositionSequence> findWord(PDDocument document, int page, String searchTerm)
            throws IOException {
        return findWord(document, page, searchTerm::compareTo);
    }

    /**
     * Finds the exact word in the document on the given page, ignoring case
     * differences
     * 
     * @param document
     *            the document that is searched through
     * @param page
     *            the page number of the document that is searched through
     * @param searchTerm
     *            the word that is searched for
     * @return a list with all occurrences of the given word
     * @throws IOException
     */
    public static List<TextPositionSequence> findWordIgnoreCase(PDDocument document, int page, String searchTerm)
            throws IOException {
        return findWord(document, page, searchTerm::compareToIgnoreCase);
    }

    private static List<TextPositionSequence> findWord(PDDocument document, int page,
            Function<String, Integer> compareTo) throws IOException {
        final List<TextPositionSequence> hits = new ArrayList<>();

        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                if (compareTo.apply(text) == 0) {
                    hits.add(new TextPositionSequence(textPositions, page));
                }
                super.writeString(text, textPositions);
            }
        };

        runTextStripper(stripper, document, page);
        return hits;
    }

    private static void runTextStripper(PDFTextStripper stripper, PDDocument document, int page) throws IOException {
        stripper.setSortByPosition(true);
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.getText(document);
    }

    public static List<TextFinding> getTextFindings(List<TextPositionSequence> textPositions) {
        List<TextFinding> findings = new ArrayList<>();
        textPositions.stream().forEach(e -> findings.add(new TextFinding(e)));
        return findings;
    }

}