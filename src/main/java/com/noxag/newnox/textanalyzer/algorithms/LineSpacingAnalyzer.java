package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.TextPosition;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

public class LineSpacingAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(WordingAnalyzer.class.getName());

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }
        List<TextPositionSequence> matches = getLinespacings(pages);
        findings.addAll(generateTextFindings(matches));
        return findings;
    }

    private List<TextPositionSequence> getLinespacings(List<PDFPage> pages) {
        List<TextPositionSequence> linespacings = new ArrayList<>();
        List<PDFLine> lines = PDFTextExtractionUtil.extractLines(pages);
        ListIterator<PDFLine> lineIterator = lines.stream().collect(Collectors.toCollection(LinkedList::new))
                .listIterator();

        TextPositionSequence currentLineWord;
        TextPositionSequence nextLineWord;
        while (lineIterator.hasNext()) {
            currentLineWord = lineIterator.next().getFirstWord();
            if (lineIterator.hasNext()) {
                nextLineWord = lineIterator.next().getLastWord();
                lineIterator.previous();
            } else {
                nextLineWord = currentLineWord;
            }
            if (nextLineWord.getPageIndex() == currentLineWord.getPageIndex()) {
                List<TextPosition> textPositions = new ArrayList<>();
                textPositions.add(currentLineWord.getFirstTextPosition());
                textPositions.add(nextLineWord.getLastTextPosition());
                linespacings.add(new TextPositionSequence(textPositions, currentLineWord.getPageIndex()));
            }
        }
        return linespacings;
    }

    private List<? extends Finding> generateTextFindings(List<TextPositionSequence> textPositions) {
        List<TextFinding> textFindings = new ArrayList<>();
        textPositions.stream()
                .forEach(textPosition -> textFindings.add(new TextFinding(textPosition, TextFindingType.LINE_SPACING)));
        return textFindings;
    }

    @Override
    public String getUIName() {
        return LineSpacingAnalyzer.class.getSimpleName();
    }

}
