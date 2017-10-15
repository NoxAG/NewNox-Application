package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.List;

public interface PDFObject {
    public TextPositionSequence getTextPositionSequence();

    public List<TextPositionSequence> getWords();

}
