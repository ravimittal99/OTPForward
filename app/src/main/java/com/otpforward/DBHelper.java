package com.otpforward;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "otp_forward_db";
    private static final int DATABASE_VERSION = 1;
    private static final String EMAILS_TABLE_CREATE = "create table if not exists emails(_id integer primary key autoincrement, email text);";
    private static final String KEYWORDS_TABLE_CREATE = "create table if not exists keywords(_id integer primary key autoincrement, keyword text, email_id integer);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(EMAILS_TABLE_CREATE);
        sQLiteDatabase.execSQL(KEYWORDS_TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i2 != i) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS emails;");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS keywords;");
            onCreate(sQLiteDatabase);
        }
    }
}