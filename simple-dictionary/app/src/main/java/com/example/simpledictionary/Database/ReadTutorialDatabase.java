package com.example.simpledictionary.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.simpledictionary.Model.words;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class ReadTutorialDatabase extends SQLiteAssetHelper {

    private static final String DB_NAME = "tutorial.db";
    private static final String TABLE_NAME = "tutorial_process";


    public ReadTutorialDatabase(Context context) {
        super(context, DB_NAME, null, 1);
    }



    public List<String> getProcess(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlWordsSelect = {"process"};

        qb.setTables(TABLE_NAME);
        List<String> total_selection = new ArrayList<>();


        Cursor cursor = qb.query(db, sqlWordsSelect, "process LIKE ?",
                new String[]{"%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String current_process = "";
                current_process = cursor.getString(cursor.getColumnIndex("process"));
                total_selection.add(current_process);
            } while (cursor.moveToNext());
        }
        return total_selection;
    }
}
