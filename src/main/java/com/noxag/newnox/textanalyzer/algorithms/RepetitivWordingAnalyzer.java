package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class RepetitivWordingAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static final int AMOUNT_OF_WORDS_TO_COMPARE = 20;
    private static final int ALLOWED_REPETITIONS_BY_DEFAULT = 2;

    private List<TextPositionSequence> wordInPDF;
    private Map<String, Integer> repetitivWordMap = new HashMap<>();
    private List<TextPositionSequence> actuallyWordBlock = new ArrayList<>();
    private List<Finding> findings = new ArrayList<>();

    @Override
    public List<Finding> run(PDDocument doc) {
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        findings.addAll(getRepetitionsInWordBlock(pages));
        return findings;
    }

    private List<Finding> getRepetitionsInWordBlock(List<PDFPage> pages) {
        wordInPDF = PDFTextExtractionUtil.extractWords(pages);
        List<Finding> findings = new ArrayList<>();

        wordInPDF.stream().forEach(word -> {

            if (actuallyWordBlock.size() < AMOUNT_OF_WORDS_TO_COMPARE) {
                repetitivWordMap.put(word.toString(),
                        (repetitivWordMap.containsKey(word.toString()) ? repetitivWordMap.get(word.toString()) : 0)
                                + 1);
                actuallyWordBlock.add(word);
            } else {
                repetitivWordMap.put(actuallyWordBlock.get(0).toString(),
                        repetitivWordMap.get(actuallyWordBlock.get(0).toString()) - 1);
                if (repetitivWordMap.get(actuallyWordBlock.get(0).toString()) == 0) {
                    repetitivWordMap.remove(actuallyWordBlock.get(0).toString());
                }
                actuallyWordBlock.remove(0);
                repetitivWordMap.put(word.toString(),
                        (repetitivWordMap.containsKey(word.toString()) ? repetitivWordMap.get(word.toString()) : 0)
                                + 1);
                actuallyWordBlock.add(word);
            }

            repetitivWordMap.entrySet().stream().filter(entry -> entry.getValue() > ALLOWED_REPETITIONS_BY_DEFAULT)
                    .forEach(entry -> {
                        System.out.println("test");
                        actuallyWordBlock.stream()
                                .filter(calculatedWords -> calculatedWords.toString().equals(entry.getKey()))
                                .forEach(entryPositionSequence -> {
                                    findings.add(
                                            new TextFinding(entryPositionSequence, TextFindingType.REPETITIV_WORDING));
                                });
                    });
        });
        return findings;
    }

    @Override
    public String getUIName() {
        return RepetitivWordingAnalyzer.class.getSimpleName();
    }

}
