package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.textanalyzer.data.Finding;
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
        // (String comment,
        // String type, int
        // page, int line)
        try {
            if (compareString(generateLowerCaseString(doc))) {
                // true
                commentaryFinding = new CommentaryFinding("Aufrichtigkeitserklaerung found", "", 0, 0);
                findings.add(commentaryFinding);
                return findings;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // false
        commentaryFinding = new CommentaryFinding("Aufrichtigkeitserklaerung not found", "", 0, 0);
        findings.add(commentaryFinding);
        return findings;
    }

    public String generateLowerCaseString(PDDocument doc) throws IOException {
        return PDFTextExtractionUtil.runTextStripper(doc);
    }

    public boolean compareString(String doc) {
        if (aufrichtigkeitserklaerungHints.stream().anyMatch(doc::contains)) {
            return true;
        }
        return false;
    }
    // durchsuche alle Seite nach aufrichtigkeitserklaerung && !contentpage
    // (methode vorhanden)

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
        return AkademischeAufrichtigkeitserklaerung.class.getSimpleName();
    }

}
