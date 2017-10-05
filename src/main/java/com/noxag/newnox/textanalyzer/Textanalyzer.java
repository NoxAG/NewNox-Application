package com.noxag.newnox.textanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.data.Finding;

/**
 * This class is used to run a set of {@link TextanalyzerAlgorithm}
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class Textanalyzer {
    List<Function<PDDocument, List<Finding>>> algorithms;

    public Textanalyzer(List<Function<PDDocument, List<Finding>>> algorithms) {
        this.algorithms = algorithms;
    }

    /**
     * This method runs all {@link TextanalyzerAlgorithm} contained in this
     * class and merges the results of all analysis
     * 
     * @param pdfDoc
     *            the PDF document to be analyzed
     * @return the results of all analysis
     */
    public List<Finding> analyze(PDDocument pdfDoc) {
        List<Finding> findings = new ArrayList<>();
        algorithms.stream().forEach(algorithm -> findings.addAll((algorithm.apply(pdfDoc))));
        return findings;
    }
}