/**
 * 
 */
package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding;
import com.noxag.newnox.textanalyzer.data.StatisticFinding.StatisticFindingType;
import com.noxag.newnox.textanalyzer.data.StatisticFindingData;
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

/**
 * This class produces a statistic to show which words have been used often
 * 
 * @author Tobias.Schmidt@de.ibm.com, Lars.Dittert@de.ibm.com
 *
 */
public class VocabularyDistributionAnalyzer implements TextanalyzerAlgorithm {

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        try {
            String document = PDFTextExtractionUtil.runTextStripper(doc);
            Map<String, Long> matches = generateStatisticFinding(splitStringIntoWordsAndPutIntoList(document));
            findings.add(generateStatisticFinding(matches));
            return findings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUIName() {
        return VocabularyDistributionAnalyzer.class.getSimpleName();
    }

    private <T extends Finding> T generateStatisticFinding(Map<String, Long> matches) {
        List<StatisticFindingData> data = new ArrayList<>();
        matches.entrySet().stream()
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));
        return (T) new StatisticFinding(StatisticFindingType.WORDING, data);
    }

    public Map<String, Long> splitStringIntoWordsAndPutIntoList(String document) {
        List<String> ListWithoutPunctuationCharacterIgnoreCase = new ArrayList<>();
        String[] parts = document.split(" ");

        for (int i = 0; i < parts.length; i++) {
            ListWithoutPunctuationCharacterIgnoreCase.add(parts[i].replaceAll("[^\\p{Alpha}]+", "").toLowerCase());
        }

        Map<String, Long> matchesGroupedByName = ListWithoutPunctuationCharacterIgnoreCase.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return matchesGroupedByName;
    }

}
