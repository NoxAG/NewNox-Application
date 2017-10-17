package com.noxag.newnox.textanalyzer.data;

/**
 * This class represents the result of a statistical analysis
 * 
 * @author Pascal.Schroeder@de.ibm.com
 *
 */
public class CommentaryFinding extends Finding {
    private int page;
    private int line;
    private String type;

    public CommentaryFinding() {
        super();
    }

    public CommentaryFinding(String comment, String type, int page, int line) {
        this();
        setComment(comment);
        setType(type);
        setPage(page);
        setLine(line);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
