package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.noxag.newnox.textanalyzer.data.pdf.PDFObject;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;

/**
 * This utils class contains methods to extract text from {@link PDDocument}s
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFTextExtractionUtil {
    /**
     * Extracts the text of a {@link PDDocument}
     * 
     * @param document
     *            the document to extract the text from
     * @returns a representation of the documents' text
     * @throws IOException
     *             if PDDcument can not be read
     */
    public static List<PDFPage> extractText(PDDocument document) throws IOException {
        return extractText(document, 1, document.getNumberOfPages());
    }

    /**
     * Extracts the text of a single page of a {@link PDDocument}
     * 
     * @param document
     *            the document to extract the text from
     * @param pageIndex
     *            the '1' bases pageIndex that specifies the page to extract the
     *            text from
     * @returns a representation of the documents' text
     * @throws IOException
     *             if PDDcument can not be read
     */
    public static PDFPage extractText(PDDocument document, int pageIndex) throws IOException {
        return extractText(document, pageIndex, pageIndex).get(0);
    }

    /**
     * Extracts the text of a {@link PDDocument} from one page to another
     * 
     * @param document
     *            the document to extract the text from
     * @param pageStartIndex
     *            the '1' bases pageIndex to start extracting from
     * @param pageEndIndex
     *            the '1' bases pageIndex to end extracting from
     * @returns a representation of the documents' text
     * @throws IOException
     *             if PDDcument can not be read
     */
    public static List<PDFPage> extractText(PDDocument document, int pageStartIndex, int pageEndIndex)
            throws IOException {
        final List<PDFPage> pages = new ArrayList<>();

        PDFTextPositionSequenceStripper stripper = new PDFTextPositionSequenceStripper() {
            @Override
            public String getText(PDDocument doc) throws IOException {
                String result = super.getText(document);
                pages.addAll(this.getPDFPages());
                return result;
            }
        };

        runTextStripper(stripper, document, pageStartIndex, pageEndIndex);
        return pages;
    }

    public static String runTextStripper(PDFTextStripper stripper, PDDocument document, int pageStartIndex,
            int pageEndIndex) throws IOException {
        stripper.setSortByPosition(true);
        stripper.setStartPage(pageStartIndex);
        stripper.setEndPage(pageEndIndex);
        return stripper.getText(document);
    }

    /**
     * Extracts all words contained in the pdf data objects
     * 
     * For example you can use this function to get all words of a complete pdf
     * document represented as a list of {@link PDFPage}
     * 
     * @param pdfObjects
     *            the list of pdf objects that are to supply the source for the
     *            word extractions
     * @returns all words contained in this pdf data objects
     */
    public static <T extends PDFObject> List<TextPositionSequence> extractWords(List<T> pdfObjects) {
        List<TextPositionSequence> words = new ArrayList<>();
        pdfObjects.stream().forEach(pdfObject -> words.addAll(pdfObject.getWords()));
        return words;
    }

    public PDFTextExtractionUtil() {
        // hide constructor, because this is a completely static class
    }
}