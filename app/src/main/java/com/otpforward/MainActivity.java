package com.otpforward;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    WordsAdapter mAdapter;
    ListView mListView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECEIVE_SMS") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECEIVE_SMS"}, 0);
        }
        try {
            DBAdapter dBAdapter = new DBAdapter(this);
            dBAdapter.open();
            if (dBAdapter.fetchAllEmails().getCount() == 0) {
                dBAdapter.insertEmail("krishna@vervete.com");
                Cursor fetchAllEmails = dBAdapter.fetchAllEmails();
                fetchAllEmails.moveToFirst();
                int i = fetchAllEmails.getInt(fetchAllEmails.getColumnIndex("_id"));
                String[] split = PreferenceManager.getDefaultSharedPreferences(this).getString("keywords", "").split(", ");
                if (split.length > 1) {
                    for (String insertKeywords : split) {
                        dBAdapter.insertKeywords(insertKeywords, i);
                    }
                }
                PreferenceManager.getDefaultSharedPreferences(this).edit().clear();
            }
            dBAdapter.close();
        } catch (Exception unused) {
        }
        this.mListView = (ListView) findViewById(R.id.listview);
        this.mAdapter = new WordsAdapter(this);
        this.mListView.setAdapter(this.mAdapter);
        findViewById(R.id.add_keyword).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle((CharSequence) "Add keyword");
                View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_keyword, (ViewGroup) null);
                final EditText editText = (EditText) inflate.findViewById(R.id.keyword_edt);
                final ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                try {
                    DBAdapter dBAdapter = new DBAdapter(MainActivity.this);
                    dBAdapter.open();
                    Cursor fetchAllEmails = dBAdapter.fetchAllEmails();
                    fetchAllEmails.moveToFirst();
                    while (!fetchAllEmails.isAfterLast()) {
                        arrayList.add(fetchAllEmails.getString(fetchAllEmails.getColumnIndex("email")));
                        arrayList2.add(Integer.valueOf(fetchAllEmails.getInt(fetchAllEmails.getColumnIndex("_id"))));
                        fetchAllEmails.moveToNext();
                    }
                    dBAdapter.close();
                } catch (Exception unused) {
                }
                final Spinner spinner = (Spinner) inflate.findViewById(R.id.email_spinner);
                spinner.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
                builder.setView(inflate);
                builder.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // editText.getText().toString() + ":" + spinner.getSelectedItem().toString();
                        try {
                            String trim = editText.getText().toString().toLowerCase().trim();
                            int intValue = ((Integer) arrayList2.get(arrayList.indexOf(spinner.getSelectedItem().toString()))).intValue();
                            DBAdapter dBAdapter = new DBAdapter(MainActivity.this);
                            dBAdapter.open();
                            dBAdapter.insertKeywords(trim, intValue);
                            dBAdapter.close();
                        } catch (Exception unused) {
                        }
                        MainActivity.this.mAdapter = new WordsAdapter(MainActivity.this);
                        MainActivity.this.mListView.setAdapter(MainActivity.this.mAdapter);
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.add_email).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle((CharSequence) "Add Email");
                final EditText editText = new EditText(MainActivity.this);
                builder.setView((View) editText);
                builder.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String trim = editText.getText().toString().trim();
                        try {
                            DBAdapter dBAdapter = new DBAdapter(MainActivity.this);
                            dBAdapter.open();
                            dBAdapter.insertEmail(trim.toLowerCase());
                            dBAdapter.close();
                        } catch (Exception unused) {
                        }
                        MainActivity.this.mAdapter = new WordsAdapter(MainActivity.this);
                        MainActivity.this.mListView.setAdapter(MainActivity.this.mAdapter);
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}