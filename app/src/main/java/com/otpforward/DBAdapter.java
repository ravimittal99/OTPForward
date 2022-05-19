package com.otpforward;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        contentValues.put("email", str);
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
        return rawQuery.getString(rawQuery.getColumnIndex("email"));
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

    public String getDBJSON() throws JSONException {
        JSONObject object = new JSONObject();
        Cursor cr = fetchAllEmails();
        cr.moveToFirst();
        JSONArray emails = new JSONArray();
        while (!cr.isAfterLast()) {
            JSONObject emailObj = new JSONObject();
            emailObj.put("email", cr.getString(cr.getColumnIndex("email")));
            emailObj.put("_id", cr.getInt(cr.getColumnIndex("_id")));
            emails.put(emailObj);
            cr.moveToNext();
        }

        Cursor cr2 = fetchAllKeywords();
        cr2.moveToFirst();
        JSONArray keywords = new JSONArray();
        while (!cr2.isAfterLast()) {
            JSONObject keywordObj = new JSONObject();
            keywordObj.put("keyword", cr.getString(cr.getColumnIndex("keyword")));
            keywordObj.put("_id", cr.getInt(cr.getColumnIndex("_id")));
            keywordObj.put("email_id", cr.getInt(cr.getColumnIndex("email_id")));
            keywords.put(keywordObj);
            cr2.moveToNext();
        }
        object.put("emails", emails);
        object.put("keywords", keywords);
        return object.toString();

    }

    public void createDBFromJson(String json) throws JSONException {
        SQLiteDatabase sQLiteDatabase = this.database;
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS emails;");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS keywords;");
        sQLiteDatabase.execSQL(DBHelper.EMAILS_TABLE_CREATE);
        sQLiteDatabase.execSQL(DBHelper.KEYWORDS_TABLE_CREATE);

        JSONObject object=new JSONObject(json);
        JSONArray emails=object.getJSONArray("emails");
        for(int i=0;i<emails.length();i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("email", emails.getJSONObject(i).getString("email"));
            contentValues.put("_id", emails.getJSONObject(i).getInt("_id"));
            try {
                this.database.insert("emails", (String) null, contentValues);
            } catch (Throwable unused) {
            }
        }

        JSONArray keywords=object.getJSONArray("keywords");
        for(int i=0;i<keywords.length();i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("keyword", keywords.getJSONObject(i).getString("keyword"));
            contentValues.put("_id", keywords.getJSONObject(i).getInt("_id"));
            contentValues.put("email_id", keywords.getJSONObject(i).getInt("email_id"));
            try {
                this.database.insert("keywords", (String) null, contentValues);
            } catch (Throwable unused) {
            }
        }



    }
}