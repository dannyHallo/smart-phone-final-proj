package com.example.simpledictionary.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.simpledictionary.MainActivity;
import com.example.simpledictionary.Model.words;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReadOriginalDatabase extends SQLiteAssetHelper {

    private static final String DB_NAME = "merged.db";
    private static final int VERSION = 1;
    public Context context;

    ReadTutorialDatabase readTutorialDatabase;

    public ReadOriginalDatabase(Context context) {
        super(context, DB_NAME, null, 49);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<words> getWordsByObj(String obj) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //Make sure all is column in your table
        String[] sqlWordsSelect = {"num", "obj", "basic_trans", "spec_trans", "english_trans"};
        String[] sqlPhrasesSelect = {"num", "obj", "trans"};
        String wordsTableName = "words_trans";
        String phrasesTableName = "phrases_trans";
        qb.setTables(wordsTableName);


        List<words> total_selection = new ArrayList<>(Tutorial());

        obj = ProcessStringForWords(obj);
        Cursor cursor = qb.query(db, sqlWordsSelect, "obj LIKE ?",
                new String[]{obj.replace("", "%")}, null, null, null);
        List<words> words_selection = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                words words = new words();
                words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                words.setBasic_trans(cursor.getString(cursor.getColumnIndex("basic_trans")));
                words.setSpec_trans(cursor.getString(cursor.getColumnIndex("spec_trans")));
                words.setEnglish_trans(cursor.getString(cursor.getColumnIndex("english_trans")));
                words_selection.add(words);
            } while (cursor.moveToNext());
        }
        words_selection.sort(new Comparator<words>() {
            @Override
            public int compare(words o1, words o2) {
                return Integer.compare(o1.getObjLength(), o2.getObjLength());
            }
        });

        total_selection.addAll(words_selection);


        List<words> phrases_selection = new ArrayList<>();

        if(obj.contains(" ")){
            qb = new SQLiteQueryBuilder();
            qb.setTables(phrasesTableName);

            cursor = qb.query(db, sqlPhrasesSelect, "obj LIKE ?",
                    new String[]{obj.replace("", "%")}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    words words = new words();
                    words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                    words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                    words.setBasic_trans(cursor.getString(cursor.getColumnIndex("trans")));
                    words.setSpec_trans("");
                    words.setEnglish_trans("");
                    phrases_selection.add(words);
                } while (cursor.moveToNext());
            }
        }else{
            qb = new SQLiteQueryBuilder();
            qb.setTables(phrasesTableName);

            cursor = qb.query(db, sqlPhrasesSelect, "obj LIKE ?",
                    new String[]{obj + " " + "%"}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    words words = new words();
                    words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                    words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                    words.setBasic_trans(cursor.getString(cursor.getColumnIndex("trans")));
                    words.setSpec_trans("");
                    words.setEnglish_trans("");
                    phrases_selection.add(words);
                } while (cursor.moveToNext());
            }

            cursor = qb.query(db, sqlPhrasesSelect, "obj LIKE ?",
                    new String[]{"%" + " " + obj + " " + "%"}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    words words = new words();
                    words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                    words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                    words.setBasic_trans(cursor.getString(cursor.getColumnIndex("trans")));
                    words.setSpec_trans("");
                    words.setEnglish_trans("");
                    phrases_selection.add(words);
                } while (cursor.moveToNext());
            }

            cursor = qb.query(db, sqlPhrasesSelect, "obj LIKE ?",
                    new String[]{"%" + " " + obj}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    words words = new words();
                    words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                    words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                    words.setBasic_trans(cursor.getString(cursor.getColumnIndex("trans")));
                    words.setSpec_trans("");
                    words.setEnglish_trans("");
                    phrases_selection.add(words);
                } while (cursor.moveToNext());
            }

            cursor = qb.query(db, sqlPhrasesSelect, "obj LIKE ?",
                    new String[]{obj}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    words words = new words();
                    words.setNum(cursor.getString(cursor.getColumnIndex("num")));
                    words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                    words.setBasic_trans(cursor.getString(cursor.getColumnIndex("trans")));
                    words.setSpec_trans("");
                    words.setEnglish_trans("");
                    phrases_selection.add(words);
                } while (cursor.moveToNext());
            }
        }
        phrases_selection.sort(new Comparator<words>() {
            @Override
            public int compare(words o1, words o2) {
                return Integer.compare(o1.getObjLength(), o2.getObjLength());
            }
        });

        total_selection.addAll(phrases_selection);



        words back_word = new words();
        back_word.setNum("0");
        back_word.setObj("BACK");
        back_word.setBasic_trans("双击任意词条返回");
        back_word.setSpec_trans("");
        back_word.setEnglish_trans("");

        total_selection.add(back_word);
        //total_selection = total_selection.subList(0, 100);
        return total_selection;
    }

    public String ProcessStringForWords(String originString) {
        String final_string = originString;
        if ("%".equals(final_string)) {
            final_string = "NO WAY";
        }
        return final_string;
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
            words.setSpec_trans("单词本中也有任务");
            words.setEnglish_trans("");
            tutorial_selection.add(words);
        }
        if(!tutorial_process.contains("double_tap")){
            words words = new words();
            words.setNum("0");
            words.setObj("DOUBLE TAP");
            words.setBasic_trans("双击任意词条返回搜索界面");
            words.setSpec_trans("返回一次以删除该词条");
            words.setEnglish_trans("");
            tutorial_selection.add(words);
        }if(!tutorial_process.contains("sweep")){
            words words = new words();
            words.setNum("0");
            words.setObj("SWEEP");
            words.setBasic_trans("侧滑任意词条将其放入你的单词本");
            words.setSpec_trans("将一个单词滑入单词本以删除该词条；\n需要注意的是：教程词条无效");
            words.setEnglish_trans("");
            tutorial_selection.add(words);
        }if(!tutorial_process.contains("enter_personal_db")){
            words words = new words();
            words.setNum("0");
            words.setObj("ENTER YOUR WORD LIST");
            words.setBasic_trans("搜索空白信息进入单词本");
            words.setSpec_trans("进入一次单词本以删除该词条\n也就是说 你只需要在搜索界面按下ENTER\n不要像吴汶轩一样加个空格");
            words.setEnglish_trans("");
            tutorial_selection.add(words);
        }
        return tutorial_selection;
    }

    public List<words> singleSearch(String word){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //Make sure all is column in your table
        String[] sqlWordsSelect = {"num", "obj", "basic_trans", "spec_trans", "english_trans"};
        String wordsTableName = "words_trans";
        qb.setTables(wordsTableName);


        List<words> total_selection = new ArrayList<>();

        Cursor cursor = qb.query(db, sqlWordsSelect, "obj = ?",
                new String[]{word}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                words words = new words();
                words.setNum("0");
                words.setObj(cursor.getString(cursor.getColumnIndex("obj")));
                words.setBasic_trans(cursor.getString(cursor.getColumnIndex("basic_trans")));
                words.setSpec_trans(cursor.getString(cursor.getColumnIndex("spec_trans")));
                words.setEnglish_trans(cursor.getString(cursor.getColumnIndex("english_trans")));
                total_selection.add(words);
            } while (cursor.moveToNext());
        }
        return total_selection;
    }

    public List<words> singleSearchForTutorial(){
        List<words> total_selection = new ArrayList<>();

        words words = new words();
        words.setNum("0");
        words.setObj("tutorial");
        words.setBasic_trans("你好, 搜索任意单词进入教程");
        words.setSpec_trans("");
        words.setEnglish_trans("");
        total_selection.add(words);

        return total_selection;
    }

}
