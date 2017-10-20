/**
 * 
 */
package com.noxag.newnox.textanalyzer.algorithms;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textanalyzer.data.pdf.PDFPage;
import com.noxag.newnox.textanalyzer.data.pdf.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.PDFTextAnalyzerUtil;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;
import com.opencsv.CSVReader;

/**
 * This class produces a statistic to show which words have been used often
 * 
 * @author Tobias.Schmidt@de.ibm.com, Lars.Dittert@de.ibm.com
 *
 */
public class VocabularyDistributionAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(WordingAnalyzer.class.getName());
    private static final String VOCABULARY_DISTRIBUTION_EXEPTIONS_PATH = "src/main/resources/analyzer-conf/vocabularydistributionanalyzer-blacklist.csv";
    private List<String> vocabularyDistributionExeptions;

    public VocabularyDistributionAnalyzer() {
        this(VOCABULARY_DISTRIBUTION_EXEPTIONS_PATH);
    }

    public VocabularyDistributionAnalyzer(String vocabularyDistributionExeptionsPath) {
        this.vocabularyDistributionExeptions = readExeptionsFile(vocabularyDistributionExeptionsPath);
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        try {
            // String document = PDFTextExtractionUtil.runTextStripper(doc);
            findings.add(generateStatisticFinding(splitStringIntoWordsAndPutIntoList(doc)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return findings;
    }

    @Override
    public String getUIName() {
        return VocabularyDistributionAnalyzer.class.getSimpleName();
    }

    private <T extends Finding> T generateStatisticFinding(Map<String, Long> map) {
        List<StatisticFindingData> data = new ArrayList<>();

        List<Entry<String, Long>> hashEntries = map.entrySet().stream().collect(Collectors.toList());
        hashEntries.sort(Collections.reverseOrder(Comparator.comparing(Entry::getValue)));

        hashEntries.stream().limit(20)
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));

        return (T) new StatisticFinding(StatisticFindingType.WORDING, data);
    }

    public Map<String, Long> splitStringIntoWordsAndPutIntoList(PDDocument doc) throws IOException {
        List<PDFPage> pages = new ArrayList<>();
        List<TextPositionSequence> words = new ArrayList<>();
        pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
        words = PDFTextExtractionUtil.extractWords(pages);

        List<String> matchesAsString = words.stream().filter(word -> !PDFTextAnalyzerUtil.isPunctuationMark(word))
                .map(TextPositionSequence::toString).map(String::toLowerCase).collect(Collectors.toList());

        Map<String, Long> matchesGroupedByName = matchesAsString.stream()
                .filter(word -> !vocabularyDistributionExeptions.contains(word))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return matchesGroupedByName;
    }

    private List<String> readExeptionsFile(String vocabularyDistributionExeptionsPath) {
        List<String> vocabularyDistributionExeptions = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(vocabularyDistributionExeptionsPath));
            String[] line;
            while ((line = reader.readNext()) != null) {
                Arrays.stream(line).map(String::toLowerCase).forEach(vocabularyDistributionExeptions::add);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Configuration file could not be read", e);
        }
        return vocabularyDistributionExeptions;
    }

}
