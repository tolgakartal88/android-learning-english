package com.tprojects.tplearningenglish.data.dto;

import androidx.annotation.Nullable;

public class Words {
    public Integer _id;
    public String _word;
    public String _mean;

    public Words(@Nullable Integer id, String word, String mean){
        _id = id;
        _word = word;
        _mean = mean;
    }

    public Integer get_id() {
        return _id;
    }

    public String get_word() {
        return _word;
    }

    public String get_mean() {
        return _mean;
    }
}
