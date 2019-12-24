package com.example.project1;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCardHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "cardsDb";
    public static final String TABLE_CARDS = "cards";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_METHOD = "method";
    public static final String KEY_BLOCK = "block";
    public static final String KEY_FIRST_WORD = "first_word";
    public static final String KEY_SECOND_WORD = "second_word";

    public DBCardHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CARDS + "(" + KEY_ID
                + " integer primary key autoincrement, " + KEY_NAME + " text," + KEY_METHOD + " text," + KEY_BLOCK + " text,"  + KEY_FIRST_WORD + " text," + KEY_SECOND_WORD + " text" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_CARDS);
        onCreate(db);
    }
}
