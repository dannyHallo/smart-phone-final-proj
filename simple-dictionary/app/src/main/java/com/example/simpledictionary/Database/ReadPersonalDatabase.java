package com.example.simpledictionary.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.simpledictionary.Model.words;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class ReadPersonalDatabase extends SQLiteAssetHelper {

    private static final String DB_NAME = "my_words.db";
    private static final String TABLE_NAME = "words_trans";
    public Context context;

    ReadTutorialDatabase readTutorialDatabase;

    public ReadPersonalDatabase(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }



    public List<words> getWords(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlWordsSelect = {"num", "obj", "basic_trans", "spec_trans", "english_trans"};

        qb.setTables(TABLE_NAME);
        List<words> total_selection = new ArrayList<>(Tutorial());

        Cursor cursor = qb.query(db, sqlWordsSelect, "obj LIKE ?",
                new String[]{"%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                words words = new words();
                words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                words.setBasic_trans(cursor.getString(cursor.getColumnIndex("basic_trans")));
                words.setSpec_trans(cursor.getString(cursor.getColumnIndex("spec_trans")));
                words.setEnglish_trans(cursor.getString(cursor.getColumnIndex("english_trans")));
                total_selection.add(words);
            } while (cursor.moveToNext());
        }

        if(total_selection.size() == 0){
            words word_list_empty = new words();
            word_list_empty.setNum("0");
            word_list_empty.setObj("WORD LIST EMPTY");
            word_list_empty.setBasic_trans("单词本为空,先将词划入单词本");
            word_list_empty.setSpec_trans("");
            word_list_empty.setEnglish_trans("");

            total_selection.add(word_list_empty);
        }else{
            words back_word = new words();
            back_word.setNum("0");
            back_word.setObj("BACK");
            back_word.setBasic_trans("双击任意词条返回");
            back_word.setSpec_trans("");
            back_word.setEnglish_trans("");

            total_selection.add(back_word);
        }



        return total_selection;


    }

    public List<words> Tutorial(){
        List<words> tutorial_selection = new ArrayList<>();
        readTutorialDatabase = new ReadTutorialDatabase(context);
        List<String> tutorial_process = readTutorialDatabase.getProcess();
        if(tutorial_process.size() != 4){
            words words = new words();
            words.setNum("0");
            words.setObj("Tutorials");
            words.setBasic_trans("完成所有教程以删除该词条");
            words.setSpec_trans("这是你的单词本，这里也有一个任务");
            words.setEnglish_trans("");
            tutorial_selection.add(words);
        }
        if(!tutorial_process.contains("sweep2")){
            words words = new words();
            words.setNum("0");
            words.setObj("SWEEP");
            words.setBasic_trans("侧滑任意词条将其从单词本删除");
            words.setSpec_trans("将一个单词从单词本删除以删除该词条\n需要注意的是：教程词条无效");
            words.setEnglish_trans("");
            tutorial_selection.add(words);
        }
        return tutorial_selection;
    }
}
