package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
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

/**
 * Algorithm for finding referenced 'Sources' without an entry in Bibliography
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */
public class BibliographyAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(CommonAbbreviationAnalyzer.class.getName());
    private static final String BIBLIOGRAPHY_IDENTIFICATION_LIST_PATH = "src/main/resources/analyzer-conf/bibliography-identifications.csv";

    private List<String> bibliographyHints;

    public BibliographyAnalyzer() {
        bibliographyHints = readBibliographyIdentificationListFile(BIBLIOGRAPHY_IDENTIFICATION_LIST_PATH);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.extractText(doc);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not strip text from document", e);
        }
        findings.addAll(getReferencesWithoutBibliographyEntry(doc, pages));
        findings.addAll(getReferencedBibliographyEntries(doc, pages));
        return findings;
    }

    private List<Finding> getReferencesWithoutBibliographyEntry(PDDocument doc, List<PDFPage> pages) {
        return compareReferenceWithBibliographyEntries(doc, pages,
                this::checkIfReferencesAreMarkedInBibliographyReferences);
    }

    private List<Finding> getReferencedBibliographyEntries(PDDocument doc, List<PDFPage> pages) {
        return compareReferenceWithBibliographyEntries(doc, pages, this::checkIfBibliographyEntriesHaveReferences);
    }

    private List<Finding> compareReferenceWithBibliographyEntries(PDDocument doc, List<PDFPage> pages,
            BiFunction<List<TextPositionSequence>, List<TextPositionSequence>, List<Finding>> method) {
        List<Finding> findings = new ArrayList<>();
        pages.stream().forEach(page -> {
            TextPositionSequence firstWordOfPage = page.getFirstWord();
            if (bibliographyHints.stream().anyMatch(firstWordOfPage.toString().toLowerCase()::contains)) {
                List<PDFPage> contentPages = getAllInTextReferences(pages, firstWordOfPage.getPageIndex());
                List<PDFPage> bibliographyPages = getAllBibliographyEntries(pages, firstWordOfPage.getPageIndex());
                List<TextPositionSequence> bibliographyReferences = getBibliographyReference(contentPages);
                List<TextPositionSequence> bibliographyEntries = getBibliographyReference(bibliographyPages);
                findings.addAll(method.apply(bibliographyReferences, bibliographyEntries));
            }
        });
        return findings;
    }

    private List<PDFPage> getAllInTextReferences(List<PDFPage> pages, int bibliographyPageIndex) {
        return pages.stream().filter(page -> page.getPageIndex() < bibliographyPageIndex).collect(Collectors.toList());
    }

    private List<PDFPage> getAllBibliographyEntries(List<PDFPage> pages, int bibliographyPageIndex) {
        return pages.stream().filter(page -> page.getPageIndex() >= bibliographyPageIndex).collect(Collectors.toList());
    }

    // Reads all references out of given list of PDFPages
    private List<TextPositionSequence> getBibliographyReference(List<PDFPage> pageList) {
        List<TextPositionSequence> wordsContainedInPageList = PDFTextExtractionUtil.extractWords(pageList);
        List<TextPositionSequence> foundReferences = new ArrayList<>();
        wordsContainedInPageList.stream().filter(word -> containsBibliographyReference(word.toString()))
                .forEach(word -> {
                    foundReferences.add(word);
                });
        return foundReferences;
    }

    private List<Finding> checkIfReferencesAreMarkedInBibliographyReferences(
            List<TextPositionSequence> bibliographyReferences, List<TextPositionSequence> bibliographyEntries) {
        List<Finding> findings = new ArrayList<>();
        bibliographyReferences.stream().forEach(bibliographyReference -> {
            List<String> references = readReferenceOutOfWord(bibliographyReference);
            references.stream().forEach(reference -> {
                if (!bibliographyEntries.toString().contains(reference)) {
                    findings.add(new TextFinding(bibliographyReference, TextFindingType.BIBLIOGRAPHY));
                }
            });
        });
        return findings;
    }

    private List<Finding> checkIfBibliographyEntriesHaveReferences(List<TextPositionSequence> bibliographyReferences,
            List<TextPositionSequence> bibliographyEntries) {
        List<Finding> findings = new ArrayList<>();
        bibliographyEntries.stream().forEach(bibliographyEntry -> {
            List<String> entries = readReferenceOutOfWord(bibliographyEntry);
            entries.stream().forEach(entry -> {
                if (bibliographyReferences.toString().contains(entry)) {
                    findings.add(new TextFinding(bibliographyEntry, TextFindingType.POSITIVE_BIBLIOGRAPHY));
                }
            });
        });
        return findings;
    }

    private List<String> readReferenceOutOfWord(TextPositionSequence wordWithReference) {
        List<String> bibliographyReferences = new ArrayList<>();
        String referenceString = wordWithReference.toString();
        while (containsBibliographyReference(referenceString)) {
            int startIndex = referenceString.toString().indexOf("[");
            int endIndex = referenceString.toString().indexOf("]") + 1;
            bibliographyReferences.add(referenceString.substring(startIndex, endIndex));

            referenceString = referenceString.substring(endIndex, referenceString.length());
        }
        return bibliographyReferences;
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

    private boolean containsBibliographyReference(String string) {
        return string.matches(".*\\[(\\d+)\\].*");
    }

    @Override
    public String getUIName() {
        return "Bibliography Analyzer";
    }

}
