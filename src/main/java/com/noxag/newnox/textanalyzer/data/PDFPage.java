package com.noxag.newnox.textanalyzer.data;

import java.util.ArrayList;
import java.util.List;

public class PDFPage {
    private List<PDFLine> lines;
    private boolean isContentPage;
    private int pageNum;
    private int pageIndex;

    public PDFPage() {
        lines = new ArrayList<>();
    }

    public PDFPage(List<PDFLine> lines) {
        this.setLines(lines);
    }

    public PDFPage(List<PDFLine> lines, int pageIndex) {
        this.setLines(lines);
        this.setPageIndex(pageIndex);
    }

    public PDFPage(List<PDFLine> lines, int pageIndex, int pageNum) {
        this.setLines(lines);
        this.setPageIndex(pageIndex);
        this.setPageNum(pageNum);
    }

    public PDFPage(List<PDFLine> lines, boolean isContentPage) {
        this.setLines(lines);
        this.setContentPage(isContentPage);
    }

    public List<TextPositionSequence> getWords() {
        List<TextPositionSequence> words = new ArrayList<>();
        this.getLines().stream().forEach(line -> words.addAll(line.getWords()));
        return words;
    }

    public List<PDFLine> getLines() {
        return lines;
    }

    public void setLines(List<PDFLine> lines) {
        this.lines = lines;
    }

    public boolean isContentPage() {
        return isContentPage;
    }

    public void setContentPage(boolean isContentPage) {
        this.isContentPage = isContentPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

}
