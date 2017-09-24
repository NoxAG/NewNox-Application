package com.noxag.newnox.textanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.algorithms.BadWordingAnalyzer;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding;

public class TextlogicFacade {
    HashMap<String, TextanalyzerAlgorithm> allTextanalyzerAlgorithms;

    public TextlogicFacade() {
        initTextanalyzer();
    }

    public List<String> getTextanayzerUINames() {
        return new ArrayList<String>(this.allTextanalyzerAlgorithms.keySet());
    }

    public void analyzeAction(PDDocument pdfDoc, List<String> textAnalyzerUINames) {
        Textanalyzer textanalyzer = new Textanalyzer(
                getRunMethodesFromAlgorithms(getTextanalyzerAlgorithmFromName(textAnalyzerUINames)));
        List<Finding> findings = textanalyzer.analyze(pdfDoc);

        List<StatisticFinding> statisticFindings = getFindingsOfSubInstances(findings, StatisticFinding.class);
        List<TextFinding> textFindings = getFindingsOfSubInstances(findings, TextFinding.class);

        TextFindingProcessor textFindingProcessor = new TextFindingProcessor();
    }

    private <S extends Finding> List<S> getFindingsOfSubInstances(List<Finding> findings, Class<S> childClass) {
        return findings.stream().filter(childClass::isInstance).map(childClass::cast).collect(Collectors.toList());
    }

    private List<TextanalyzerAlgorithm> getTextanalyzerAlgorithmFromName(List<String> uiNames) {
        return uiNames.stream().reduce(new ArrayList<TextanalyzerAlgorithm>(), (algorithms, uiName) -> {
            algorithms.add(this.allTextanalyzerAlgorithms.get(uiName));
            return algorithms;
        }, (algorithm1, algorithm2) -> {
            algorithm1.addAll(algorithm2);
            return algorithm1;
        });
    }

    private List<Function<PDDocument, List<Finding>>> getRunMethodesFromAlgorithms(
            List<TextanalyzerAlgorithm> algorithms) {
        return algorithms.stream().reduce(new ArrayList<Function<PDDocument, List<Finding>>>(),
                (runMethodes, algorithm) -> {
                    runMethodes.add(getRunMethodeFromAlgorithm(algorithm));
                    return runMethodes;
                }, (runMethodes, missingRunMethodes) -> {
                    runMethodes.addAll(missingRunMethodes);
                    return runMethodes;
                });
    }

    private Function<PDDocument, List<Finding>> getRunMethodeFromAlgorithm(TextanalyzerAlgorithm algorithm) {
        return algorithm::run;
    }

    private void initTextanalyzer() {
        allTextanalyzerAlgorithms = new HashMap<>();
        allTextanalyzerAlgorithms.put(BadWordingAnalyzer.getUIName(), new BadWordingAnalyzer());
    }

}
