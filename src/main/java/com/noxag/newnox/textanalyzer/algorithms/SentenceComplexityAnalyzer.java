package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFLine;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.PDFParagraph;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

public class SentenceComplexityAnalyzer implements TextanalyzerAlgorithm {
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

        return findings;
    }

    private List<PDFParagraph> getSentences(List<PDFPage> pages) {
        List<PDFParagraph> sentences = new ArrayList<>();
        List<TextPositionSequence> words = PDFTextExtractionUtil.extractWords(pages);

        return sentences;
    }

    private List<? extends Finding> generateTextFindings(List<PDFLine> lines) {
        List<TextFinding> textFindings = new ArrayList<>();
        lines.stream().forEach(line -> textFindings
                .add(new TextFinding(line.getTextPositionSequence(), TextFindingType.SENTENCE_COMPLEXITY)));
        return textFindings;
    }

    private <T extends Finding> T generateStatisticFinding(List<TextPositionSequence> matches) {
        List<StatisticFindingData> data = new ArrayList<>();

        return (T) new StatisticFinding(StatisticFindingType.SENTENCE_COMPLEXITY, data);
    }

    @Override
    public String getUIName() {
        return SentenceComplexityAnalyzer.class.getSimpleName();
    }

}
