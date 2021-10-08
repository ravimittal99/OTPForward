package com.otpforward;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class WordsAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public final Context ctx;
    private final LayoutInflater inflater;
    ArrayList<String> keywords;

    public long getItemId(int i) {
        return (long) i;
    }

    public WordsAdapter(Context context) {
        this.ctx = context;
        try {
            DBAdapter dBAdapter = new DBAdapter(context);
            dBAdapter.open();
            Cursor fetchAllKeywords = dBAdapter.fetchAllKeywords();
            this.keywords = new ArrayList<>();
            if (fetchAllKeywords.getCount() > 0) {
                fetchAllKeywords.moveToFirst();
                while (!fetchAllKeywords.isAfterLast()) {
                    ArrayList<String> arrayList = this.keywords;
                    arrayList.add(fetchAllKeywords.getString(fetchAllKeywords.getColumnIndex("keyword")) + " -> " + dBAdapter.getEmail(fetchAllKeywords.getInt(fetchAllKeywords.getColumnIndex("email_id"))));
                    fetchAllKeywords.moveToNext();
                }
            }
            dBAdapter.close();
        } catch (Exception unused) {
        }
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.keywords.size();
    }

    public Object getItem(int i) {
        return this.keywords.get(i);
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = this.inflater.inflate(R.layout.list_item, viewGroup, false);
            viewHolder.btn = (Button) view2.findViewById(R.id.btn);
            viewHolder.txt = (TextView) view2.findViewById(R.id.txt);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txt.setText(getItem(i).toString());
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    DBAdapter dBAdapter = new DBAdapter(WordsAdapter.this.ctx);
                    dBAdapter.open();
                    dBAdapter.removeKeyWord(WordsAdapter.this.keywords.get(i).split(" -> ")[0], WordsAdapter.this.keywords.get(i).split(" -> ")[1]);
                    dBAdapter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                WordsAdapter.this.keywords.remove(i);
                WordsAdapter.this.notifyDataSetChanged();
            }
        });
        return view2;
    }

    public class ViewHolder {
        Button btn;
        TextView txt;

    }
}