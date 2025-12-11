package com.tprojects.tplearningenglish.data.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.tprojects.tplearningenglish.data.tables.WordTable;

public class WordsHelper extends DbHelper {

    private DbHelper dbHelper;
    public WordsHelper(@Nullable Context context) {
        super(context);
        dbHelper = new DbHelper(context);
    }

    public Long insertWord(String word, String mean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WordTable.COL_WORD,word);
        cv.put(WordTable.COL_MEAN,mean);
        long result = db.insert(WordTable.TABLE_NAME,null,cv);
        return result;
    }

    public Cursor getWords(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+WordTable.TABLE_NAME+" ORDER BY word",null);
    }

    public Cursor getWordByWord(String word)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+WordTable.TABLE_NAME+" WHERE word='"+word+"' ORDER BY word",null);
    }
    public Cursor getWordById(Integer id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+WordTable.TABLE_NAME+" WHERE id='"+id.toString()+"' ORDER BY word",null);
    }
}