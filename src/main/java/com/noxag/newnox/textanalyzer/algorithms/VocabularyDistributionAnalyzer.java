/**
 * 
 */
package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;

/**
 * This class produces a statistic to show which words have been used often
 * 
 * @author Tobias.Schmidt@de.ibm.com, Lars.Dittert@de.ibm.com
 *
 */
public class VocabularyDistributionAnalyzer implements TextanalyzerAlgorithm {

    @Override
    public List<Finding> run(PDDocument doc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUIName() {
        return VocabularyDistributionAnalyzer.class.getSimpleName();
    }

    public VocabularyDistributionAnalyzer() {

    }

    public void getStatistics() {
        splitStringIntoWordsAndPutIntoList();
    }

    public void createStringOutOfPDF() throws IOException {
        // PDFTextStripper stripper = new PDFTextStripper();
        // PDFTextExtractionUtil zugriff = new PDFTextExtractionUtil();
        // zugriff.runTextStripper(stripper, document, 1, pageEndIndex)
    }

    public Object[] splitStringIntoWordsAndPutIntoList() {
        List<String> arrayListWithoutPunctuationCharacter = new ArrayList<String>();
        List<String> notCaseSensitiveListWithoutPunctuationCharacter = new ArrayList<>();
        List<Integer> wordsCounter = new ArrayList<Integer>();
        String test = "Lorem lorem ipsum 123 dolor Lorem sit amet, consetetur Lorem sadipscing elitr, sed Lorem diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";
        String[] parts = test.split(" ");

        for (int i = 0; i < parts.length; i++) {
            arrayListWithoutPunctuationCharacter.add(parts[i].replaceAll("[^\\p{Alpha}]+", ""));
            notCaseSensitiveListWithoutPunctuationCharacter
                    .add((arrayListWithoutPunctuationCharacter.get(i)).toLowerCase());
        }

        for (int k = 0; k < parts.length; k++) {
            wordsCounter.add(1);
        }

        for (int j = 0; j < arrayListWithoutPunctuationCharacter.size(); j++) {
            String wordWhichShouldBeProofed = notCaseSensitiveListWithoutPunctuationCharacter.get(j);
            for (int l = 0; l < arrayListWithoutPunctuationCharacter.size(); l++) {
                if (j != l) {
                    if (notCaseSensitiveListWithoutPunctuationCharacter.get(l).equals(wordWhichShouldBeProofed)) {
                        // 1.increase counter
                        wordsCounter.set(j, (wordsCounter.get(j) + 1));
                        // 2.delete duplicate
                        notCaseSensitiveListWithoutPunctuationCharacter.remove(l);
                        arrayListWithoutPunctuationCharacter.remove(l);
                        // 3.delete counter of duplicate
                        wordsCounter.remove(l);
                    }
                }
            }

        }
        return new Object[] { arrayListWithoutPunctuationCharacter, wordsCounter };
    }

}
