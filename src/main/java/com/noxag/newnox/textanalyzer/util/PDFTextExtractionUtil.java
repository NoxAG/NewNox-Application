package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.noxag.newnox.textanalyzer.data.pdf.PDFArticle;
import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.PDFObject;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.PDFParagraph;
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

    public static String runTextStripper(PDDocument document) throws IOException {
        return runTextStripper(new PDFTextStripper(), document, 1, document.getNumberOfPages());
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

    public static List<PDFLine> extractLines(List<PDFPage> pages) {
        List<PDFLine> lines = new ArrayList<>();
        pages.stream().forEach(page -> lines.addAll(page.getLines()));
        return lines;
    }

    public static List<PDFParagraph> extractParagraphs(List<PDFPage> pages) {
        List<PDFParagraph> paragraphs = new ArrayList<>();
        pages.stream().forEach(page -> paragraphs.addAll(page.getParagraphss()));
        return paragraphs;
    }

    /**
     * Returns all pages that contain actual content (no table of content etc.)
     * <br>
     * Only reduces the page list if it is not reduce to more than half of the
     * previous size
     * 
     */
    public static List<PDFPage> extractContentPages(List<PDFPage> pages) {
        List<PDFPage> contentPages = reducetoContentPages(pages);
        if (contentPages.size() / pages.size() >= 0.5) {
            return contentPages;
        }
        return pages;

    }

    private static List<PDFPage> reducetoContentPages(List<PDFPage> pages) {
        return pages.stream().filter(PDFPage::isContentPage).collect(Collectors.toList());
    }

    public static List<PDFPage> extractTableOfContentPages(List<PDFPage> pages) {
        PDFPage firstToCPage = pages.stream().filter(page -> {
            boolean match = page.getFirstWord().toString()
                    .matches("Inhaltsverzeichnis|Content|Table of Contents|Contents");
            return match;
        }).findFirst().get();

        float H1FontSize = firstToCPage.getFirstWord().getFirstTextPosition().getFontSize();
        int firstToCPageIndex = firstToCPage.getFirstWord().getPageIndex();
        PDFPage firstPageAfterToC = pages.stream().filter(page -> {
            TextPositionSequence firstWord = page.getFirstWord();
            boolean sameFontSize = firstWord.getFirstTextPosition().getFontSize() == H1FontSize;
            boolean pageAfterTableOfConent = firstWord.getPageIndex() > firstToCPageIndex;
            return sameFontSize && pageAfterTableOfConent;
        }).findFirst().get();
        int firstPageAfterToCIndex = firstPageAfterToC.getFirstWord().getPageIndex();

        return pages.subList(firstToCPageIndex - 1, firstPageAfterToCIndex - 1);
    }

    /**
     * This method only returns content pages and removes every line that is not
     * real content like headlines and page numbers, etc. <br>
     * Only reduces the page list if it is not reduce to more than half of the
     * previous size
     * 
     * @param pages
     */
    public static List<PDFPage> reduceToContent(List<PDFPage> pages) {
        List<PDFPage> contentPages = reducetoContentPages(pages);
        if (contentPages.size() / pages.size() < 0.5) {
            return pages;
        }
        double minContentFontSize = 10;
        double maxContentFontSize = 14;
        List<PDFPage> reducedContent = new ArrayList<>();
        PDFPage reducedContentPage = new PDFPage();
        PDFArticle reducedContentArticle = new PDFArticle();
        PDFParagraph reducedContentParagraph = new PDFParagraph();
        for (PDFPage page : contentPages) {
            for (PDFArticle article : page.getArticles()) {
                for (PDFParagraph paragraph : article.getParagraphs()) {
                    paragraph.getLines().stream().filter(line -> {
                        float lineFontSize = line.getFirstWord().getFirstTextPosition().getFontSize();
                        return minContentFontSize <= lineFontSize && lineFontSize <= maxContentFontSize;
                    }).forEach(reducedContentParagraph::add);
                    reducedContentArticle.add(reducedContentParagraph);
                    reducedContentParagraph = new PDFParagraph();
                }
                reducedContentPage.add(reducedContentArticle);
                reducedContentArticle = new PDFArticle();
            }
            // remove last line because this is the page num
            reducedContentPage.getLastArticle().getLastParagraph().removeLastLine();

            reducedContent.add(reducedContentPage);
            reducedContentPage = new PDFPage();
        }

        return reducedContent;

    }

    private PDFTextExtractionUtil() {
        // hide constructor, because this is a completely static class
    }
}