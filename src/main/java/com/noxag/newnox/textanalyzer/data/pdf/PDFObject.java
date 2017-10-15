package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

public interface PDFObject {
    public TextPositionSequence getTextPositionSequence();

    public List<TextPositionSequence> getWords();

    public static <T extends PDFObject> List<TextPositionSequence> getWords(List<T> pdfObjects) {
        List<TextPositionSequence> words = new ArrayList<>();
        pdfObjects.stream().forEach(pdfObject -> words.addAll(pdfObject.getWords()));
        return words;
    }
}
