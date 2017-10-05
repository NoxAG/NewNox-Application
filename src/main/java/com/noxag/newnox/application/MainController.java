package com.noxag.newnox.application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
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

/**
 * This class handles inputs of the userinterface via an event listener
 * interface and serves as mediator between the userinterface and the text
 * processing
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private PDDocument pdfDoc;
    private HashMap<String, TextanalyzerAlgorithm> allTextanalyzerAlgorithms;
    private Consumer<List<BufferedImage>> updatePDFViewCallbacks;
    private Consumer<List<BufferedImage>> updateStatisticViewCallbacks;

    public MainController() {
        initTextanalyzerAlgorithms();
    }

    /**
     * This method reads a PDF document from a file and generates images of this
     * PDF to be display in the userinterface
     * 
     * @param path
     *            the path to the file that is supposed to be opened
     */
    public void openPDFDocument(String path) {
        this.pdfDoc = readPDFFromFile(path);
        triggerPDFViewUpdateEvent(renderPDFImages());
    }

    /**
     * This method analyzes the PDF document and processes the results so they
     * can be displayed in the userinterface
     * 
     * @param textAnalyzerUINames
     *            the textanalyzer algorithms to be run referenced by name
     */
    public void analyzePDFDocument(List<String> textAnalyzerUINames) {
        Textanalyzer textanalyzer = new Textanalyzer(getTextanalyzerAlgorithmFromName(textAnalyzerUINames));
        List<Finding> findings = textanalyzer.analyze(this.pdfDoc);

        List<StatisticFinding> statisticFindings = getFindingsOfSubInstances(findings, StatisticFinding.class);
        List<TextFinding> textFindings = getFindingsOfSubInstances(findings, TextFinding.class);

        PDFHighlighter.highlight(this.pdfDoc, textFindings);

        triggerPDFViewUpdateEvent(renderPDFImages());
        triggerStatisticViewUpdateEvent(ChartGenerator.generateChartImages(statisticFindings));
    }

    /**
     * Registers the "PDFViewUUpdate" event
     * 
     * <p>
     * Calling this method twice will override the previous event callback
     * </p>
     * 
     * @param updatePDFViewCallbacks
     *            the method to be called when the "PDFViewUUpdate" event is
     *            triggered
     */
    public void registerPDFViewUpdateEvent(Consumer<List<BufferedImage>> updatePDFViewCallbacks) {
        this.updatePDFViewCallbacks = updatePDFViewCallbacks;
    }

    /**
     * Registers the "StatisticViewUpdate" event
     * 
     * <p>
     * Calling this method twice will override the previous event callback
     * </p>
     * 
     * @param updateStatisticViewCallbacks
     *            the method to be called when the "StatisticViewUpdate" event
     *            is triggered
     */
    public void registerStatisticViewUpdateEvent(Consumer<List<BufferedImage>> updateStatisticViewCallbacks) {
        this.updateStatisticViewCallbacks = updateStatisticViewCallbacks;
    }

    /**
     * Triggers the "PDFViewUUpdate" event
     * 
     * @param pdfImages
     *            the pdf images that should be displayed in the userinterface
     */
    public void triggerPDFViewUpdateEvent(List<BufferedImage> pdfImages) {
        updatePDFViewCallbacks.accept(pdfImages);
    }

    /**
     * Triggers the "StatisticViewUpdate" event
     * 
     * @param statisticImages
     *            the statistic images that should be displayed in the
     *            userinterface
     */
    public void triggerStatisticViewUpdateEvent(List<BufferedImage> statisticImages) {
        updateStatisticViewCallbacks.accept(statisticImages);
    }

    /**
     * This method returns a list of the UINames of all textanalyzeralgorithms
     * that have been registered to the controller
     * 
     * @return UINames of all textanalyzeralgorithms
     */
    public List<String> getTextanalyzerUINames() {
        return new ArrayList<String>(this.allTextanalyzerAlgorithms.keySet());
    }

    private PDDocument readPDFFromFile(String path) {
        try {
            closePDF();

        } catch (IOException e) {
            // TODO: add proper error propagation and feedback to user
            // We may need to add an addition event for this
            LOGGER.log(Level.WARNING, "PDF document could not be closed", e);
            return null;
        }
        try {
            return PDDocument.load(new File(path));
        } catch (IOException e) {
            // TODO: error propagation
            LOGGER.log(Level.WARNING, "PDF document could not be loaded", e);
            return null;
        }

    }

    private void closePDF() throws IOException {
        if (this.pdfDoc != null) {
            this.pdfDoc.close();
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

    private void initTextanalyzerAlgorithms() {
        allTextanalyzerAlgorithms = new HashMap<>();
        allTextanalyzerAlgorithms.put(BadWordingAnalyzer.getUIName(), new BadWordingAnalyzer());
    }

    @Override
    protected void finalize() throws Throwable {
        closePDF();
    }

}
