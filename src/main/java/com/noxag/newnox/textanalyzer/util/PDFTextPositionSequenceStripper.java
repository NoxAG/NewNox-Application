package com.noxag.newnox.textanalyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    private Pattern pattern = Pattern.compile("\\d+");

    public PDFTextPositionSequenceStripper() throws IOException {
        super();
        resetCurrentPage();
        document = new ArrayList<>();
        pdfPage = new PDFPage();
        pdfArticle = new PDFArticle();
        pdfParagraph = new PDFParagraph();
        pdfLine = new PDFLine();
        words = new ArrayList<>();

    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (containsPunctutationMark(textPositions)) {
            divideWriteStringCall(text, textPositions);
            return;
        }
        if (!textPositions.isEmpty()) {
            words.add(new TextPositionSequence(textPositions, currentPage));
        }
    }

    private void divideWriteStringCall(String text, List<TextPosition> textPositions) throws IOException {
        int punctuationMarkIndex = PDFTextAnalyzerUtil.getPunctuationMarkIndex(textPositions);

        List<TextPosition> firstPositions = textPositions.subList(0, punctuationMarkIndex);
        List<TextPosition> punctuationPosition = textPositions.subList(punctuationMarkIndex, punctuationMarkIndex + 1);

        String firstText = text.substring(0, punctuationMarkIndex);
        String punctuationText = text.substring(punctuationMarkIndex, punctuationMarkIndex + 1);

        writeString(firstText, firstPositions);
        writeString(punctuationText, punctuationPosition);
        if (punctuationMarkIndex + 1 < textPositions.size()) {
            List<TextPosition> lastPositions = textPositions.subList(punctuationMarkIndex + 1, textPositions.size());
            String lastText = text.substring(punctuationMarkIndex + 1, textPositions.size());
            writeString(lastText, lastPositions);
        }

    }

    private boolean containsPunctutationMark(List<TextPosition> textPositions) {
        boolean erstens = textPositions.size() > 1;
        boolean zweitesns = PDFTextAnalyzerUtil.containsPunctuationMark(textPositions);
        return erstens && zweitesns;
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
        pdfLine.addAll(words);
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
    protected void endPage(PDPage page) {
        pdfLine.addAll(words);
        pdfParagraph.add(pdfLine);
        pdfArticle.add(pdfParagraph);
        pdfPage.add(pdfArticle);

        String lastLine = pdfPage.getLastLine().getWords().stream().map(TextPositionSequence::toString).reduce("",
                String::concat);

        if (lastLine.matches("[0-9]+")) {
            pdfPage.setPageNum(Integer.parseInt(lastLine));
        }
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