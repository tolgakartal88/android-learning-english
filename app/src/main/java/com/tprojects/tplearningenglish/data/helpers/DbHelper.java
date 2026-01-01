package com.tprojects.tplearningenglish.data.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tprojects.tplearningenglish.data.tables.WordTable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="LearningEnglish.db";
    private static final int DATABASE_VERSION=1;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WordTable.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WordTable.DROP_TABLE_QUERY);
    }
}
