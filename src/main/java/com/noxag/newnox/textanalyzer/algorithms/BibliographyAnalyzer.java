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
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding.TextFindingType;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

public class BibliographyAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static final String BIBLIOGRAPHY_IDENTIFICATION_LIST_PATH = "src/main/resources/analyzer-conf/bibliography-identifications.csv";

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        try {
            findings.addAll(getReferencesWithoutSource(doc));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        return findings;
    }

    private List<Finding> getReferencesWithoutSource(PDDocument doc) throws IOException {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = PDFTextExtractionUtil.extractText(doc);
        List<String> bibliographyHints = readBibliographyIdentificationListFile(BIBLIOGRAPHY_IDENTIFICATION_LIST_PATH);
        pages.stream().forEach(page -> {
            TextPositionSequence firstWordOfPage = page.getFirstWord();
            if (bibliographyHints.stream().anyMatch(firstWordOfPage.toString().toLowerCase()::contains)) {
                List<TextPositionSequence> markedSources = getAllSources(pages, firstWordOfPage.getPageIndex());
                List<TextPositionSequence> referencedSources = getAllReferences(pages, firstWordOfPage.getPageIndex());
                findings.addAll(compareReferencesWithSources(referencedSources, markedSources));
            }
        });
        return findings;
    }

    // Gets all references to sources of content pages
    private List<TextPositionSequence> getAllReferences(List<PDFPage> pages, int sourcePageIndex) {
        List<PDFPage> contentPages = pages.stream().filter(page -> page.getPageIndex() < sourcePageIndex)
                .collect(Collectors.toList());
        return getSourceList(contentPages);
    }

    // Gets all sources in bibliography
    private List<TextPositionSequence> getAllSources(List<PDFPage> pages, int sourcePageIndex) {
        List<PDFPage> sourcePages = pages.stream().filter(page -> page.getPageIndex() >= sourcePageIndex)
                .collect(Collectors.toList());
        return getSourceList(sourcePages);
    }

    // Reads all sources/references out of given list of PDFPages
    private List<TextPositionSequence> getSourceList(List<PDFPage> pageList) {
        List<TextPositionSequence> wordsOfBibliography = PDFTextExtractionUtil.extractWords(pageList);
        List<TextPositionSequence> markedSources = new ArrayList<>();
        wordsOfBibliography.stream().forEach(word -> {
            if (word.toString().contains("[") && word.toString().contains("]")) {
                markedSources.add(word);
            }
        });
        return markedSources;
    }

    // Compares, if reference has been stored in bibliography
    private List<Finding> compareReferencesWithSources(List<TextPositionSequence> referencedSources,
            List<TextPositionSequence> markedSources) {
        List<Finding> findings = new ArrayList<>();
        referencedSources.stream().forEach(referencedSource -> {
            // Reads reference part out of whole word
            String reference = referencedSource.toString().substring(referencedSource.toString().indexOf("["),
                    referencedSource.toString().indexOf("]") + 1);
            if (!markedSources.toString().contains(reference)) {
                findings.add(new TextFinding(referencedSource, TextFindingType.BIBLIOGRAPHY));
            }
        });
        return findings;
    }

    private List<String> readBibliographyIdentificationListFile(String bibliographyIdentificationListPath) {
        List<String> bibliographyIdentificationList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(bibliographyIdentificationListPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).forEach(identification -> {
                    bibliographyIdentificationList.add(identification.trim().toLowerCase());
                });
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return bibliographyIdentificationList;
    }

    @Override
    public String getUIName() {
        return BibliographyAnalyzer.class.getSimpleName();
    }

}
