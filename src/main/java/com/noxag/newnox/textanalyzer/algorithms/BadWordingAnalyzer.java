package com.noxag.newnox.textanalyzer.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;
import com.noxag.newnox.textanalyzer.data.TextPositionSequence;
import com.noxag.newnox.textanalyzer.util.TextanalyzerUtil;

public class BadWordingAnalyzer implements TextanalyzerAlgorithm {

    private static final String BLACKLIST_PATH = "src/ressources/analyzer-conf/wording-blacklist.cvg";
    private List<String> wordingBlacklist;

    public BadWordingAnalyzer() {
        this(BLACKLIST_PATH);
    }

    public BadWordingAnalyzer(String wordingBlacklistPath) {
        this.wordingBlacklist = readWordingBlackListFile(wordingBlacklistPath);
    }

    private List<String> readWordingBlackListFile(String wordingBlacklistPath) {
        List<String> wordingBlacklist = new ArrayList<>();
        wordingBlacklist.add("ich");
        wordingBlacklist.add("man");
        wordingBlacklist.add("könnte");
        wordingBlacklist.add("sollte");
        wordingBlacklist.add("müsste");
        return wordingBlacklist;
    }

    @Override
    public List<Finding> run(PDDocument doc) {
        try {
            List<TextPositionSequence> findings = TextanalyzerUtil.findInDocument(doc, "word",
                    TextanalyzerUtil::findWordOnPage);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getUIName() {
        return BadWordingAnalyzer.class.getSimpleName();
    }

}
