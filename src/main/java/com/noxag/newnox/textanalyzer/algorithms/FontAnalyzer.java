package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

public class FontAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static String[] punctuationMarks = { ",", ".", ":", ";", "!", "?" };
    private static String[] integerMarks = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> contentPages = new ArrayList<>();
        List<PDFPage> tableContentPages = new ArrayList<>();
        try {
            List<PDFPage> pages = PDFTextExtractionUtil.extractText(doc);
            contentPages = PDFTextExtractionUtil.extractContentPages(pages);
            tableContentPages = PDFTextExtractionUtil.extractTableOfContentPages(pages);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        List<TextPositionSequence> exceptions = readChaptersOutOfTableContentPage(tableContentPages);
        // findings.addAll(getWordsWithCorruptFontSize(contentPages));
        return findings;
    }

    private List<TextPositionSequence> readChaptersOutOfTableContentPage(List<PDFPage> tableContentPages) {
        List<TextPositionSequence> wordsOfTableContent = PDFTextExtractionUtil.extractWords(tableContentPages);
        List<String> chapters = new ArrayList<>();
        wordsOfTableContent.stream().forEach(word -> {
            chapters.add(readOutChapter(word));
        });
        return null;
    }

    private String readOutChapter(TextPositionSequence word) {
        List<Character> chapterMark = new ArrayList<>();
        word.chars().forEach(character -> {
            if (!(Arrays.asList(punctuationMarks).contains(word) || (Arrays.asList(integerMarks).contains(word)))) {
                chapterMark.add((char) character);
            }
        });
        return chapterMark.toString();
    }

    @Override
    public String getUIName() {
        return FontAnalyzer.class.getSimpleName();
    }

}
