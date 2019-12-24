package com.example.project1;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBBlockHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "blocksDb";
    public static final String TABLE_BLOCKS = "blocks";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_METHOD = "method";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_KIND_OF_BLOCK = "block";
    public static final String KEY_TEXT = "text";
    public static final String KEY_LEARNING = "learning";
    public static final String KEY_PREVIOUS_LESSON = "previous";
    public static final String KEY_NEXT_LESSON = "next";

    public DBBlockHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_BLOCKS + "(" + KEY_ID
                + " integer primary key autoincrement, " + KEY_NAME + " text," + KEY_METHOD + " text," + KEY_FOLDER + " text,"  + KEY_TEXT + " text," + KEY_KIND_OF_BLOCK + " text," + KEY_LEARNING + " integer," + KEY_PREVIOUS_LESSON + " integer," + KEY_NEXT_LESSON + " integer" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_BLOCKS);
        onCreate(db);

    }
}
