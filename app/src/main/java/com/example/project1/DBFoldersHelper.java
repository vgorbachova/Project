package com.example.project1;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBFoldersHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "foldersDb";
    public static final String TABLE_FOLDERS = "folders";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    public DBFoldersHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_FOLDERS + "(" + KEY_ID
                + " integer primary key autoincrement, " + KEY_NAME + " text" +  ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FOLDERS);

        onCreate(db);

    }

}
