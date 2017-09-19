package com.noxag.newnox.textanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.data.Finding;

public class Textanalyzer {
    List<Function<PDDocument, List<Finding>>> algorithms;

    public Textanalyzer(List<Function<PDDocument, List<Finding>>> algorithms) {
        this.algorithms = algorithms;
    }

    public List<Finding> analyze(PDDocument pdfDoc) {
        List<Finding> findings = new ArrayList<>();
        algorithms.stream().forEach(algorithm -> findings.addAll((algorithm.apply(pdfDoc))));
        return findings;
    }
}