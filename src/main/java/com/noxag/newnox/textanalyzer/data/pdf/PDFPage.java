package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

/**
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFPage implements PDFObject {
    private List<PDFArticle> articles;
    private Integer pageNum;

    public PDFPage() {
        articles = new ArrayList<>();
    }

    public PDFPage(List<PDFArticle> articles) {
        this(articles, null);
    }

    public PDFPage(List<PDFArticle> articles, Integer pageNum) {
        this.setArticles(articles);
        this.setPageNum(pageNum);
    }

    public List<TextPositionSequence> getWords() {
        List<TextPositionSequence> words = new ArrayList<>();
        articles.stream().forEach(article -> words.addAll(article.getWords()));
        return words;
    }

    public List<PDFArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<PDFArticle> articles) {
        this.articles = articles;
    }

    public boolean isContentPage() {
        return this.getPageNum() != null;
    }

    public TextPositionSequence getFirstWord() {
        return this.getFirstArticle().getFirstParagraph().getFirstLine().getFirstWord();
    }

    public TextPositionSequence getLastWord() {
        return this.getLastArticle().getLastParagraph().getLastLine().getLastWord();
    }

    public PDFLine getFirstLine() {
        return this.getFirstArticle().getFirstParagraph().getFirstLine();
    }

    public PDFLine getLastLine() {
        return this.getLastArticle().getLastParagraph().getLastLine();
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageIndex() {
        return this.getFirstWord().getPageIndex();
    }

    public PDFArticle getFirstArticle() {
        return articles.get(0);
    }

    public PDFArticle getLastArticle() {
        return articles.get(articles.size() - 1);
    }

    @Override
    public TextPositionSequence getTextPositionSequence() {
        if (articles.isEmpty()) {
            return null;
        }
        List<TextPosition> charPositions = new ArrayList<>();
        charPositions.add(this.getFirstArticle().getTextPositionSequence().getFirstTextPosition());
        charPositions.add(this.getLastArticle().getTextPositionSequence().getLastTextPosition());
        return new TextPositionSequence(charPositions, this.getFirstArticle().getTextPositionSequence().getPageIndex());
    }

    public void add(PDFArticle pdfArticle) {
        if (!pdfArticle.getParagraphs().isEmpty()) {
            this.getArticles().add(pdfArticle);
        }
    }

    public List<PDFLine> getLines() {
        List<PDFLine> lines = new ArrayList<>();
        articles.stream().forEach(article -> lines.addAll(article.getLines()));
        return lines;
    }

    public List<PDFParagraph> getParagraphss() {
        List<PDFParagraph> paragraphs = new ArrayList<>();
        articles.stream().forEach(article -> paragraphs.addAll(article.getParagraphs()));
        return paragraphs;
    }

}
