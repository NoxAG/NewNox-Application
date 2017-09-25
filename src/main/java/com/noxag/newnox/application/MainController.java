package com.noxag.newnox.application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.Textanalyzer;
import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.algorithms.BadWordingAnalyzer;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textlogic.ChartGenerator;
import com.noxag.newnox.textlogic.PDFHighlighter;
import com.noxag.newnox.ui.pdfmodule.PDFPageDrawer;

public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private PDDocument pdfDoc;
    private HashMap<String, TextanalyzerAlgorithm> allTextanalyzerAlgorithms;
    private Consumer<List<BufferedImage>> updatePDFViewCallbacks;
    private Consumer<List<BufferedImage>> updateStatisticViewCallbacks;

    public MainController() {
        initTextanalyzerAlgorithms();
    }

    public void openPDFDocument(String path) {
        this.pdfDoc = readPDFFromFile(path);
        triggerPDFViewUpdateEvent(renderPDFImages());
    }

    public void analyzePDFDocument(List<String> textAnalyzerUINames) {
        Textanalyzer textanalyzer = new Textanalyzer(
                getRunMethodesFromAlgorithms(getTextanalyzerAlgorithmFromName(textAnalyzerUINames)));
        List<Finding> findings = textanalyzer.analyze(this.pdfDoc);

        List<StatisticFinding> statisticFindings = getFindingsOfSubInstances(findings, StatisticFinding.class);
        List<TextFinding> textFindings = getFindingsOfSubInstances(findings, TextFinding.class);

        PDFHighlighter.highlight(this.pdfDoc, textFindings);

        triggerPDFViewUpdateEvent(renderPDFImages());
        triggerStatisticViewUpdateEvent(ChartGenerator.generateChartImages(statisticFindings));
    }

    public void registerPDFViewUpdateEvent(Consumer<List<BufferedImage>> updatePDFViewCallbacks) {
        this.updatePDFViewCallbacks = updatePDFViewCallbacks;
    }

    public void registerStatisticViewUpdateEvent(Consumer<List<BufferedImage>> updateStatisticViewCallbacks) {
        this.updateStatisticViewCallbacks = updateStatisticViewCallbacks;
    }

    public void triggerPDFViewUpdateEvent(List<BufferedImage> pdfImages) {
        updatePDFViewCallbacks.accept(pdfImages);
    }

    public void triggerStatisticViewUpdateEvent(List<BufferedImage> statisticImages) {
        updateStatisticViewCallbacks.accept(statisticImages);
    }

    public List<String> getTextanayzerUINames() {
        return new ArrayList<String>(this.allTextanalyzerAlgorithms.keySet());
    }

    private PDDocument readPDFFromFile(String path) {
        try {
            if (this.pdfDoc != null) {
                this.pdfDoc.close();
            }
            return PDDocument.load(new File(path));
        } catch (IOException e) {
            // TODO: add proper error propagation and feedback to user
            LOGGER.log(Level.WARNING, "PDF document could not be loaded", e);
            return null;
        }
    }

    private List<BufferedImage> renderPDFImages() {
        return PDFPageDrawer.getAllPagesFromPDFAsImage(this.pdfDoc);
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

    private void initTextanalyzerAlgorithms() {
        allTextanalyzerAlgorithms = new HashMap<>();
        allTextanalyzerAlgorithms.put(BadWordingAnalyzer.getUIName(), new BadWordingAnalyzer());
    }

}
