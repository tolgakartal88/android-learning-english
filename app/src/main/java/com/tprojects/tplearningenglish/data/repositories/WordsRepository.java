package com.tprojects.tplearningenglish.data.repositories;

import android.content.Context;
import android.database.Cursor;

import com.tprojects.tplearningenglish.data.dto.Words;
import com.tprojects.tplearningenglish.data.helpers.WordsHelper;
import com.tprojects.tplearningenglish.data.tables.WordTable;

import java.util.ArrayList;
import java.util.List;

public class WordsRepository {

    private final WordsHelper wordsHelper;
    public WordsRepository(Context context) {
        wordsHelper = new WordsHelper(context);
    }

    /**
     * Tüm kelimeleri getirir
     */
    public List<Words> getWords() {
        return wordsHelper.getWords();
    }

    /**
     * Yeni kelime ekler
     */
    public long insertWord(String word, String mean) {
        return wordsHelper.insertWord(word, mean);
    }

    public void deleteWord(int id) {
        wordsHelper.deleteWord(id);
    }

    public void updateWord(int id, String word, String mean) {
        wordsHelper.updateWord(id, word, mean);
    }

    public List<Words> searchWords(String query) {
        List<Words> list = new ArrayList<>();
        Cursor cursor = wordsHelper.searchWords(query);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Words word = new Words();
                word.setId(cursor.getInt(
                        cursor.getColumnIndexOrThrow(WordTable.COL_ID)));
                word.setWord(cursor.getString(
                        cursor.getColumnIndexOrThrow(WordTable.COL_WORD)));
                word.setMean(cursor.getString(
                        cursor.getColumnIndexOrThrow(WordTable.COL_MEAN)));
                list.add(word);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return list;
    }

}
