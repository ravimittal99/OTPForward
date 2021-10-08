package com.otpforward;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.core.app.NotificationCompat;

public class DBAdapter {
    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBAdapter(Context context2) {
        this.context = context2;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void insertEmail(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationCompat.CATEGORY_EMAIL, str);
        try {
            this.database.insert("emails", (String) null, contentValues);
        } catch (Throwable unused) {
        }
    }

    public DBAdapter open() throws Exception {
        this.dbHelper = new DBHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void insertKeywords(String str, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email_id", Integer.valueOf(i));
        contentValues.put("keyword", str);
        try {
            this.database.insert("keywords", (String) null, contentValues);
        } catch (Throwable unused) {
        }
    }

    public Cursor fetchAllEmails() {
        return this.database.query("emails", (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null);
    }

    public Cursor fetchAllKeywords() {
        return this.database.query("keywords", (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null);
    }

    public String getEmail(int i) {
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from emails where _id=" + i, (String[]) null);
        rawQuery.moveToFirst();
        return rawQuery.getString(rawQuery.getColumnIndex(NotificationCompat.CATEGORY_EMAIL));
    }

    public int getEmailId(String str) {
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor query = sQLiteDatabase.query("emails", (String[]) null, "email='" + str + "'", (String[]) null, (String) null, (String) null, (String) null, (String) null);
        query.moveToFirst();
        return query.getInt(query.getColumnIndex("_id"));
    }

    public void removeKeyWord(String str, String str2) {
        int emailId = getEmailId(str2);
        SQLiteDatabase sQLiteDatabase = this.database;
        sQLiteDatabase.execSQL("delete from keywords where keyword='" + str + "' and email_id=" + emailId + ";");
    }

    public void deleteOldEmail(String str) {
        SQLiteDatabase sQLiteDatabase = this.database;
        sQLiteDatabase.execSQL("delete from emails where email='" + str + "';");
    }
}