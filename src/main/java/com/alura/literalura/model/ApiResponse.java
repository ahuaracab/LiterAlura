package com.alura.literalura.model;

import java.util.List;

public class ApiResponse {
    private int count;
    private String next;
    private String previous;
    private List<BookData> results;

    public ApiResponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<BookData> getResults() {
        return results;
    }

    public void setResults(List<BookData> results) {
        this.results = results;
    }
}
