package com.noxag.newnox.textanalyzer.data.pdf;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

/**
 * This class represents a single word
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class TextPositionSequence implements CharSequence, PDFObject {
    final List<TextPosition> textPositions;
    final int start;
    final int end;
    final int pageIndex;
    final boolean hasWordSeperator;

    public TextPositionSequence(List<TextPosition> textPositions, int pageIndex) {
        this(textPositions, 0, textPositions.size() - 1, pageIndex);
    }

    public TextPositionSequence(List<TextPosition> textPositions, int start, int end, int pageIndex) {
        this(textPositions, start, end, pageIndex, false);
    }

    public TextPositionSequence(TextPositionSequence copyMe, boolean hasWordSeperator) {
        this(copyMe.getTextPositions(), copyMe.getStart(), copyMe.getEnd(), copyMe.getPageIndex(), hasWordSeperator);
    }

    public TextPositionSequence(List<TextPosition> textPositions, int start, int end, int pageIndex,
            boolean hasWordSeperator) {
        this.textPositions = textPositions;
        this.start = start;
        this.end = end;
        this.pageIndex = pageIndex;
        this.hasWordSeperator = hasWordSeperator;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        TextPosition textPosition = textPositionAt(index);
        String text = textPosition.getUnicode();
        return text.charAt(0);

    }

    @Override
    public TextPositionSequence subSequence(int start, int end) {
        return new TextPositionSequence(textPositions, this.start + start, this.start + end, this.pageIndex);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(length());
        for (int i = 0; i <= length(); i++) {
            builder.append(charAt(i));
        }
        return builder.toString();
    }

    public TextPosition textPositionAt(int index) {
        return textPositions.get(start + index);
    }

    public float getX() {
        return textPositions.get(start).getXDirAdj();
    }

    public float getY() {
        return textPositions.get(start).getPageHeight() - textPositions.get(start).getYDirAdj();
    }

    public float getWidth() {
        TextPosition first = textPositions.get(start);
        TextPosition last = textPositions.get(end);
        return last.getWidthDirAdj() + last.getXDirAdj() - first.getXDirAdj();
    }

    public float getHeight() {
        TextPosition first = textPositions.get(start);
        return first.getHeightDir() * 1.1f;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public List<TextPosition> getTextPositions() {
        return textPositions;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public TextPosition getFirstTextPosition() {
        return this.getTextPositions().get(0);
    }

    public TextPosition getLastTextPosition() {
        List<TextPosition> positions = this.getTextPositions();
        return positions.get(positions.size() - 1);
    }

    public boolean contains(String sequence) {
        return this.toString().contains(sequence);
    }

    @Override
    public TextPositionSequence getTextPositionSequence() {
        return this;
    }

    @Override
    public List<TextPositionSequence> getWords() {
        ArrayList<TextPositionSequence> res = new ArrayList<TextPositionSequence>();
        res.add(this);
        return res;
    }

}