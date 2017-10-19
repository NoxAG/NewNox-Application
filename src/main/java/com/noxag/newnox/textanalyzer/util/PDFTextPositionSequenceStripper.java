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
    private List<String> punctuationMarks;

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
        document = new ArrayList<>();
        pdfPage = new PDFPage();
        pdfArticle = new PDFArticle();
        pdfParagraph = new PDFParagraph();
        pdfLine = new PDFLine();
        words = new ArrayList<>();

        punctuationMarks = new ArrayList<>();
        punctuationMarks.add(".");
        punctuationMarks.add(":");
        punctuationMarks.add(";");
        punctuationMarks.add("?");
        punctuationMarks.add("!");
        punctuationMarks.add(",");
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (containsPunctutationMark(textPositions)) {
            divideWriteStringCall(text, textPositions);
            return;
        }

        words.add(new TextPositionSequence(textPositions, currentPage));
    }

    private void divideWriteStringCall(String text, List<TextPosition> textPositions) throws IOException {
        int lastTextIndex = textPositions.size();
        List<TextPosition> firstPositions = textPositions.subList(0, lastTextIndex - 1);
        List<TextPosition> punctuationPosition = textPositions.subList(lastTextIndex - 1, lastTextIndex);
        String firstText = text.substring(0, lastTextIndex - 1);
        String punctuationText = text.substring(lastTextIndex - 1, lastTextIndex);
        writeString(firstText, firstPositions);
        writeString(punctuationText, punctuationPosition);
    }

    private boolean containsPunctutationMark(List<TextPosition> textPositions) {
        return textPositions.size() > 1
                && punctuationMarks.stream().anyMatch(textPositions.get(textPositions.size() - 1).toString()::equals);
    }

    @Override
    protected void startDocument(PDDocument document) throws IOException {
        super.startDocument(document);
    }

    @Override
    protected void startPage(PDPage page) throws IOException {
        currentPage++;
        pdfPage = new PDFPage();
        pdfArticle = new PDFArticle();
        pdfParagraph = new PDFParagraph();
        pdfLine = new PDFLine();
        words = new ArrayList<>();
    }

    @Override
    protected void startArticle(boolean isLTR) throws IOException {
        pdfArticle.add(pdfParagraph);
        pdfPage.add(pdfArticle);

        pdfArticle = new PDFArticle();
        pdfParagraph = new PDFParagraph();
    }

    @Override
    protected void writeParagraphSeparator() throws IOException {
        pdfParagraph.add(pdfLine);
        pdfArticle.add(pdfParagraph);
        pdfParagraph = new PDFParagraph();
        pdfLine = new PDFLine();
    }

    @Override
    protected void writeLineSeparator() throws IOException {
        pdfLine.add(words);
        pdfParagraph.add(pdfLine);
        pdfLine = new PDFLine();
        words = new ArrayList<>();
    }

    @Override
    protected void writeWordSeparator() throws IOException {
        TextPositionSequence lastWord = words.get(words.size() - 1);
        words.remove(words.size() - 1);
        words.add(new TextPositionSequence(lastWord, true));
    }

    @Override
    protected void endPage(PDPage page) throws IOException {
        pdfParagraph.add(pdfLine);
        pdfArticle.add(pdfParagraph);
        pdfPage.add(pdfArticle);
        document.add(pdfPage);
        pdfPage = new PDFPage();
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
        this.currentPage = getStartPage() - 1;
    }

}