package com.bluewhaleyt.globalsearch;

import android.text.SpannableString;

public class SearchResult {
    private final String filePath;
    private final String fileName;
    private final String content;
    private final SpannableString highlightedContent;
    private final int lineNumber;

    private int startIndex;
    private int endIndex;

    public SearchResult(String filePath, String fileName, String content, SpannableString highlightedContent, int lineNumber) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.content = content;
        this.highlightedContent = highlightedContent;
        this.lineNumber = lineNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    public SpannableString getHighlightedContent() {
        return highlightedContent;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}