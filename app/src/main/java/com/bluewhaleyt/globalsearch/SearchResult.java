package com.bluewhaleyt.globalsearch;

import android.text.SpannableString;

public class SearchResult {
    private final String filePath;
    private final String fileName;
    private final String content;
    private final SpannableString highlightedContent;
    private final int lineNumber;

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
}