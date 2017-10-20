package com.noxag.newnox.application;

import static com.noxag.newnox.ui.pdfmodule.renderer.PDFPageRenderer.renderPDFTextOverlay;
import static com.noxag.newnox.ui.pdfmodule.renderer.PDFPageRenderer.renderTextMarkupOverlay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.Textanalyzer;
import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.algorithms.BibliographyAnalyzer;
import com.noxag.newnox.textanalyzer.algorithms.CommonAbbreviationAnalyzer;
import com.noxag.newnox.textanalyzer.algorithms.FontAnalyzer;
import com.noxag.newnox.textanalyzer.algorithms.SentenceComplexityAnalyzer;
import com.noxag.newnox.textanalyzer.algorithms.VocabularyDistributionAnalyzer;
import com.noxag.newnox.textanalyzer.algorithms.WordingAnalyzer;
import com.noxag.newnox.textanalyzer.data.CommentaryFinding;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.TextFinding;
import com.noxag.newnox.textlogic.ChartGenerator;
import com.noxag.newnox.textlogic.PDFTextMarker;

import javafx.scene.chart.BarChart;

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
    private List<TextanalyzerAlgorithm> textanalyzerAlgorithms;
    private List<TextanalyzerAlgorithm> statisticanalyzerAlgorithms;
    private Consumer<List<BufferedImage>> updatePDFImagesCallback;
    private BiConsumer<List<BarChart<String, Number>>, List<CommentaryFinding>> updateStatisticViewCallback;
    private Consumer<List<BufferedImage>> updateTextMarkupImagesCallback;
    private Consumer<String> alertPopupCallback;

    private static final String ERROR_MESSAGE_TEXT_COULD_NOT_BE_MARKED = "PDF Text could not be markered";
    private static final String ERROR_MESSAGE_PDF_COULD_NOT_BE_CLOSED = "PDF document could not be closed";

    public MainController() {
        initTextanalyzerAlgorithms();
        initStatisticanalyzerAlgorithms();
    }

    /**
     * This method reads a PDF document from a file and generates images of this
     * PDF to be display in the userinterface
     * 
     * @param path
     *            the path to the file that is supposed to be opened
     */
    public void openPDFDocument(File file) {
        if (file == null) {
            this.triggerAlertPopupEvent(
                    "PDF konnte nicht geladen werden. Möglicherweise ist es in einer anderen Anwendung geöffnet");
        } else {
            this.pdfDoc = readPDFFromFile(file);
            triggerPDFImagesUpdateEvent(renderPDFTextOverlay(pdfDoc));
        }

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
        List<CommentaryFinding> commentaryFinding = getFindingsOfSubInstances(findings, CommentaryFinding.class);

        try {
            PDFTextMarker.addTextMarkups(this.pdfDoc, textFindings);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, ERROR_MESSAGE_TEXT_COULD_NOT_BE_MARKED, e);
            this.triggerAlertPopupEvent(ERROR_MESSAGE_TEXT_COULD_NOT_BE_MARKED);
        }

        this.triggerTextMarkupImagesUpdateEvent(renderTextMarkupOverlay(this.pdfDoc));
        this.triggerStatisticViewUpdateEvent(ChartGenerator.generateBarCharts(statisticFindings), commentaryFinding);
    }

    /**
     * Registers the "PDFViewUpdate" event
     * 
     * <p>
     * Calling this method twice will override the previous event callback
     * </p>
     * 
     * @param updatePDFViewCallbacks
     *            the method to be called when the "PDFViewUUpdate" event is
     *            triggered
     */
    public void registerPDFImagesUpdateEvent(Consumer<List<BufferedImage>> updatePDFImagesCallback) {
        this.updatePDFImagesCallback = updatePDFImagesCallback;
    }

    public void registerTextMarkupImagesUpdateEvent(Consumer<List<BufferedImage>> updateTextMarkupImagesCallback) {
        this.updateTextMarkupImagesCallback = updateTextMarkupImagesCallback;
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
    public void registerStatisticViewUpdateEvent(
            BiConsumer<List<BarChart<String, Number>>, List<CommentaryFinding>> updateStatisticViewCallback) {
        this.updateStatisticViewCallback = updateStatisticViewCallback;
    }

    public void registerAlertPopupEvent(Consumer<String> alertPopupCallback) {
        this.alertPopupCallback = alertPopupCallback;
    }

    /**
     * Triggers the "PDFViewUUpdate" event
     * 
     * @param pdfImages
     *            the pdf images that should be displayed in the userinterface
     */
    public void triggerPDFImagesUpdateEvent(List<BufferedImage> pdfImages) {
        updatePDFImagesCallback.accept(pdfImages);
    }

    public void triggerTextMarkupImagesUpdateEvent(List<BufferedImage> textMarkupImages) {
        updateTextMarkupImagesCallback.accept(textMarkupImages);
    }

    /**
     * Triggers the "StatisticViewUpdate" event
     * 
     * @param statisticImages
     *            the statistic images that should be displayed in the
     *            userinterface
     */
    public void triggerStatisticViewUpdateEvent(List<BarChart<String, Number>> charts,
            List<CommentaryFinding> comment) {
        updateStatisticViewCallback.accept(charts, comment);
    }

    public void triggerAlertPopupEvent(String alertMessage) {
        alertPopupCallback.accept(alertMessage);
    }

    /**
     * This method returns a list of the UINames of all textanalyzeralgorithms
     * that have been registered to the controller
     * 
     * @return UINames of all textanalyzeralgorithms
     */
    public List<String> getTextanalyzerUINames() {
        return getAnalyzerUINames(this.textanalyzerAlgorithms);
    }

    public List<String> getStatisticanalyzerUINames() {
        return getAnalyzerUINames(this.statisticanalyzerAlgorithms);
    }

    private List<String> getAnalyzerUINames(List<TextanalyzerAlgorithm> algorithms) {
        List<String> uiNames = new ArrayList<>();
        algorithms.stream().forEach(analyzer -> uiNames.add(analyzer.getUIName()));
        return uiNames;
    }

    private PDDocument readPDFFromFile(File file) {
        try {
            closePDF();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, ERROR_MESSAGE_PDF_COULD_NOT_BE_CLOSED, e);
            this.triggerAlertPopupEvent(ERROR_MESSAGE_PDF_COULD_NOT_BE_CLOSED);
            return null;
        }
        try {
            return PDDocument.load(file);
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

    private <S extends Finding> List<S> getFindingsOfSubInstances(List<Finding> findings, Class<S> childClass) {
        return findings.stream().filter(childClass::isInstance).map(childClass::cast).collect(Collectors.toList());
    }

    private List<TextanalyzerAlgorithm> getTextanalyzerAlgorithmFromName(List<String> uiNames) {
        List<TextanalyzerAlgorithm> matchingTextanalyzer = getAlgorithmWithMatchingNames(this.textanalyzerAlgorithms,
                uiNames);
        List<TextanalyzerAlgorithm> matchingStatisticanalyzer = getAlgorithmWithMatchingNames(
                this.statisticanalyzerAlgorithms, uiNames);
        matchingTextanalyzer.addAll(matchingStatisticanalyzer);
        return matchingTextanalyzer;
    }

    private List<TextanalyzerAlgorithm> getAlgorithmWithMatchingNames(List<TextanalyzerAlgorithm> algorithms,
            List<String> uiNames) {
        return algorithms.stream().filter(analyzer -> uiNames.contains(analyzer.getUIName()))
                .collect(Collectors.toList());
    }

    private void initTextanalyzerAlgorithms() {
        this.textanalyzerAlgorithms = new ArrayList<>();
        this.textanalyzerAlgorithms.add(new WordingAnalyzer());
        this.textanalyzerAlgorithms.add(new SentenceComplexityAnalyzer());
        this.textanalyzerAlgorithms.add(new BibliographyAnalyzer());
        this.textanalyzerAlgorithms.add(new FontAnalyzer());
    }

    private void initStatisticanalyzerAlgorithms() {
        this.statisticanalyzerAlgorithms = new ArrayList<>();
        this.statisticanalyzerAlgorithms.add(new VocabularyDistributionAnalyzer());
        this.statisticanalyzerAlgorithms.add(new CommonAbbreviationAnalyzer());
    }

    @Override
    protected void finalize() throws Throwable {
        closePDF();
    }

}
