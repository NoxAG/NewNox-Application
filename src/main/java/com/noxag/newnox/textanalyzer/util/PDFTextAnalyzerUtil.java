package com.noxag.newnox.textanalyzer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.noxag.newnox.textanalyzer.data.PDFLine;
import com.noxag.newnox.textanalyzer.data.PDFPage;
import com.noxag.newnox.textanalyzer.data.TextPositionSequence;

public class PDFTextAnalyzerUtil {

    public static List<TextPositionSequence> reduceToWords(List<PDFPage> pages) {
        return pages.stream().reduce(new ArrayList<TextPositionSequence>(), (textPositionList, page) -> {
            textPositionList.addAll(page.getWords());
            return textPositionList;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    public static List<PDFLine> reduceToLines(List<PDFPage> pages) {
        return pages.stream().reduce(new ArrayList<PDFLine>(), (pdfLineList, page) -> {
            pdfLineList.addAll(page.getLines());
            return pdfLineList;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    public static List<TextPositionSequence> reduceToTextPositions(List<PDFLine> lines) {
        return lines.stream().reduce(new ArrayList<TextPositionSequence>(), (textPositionList, line) -> {
            textPositionList.add(line.getTextPositionSequence());
            return textPositionList;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    // @formatter:off
    public static PDFPage generatePDFPage(Integer pageIndex, List<TextPositionSequence> pageWords) {
        return new PDFPage(
                pageWords.stream()
                .collect(Collectors.groupingBy(TextPositionSequence::getY))
                .values().stream()
                .reduce(new ArrayList<PDFLine>(), (lines, words) -> {
                    lines.add(new PDFLine(words));
                    return lines;
                }, (lines1, lines2) -> {
                    lines1.addAll(lines2);  
                    return lines1;
                }).stream()
                .collect(Collectors.toList()));
    }
    // @formatter:on

    public static List<PDFPage> generatePDFPages(List<TextPositionSequence> words) {
        List<PDFPage> pages = new ArrayList<>();
        words.stream().collect(Collectors.groupingBy(TextPositionSequence::getPageIndex)).entrySet().stream()
                .forEach(entry -> pages.add(generatePDFPage(entry.getKey(), entry.getValue())));
        return pages;
    }
}
