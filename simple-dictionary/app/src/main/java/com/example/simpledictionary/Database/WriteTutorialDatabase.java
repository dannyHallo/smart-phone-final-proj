package com.example.simpledictionary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.simpledictionary.Model.words;

import androidx.annotation.Nullable;

public class WriteTutorialDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "tutorial.db";
    private static final String TABLE_NAME = "tutorial_process";

    public WriteTutorialDatabase(@Nullable Context context) {
        super(context, DB_NAME, null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (process TEXT)";
        db.execSQL(createTable);
    }

    public void addProcess(String process){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("process", process);

        db.insert(TABLE_NAME,null , contentValues);

    }
}
