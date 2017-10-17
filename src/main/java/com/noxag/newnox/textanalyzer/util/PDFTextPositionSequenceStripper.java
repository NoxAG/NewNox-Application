package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.noxag.newnox.textanalyzer.data.pdf.PDFArticle;
import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.PDFParagraph;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;

public class PDFTextPositionSequenceStripper extends PDFTextStripper {
    private int currentPage;
    private List<PDFPage> document;
    private PDFPage pdfPage;
    private PDFArticle pdfArticle;
    private PDFParagraph pdfParagraph;
    private PDFLine pdfLine;
    private List<TextPositionSequence> words;

    public PDFTextPositionSequenceStripper() throws IOException {
        super();
        resetCurrentPage();
        pdfPage = new PDFPage();
        pdfArticle = new PDFArticle();
        pdfParagraph = new PDFParagraph();
        pdfLine = new PDFLine();
        words = new ArrayList<>();
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        words.add(new TextPositionSequence(textPositions, currentPage));
        super.writeString(text, textPositions);
    }

    @Override
    protected void startDocument(PDDocument document) throws IOException {
        super.startDocument(document);
    }

    @Override
    protected void startPage(PDPage page) throws IOException {
        currentPage++;
        pdfPage = new PDFPage();
        super.startPage(page);
    }

    @Override
    protected void startArticle(boolean isLTR) throws IOException {
        if (pdfArticle != null && pdfParagraph != null) {
            pdfArticle.getParagraphs().add(pdfParagraph);
        }
        pdfArticle = new PDFArticle();
        pdfParagraph = new PDFParagraph();
        super.startArticle();
    }

    @Override
    protected void writeParagraphSeparator() throws IOException {
        if (pdfParagraph != null && pdfLine != null) {
            pdfParagraph.getLines().add(pdfLine);
        }
        pdfParagraph = new PDFParagraph();
        pdfLine = new PDFLine();
        super.writeParagraphSeparator();
    }

    @Override
    protected void writeLineSeparator() throws IOException {
        if (pdfLine != null && pdfLine != null) {
            pdfLine.getWords().addAll(words);
        }
        pdfLine = new PDFLine();
        words = new ArrayList<>();
        super.writeLineSeparator();
    }

    @Override
    protected void writeWordSeparator() throws IOException {
        TextPositionSequence lastWord = words.get(words.size() - 1);
        words.remove(words.size() - 1);
        words.add(new TextPositionSequence(lastWord, true));
        super.writeWordSeparator();
    }

    @Override
    protected void endPage(PDPage page) throws IOException {
        if (document != null && pdfPage != null) {
            document.add(pdfPage);
        }
        pdfPage = new PDFPage();
        super.endPage(page);
    }

    @Override
    protected void endDocument(PDDocument document) throws IOException {
        super.endDocument(document);
    }

    @Override
    public void setStartPage(int startPageValue) {
        super.setStartPage(startPageValue);
        resetCurrentPage();
    }

    public List<PDFPage> getPDFPages() {
        return document;
    }

    private void resetCurrentPage() {

        this.currentPage = getStartPage();
    }

}