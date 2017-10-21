package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.noxag.newnox.textanalyzer.util.PDFTextExtractionUtil;

/**
 * This class produces a statistic to show which punctuation marks have been
 * used often
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PunctuationDistributionAnalyzer implements TextanalyzerAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(PunctuationDistributionAnalyzer.class.getName());

    @Override
    public List<Finding> run(PDDocument doc) {
        List<Finding> findings = new ArrayList<>();
        List<PDFPage> pages = new ArrayList<>();
        try {
            pages = PDFTextExtractionUtil.reduceToContent(PDFTextExtractionUtil.extractText(doc));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not extract text from document", e);
        }
        List<TextPositionSequence> punctuationMarks = findMatches(pages);
        findings.add(generateStatisticFinding(punctuationMarks));
        return findings;
    }

    private List<TextPositionSequence> findMatches(List<PDFPage> pages) {
        List<TextPositionSequence> words = PDFTextExtractionUtil.extractWords(pages);
        return words.stream().filter(TextPositionSequence::isPunctuationMark).collect(Collectors.toList());
    }

    private Finding generateStatisticFinding(List<TextPositionSequence> matches) {
        List<StatisticFindingData> data = new ArrayList<>();

        List<String> matchesAsPunctionNames = matches.stream().map(TextPositionSequence::toString)
                .map(this::toNameOfPunctuationMark).collect(Collectors.toList());

        Map<String, Long> matchesGroupedByName = matchesAsPunctionNames.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        matchesGroupedByName.entrySet().stream()
                .forEachOrdered(entry -> data.add(new StatisticFindingData(entry.getKey(), entry.getValue())));

        return new StatisticFinding(StatisticFindingType.PUNCTUATION_DISTRIBUTION, data);
    }

    private String toNameOfPunctuationMark(String mark) {
        switch (mark) {
        case ".":
            return "periods";
        case "?":
            return "question marks";
        case "!":
            return "exclamation points";
        case ",":
            return "commas";
        case ";":
            return "semicolon";
        case ":":
            return "colon";
        case "(":
            return "braces";
        case ")":
            return "braces";
        case "-":
            return "dash";
        case "–":
            return "dash";
        case "—":
            return "dash";
        case "\"":
            return "quotation Marks";
        case "/":
            return "slash";
        case "'":
            return "apostrophe";
        default:
            return mark;
        }
    }

    @Override
    public String getUIName() {
        return "Compare punctuation character";
    }

}
