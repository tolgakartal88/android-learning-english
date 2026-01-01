package com.tprojects.tplearningenglish.data.dto;

import java.util.Objects;

public class Words {

    private int id;
    private String word;
    private String mean;

    public Words(){

    }

    public Words(int id, String word, String mean) {
        this.id = id;
        this.word = word;
        this.mean = mean;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Words)) return false;
        Words words = (Words) o;
        return id == words.id &&
                Objects.equals(word, words.word) &&
                Objects.equals(mean, words.mean);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, mean);
    }
}
