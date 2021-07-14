package com.myandroid.kimJH_60181628;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelperIT extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ITDB";
    public static final String TABLE_NAME = "IT_table";
    public static final String C_ID = "_id";
    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String SUMMARY = "summary";
    public static final String SOURCE = "source";
    public static final int VERSION = 2;

    private final String createDB = "create table if not exists " + TABLE_NAME + " ( "
            + C_ID + " integer primary key autoincrement, "
            + IMAGE + " blob, "
            + TITLE + " text, "
            + DATE + " text, "
            + SUMMARY + " text, "
            + SOURCE + " text)";

    public DbHelperIT(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
    }
}
