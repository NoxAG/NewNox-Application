package com.noxag.newnox.textanalyzer.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.noxag.newnox.textanalyzer.TextanalyzerAlgorithm;
import com.noxag.newnox.textanalyzer.data.Finding;

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
        return null;
    }

}
