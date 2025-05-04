package com.example.simpledictionary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.simpledictionary.Model.words;

import androidx.annotation.Nullable;

public class WritePersonalDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "my_words.db";
    private static final String TABLE_NAME = "words_trans";

    public WritePersonalDatabase(@Nullable Context context) {
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

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (num TEXT, obj TEXT, basic_trans TEXT, spec_trans TEXT, english_trans TEXT)";
        db.execSQL(createTable);
    }

    public void addWord(words word){
        String num, obj, basic_trans, spec_trans, english_trans;
        num = word.getNum();
        obj = word.getObj();
        basic_trans = word.getBasic_trans();
        spec_trans = word.getSpec_trans();
        english_trans = word.getEnglish_trans();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("num", num);
        contentValues.put("obj", obj);
        contentValues.put("basic_trans", basic_trans);
        contentValues.put("spec_trans", spec_trans);
        contentValues.put("english_trans", english_trans);
        db.insert(TABLE_NAME,null , contentValues);


    }

    public void deleteWord(words word){
        SQLiteDatabase db = this.getWritableDatabase();
        String obj = word.getObj();

        db.delete(TABLE_NAME, "obj = ?", new String[]{obj});
    }
}
