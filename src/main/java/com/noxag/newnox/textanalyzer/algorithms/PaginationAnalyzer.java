package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

/**
 * 
 * This class analyzes the correct pagination of the pdf
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */

public class PaginationAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        findings.addAll(getPaginationMistakes(pages));
        return findings;
    }

    private List<Finding> getPaginationMistakes(List<PDFPage> pages) {

        List<Finding> findings = new ArrayList<>();

        findings.addAll(getOutOfContentPaginationMistakes(pages));
        findings.addAll(getContentPaginationMistakes(pages));

        return findings;
    }

    private List<Finding> getOutOfContentPaginationMistakes(List<PDFPage> pages) {
        List<Finding> findings = new ArrayList<>();

        List<PDFPage> tableContentPages = PDFTextExtractionUtil.extractContentPages(pages);

        int tableContentPageIndex = tableContentPages.size() > 0
                ? tableContentPages.get(tableContentPages.size() - 1).getPageIndex()
                : 0;

        pages.stream().filter(page -> page.getPageIndex() > tableContentPageIndex && !page.isContentPage())
                .forEach(page -> {
                    TextPositionSequence lastWord = page.getLastWord();
                    if (!(isRomanNumber(lastWord.toString()) || isInteger(lastWord.toString()))) {
                        findings.add(new TextFinding(lastWord, TextFindingType.PAGINATION));
                    }
                });

        return findings;
    }

    private List<Finding> getContentPaginationMistakes(List<PDFPage> pages) {
        List<Finding> findings = new ArrayList<>();

        AtomicInteger counter = new AtomicInteger(1);
        pages.stream().filter(page -> page.isContentPage()).forEach(page -> {
            TextPositionSequence lastWord = page.getLastWord();
            if (!isInteger(lastWord.toString()) || !counter.toString().equals(lastWord.toString())) {
                findings.add(new TextFinding(lastWord, TextFindingType.PAGINATION));
            }
            counter.getAndIncrement();
        });
        return findings;
    }

    private boolean isRomanNumber(String input) {
        String pattern = "^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
        return input.matches(pattern);
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUIName() {
        return "Check pagination";
    }

}
