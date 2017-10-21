package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * This class produces a commentary finding to show if the "Akademische
 * Aufrichtigkeitserklaerung" exists.
 * 
 * @author Lars.Dittert@de.ibm.com
 *
 */

public class AkademischeAufrichtigkeitserklaerung implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(AkademischeAufrichtigkeitserklaerung.class.getName());
    private static final String AUFRICHTIGKEITSERKLAERUNG_IDENTIFICATION_LIST_PATH = "src/main/resources/analyzer-conf/aufrichtigkeitserklaerung-hints.csv";
    private List<String> aufrichtigkeitserklaerungHints;

    public AkademischeAufrichtigkeitserklaerung() {
        aufrichtigkeitserklaerungHints = readAufrichtigkeitserklaerungIdentificationListFile(
                AUFRICHTIGKEITSERKLAERUNG_IDENTIFICATION_LIST_PATH);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        CommentaryFinding commentaryFinding;
        if (compareString(generateLowerCaseString(getNotContentPages(doc)))) {
            commentaryFinding = new CommentaryFinding("Aufrichtigkeitserklärung found", "",
                    getPageNumberWhereAufrichtigkeitserklaerung(getNotContentPages(doc)), 0);
            findings.add(commentaryFinding);
            return findings;
        }
        commentaryFinding = new CommentaryFinding("Aufrichtigkeitserklaerung not found", "", 0, 0);
        findings.add(commentaryFinding);
        return findings;
    }

    public static List<PDFPage> getNotContentPages(PDDocument doc) {
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages.stream().filter(page -> !page.isContentPage()).collect(Collectors.toList());
    }

    public static int getPageNumberWhereAufrichtigkeitserklaerung(List<PDFPage> pages) {
        PDFPage PageNumberWhereAufrichtigkeitserklaerung = pages.get(0);
        return PageNumberWhereAufrichtigkeitserklaerung.getPageIndex();
    }

    public List<String> generateLowerCaseString(List<PDFPage> pages) {
        List<TextPositionSequence> words = new ArrayList<>();
        words = PDFTextExtractionUtil.extractWords(pages);

        return words.stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word))
                .map(TextPositionSequence::toString).map(String::toLowerCase).collect(Collectors.toList());
    }

    public boolean compareString(List<String> words) {
        if (aufrichtigkeitserklaerungHints.stream().anyMatch(words::contains)) {
            return true;
        }
        return false;
    }

    private List<String> readAufrichtigkeitserklaerungIdentificationListFile(
            String aufrichtigkeitserklaerungIdentificationListPath) {
        List<String> aufrichtigkeitserklaerungIdentificationList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(aufrichtigkeitserklaerungIdentificationListPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).forEach(identification -> {
                    aufrichtigkeitserklaerungIdentificationList.add(identification.trim().toLowerCase());
                });
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return aufrichtigkeitserklaerungIdentificationList;
    }

    @Override
    public String getUIName() {
        return "Check for Aufrichtigkeitserklärung";
    }

}