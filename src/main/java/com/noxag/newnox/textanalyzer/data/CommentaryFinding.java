package com.noxag.newnox.textanalyzer.data;

public class CommentaryFinding extends Finding {
    private int page;
    private int line;
    private String type;

    public CommentaryFinding() {
        super();
    }

    public CommentaryFinding(String c, String t, int p, int l) {
        this();
        setComment(c);
        setType(t);
        setPage(p);
        setLine(l);
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
