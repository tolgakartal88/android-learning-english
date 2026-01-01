package com.tprojects.tplearningenglish.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.tprojects.tplearningenglish.data.dto.Words;
import com.tprojects.tplearningenglish.data.tables.WordTable;

import java.util.ArrayList;
import java.util.List;

public class WordsHelper extends DbHelper {

    public WordsHelper(@Nullable Context context) {
        super(context);
    }

    /**
     * TÜM kelimeleri List<Words> olarak döner
     */
    public List<Words> getWords() {
        List<Words> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                WordTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WordTable.COL_WORD + " ASC"
        )) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(
                            cursor.getColumnIndexOrThrow(WordTable.COL_ID)
                    );
                    String word = cursor.getString(
                            cursor.getColumnIndexOrThrow(WordTable.COL_WORD)
                    );
                    String mean = cursor.getString(
                            cursor.getColumnIndexOrThrow(WordTable.COL_MEAN)
                    );

                    list.add(new Words(id, word, mean));
                } while (cursor.moveToNext());
            }
        }

        return list;
    }

    public long insertWord(String word, String mean) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WordTable.COL_WORD, word);
        values.put(WordTable.COL_MEAN, mean);

        return db.insert(WordTable.TABLE_NAME, null, values);
    }
    public int updateWord(int id, String word, String mean) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WordTable.COL_WORD, word);
        values.put(WordTable.COL_MEAN, mean);

        return db.update(
                WordTable.TABLE_NAME,
                values,
                WordTable.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteWord(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                WordTable.TABLE_NAME,
                WordTable.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public Cursor searchWords(String query) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + WordTable.TABLE_NAME +
                " WHERE " + WordTable.COL_WORD + " LIKE ?" +
                " OR " + WordTable.COL_MEAN + " LIKE ?" +
                " ORDER BY " + WordTable.COL_WORD;

        String[] args = new String[]{"%" + query + "%","%" + query + "%"};

        return db.rawQuery(sql, args);
    }
}
