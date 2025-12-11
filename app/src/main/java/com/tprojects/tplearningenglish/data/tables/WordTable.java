package com.tprojects.tplearningenglish.data.tables;

public class WordTable {
    public static final String TABLE_NAME ="words";
    public static final String COL_ID="id";
    public static final String COL_WORD="word";
    public static final String COL_MEAN="mean";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+ WordTable.TABLE_NAME
            +"("
            +COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COL_WORD+" TEXT,"
            +COL_MEAN+" TEXT"
            +")";
    public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+WordTable.TABLE_NAME;
}
