/**
 * 
 */
package com.noxag.newnox.textanalyzer.algorithms;

import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;

/**
 * This class produces a statistic to show which words have been used often
 * 
 * @author Tobias.Schmidt@de.ibm.com
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

}
