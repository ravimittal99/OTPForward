package com.otpforward;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Log.i("sms_received", "received");
        if (extras != null) {
            Object[] objArr = (Object[]) extras.get("pdus");
            for (Object obj : objArr) {
                SmsMessage createFromPdu = SmsMessage.createFromPdu((byte[]) obj, extras.getString("format"));
                String displayMessageBody = createFromPdu.getDisplayMessageBody();
                String originatingAddress = createFromPdu.getOriginatingAddress();
                try {
                    DBAdapter dBAdapter = new DBAdapter(context);
                    dBAdapter.open();
                    Cursor fetchAllKeywords = dBAdapter.fetchAllKeywords();
                    fetchAllKeywords.moveToFirst();
                    while (!fetchAllKeywords.isAfterLast()) {
                        if (displayMessageBody.toLowerCase().contains(fetchAllKeywords.getString(fetchAllKeywords.getColumnIndex("keyword")))) {
                            sendMail(displayMessageBody, originatingAddress, dBAdapter.getEmail(fetchAllKeywords.getInt(fetchAllKeywords.getColumnIndex("email_id"))));
                        }
                        fetchAllKeywords.moveToNext();
                    }
                    dBAdapter.close();
                } catch (Exception unused) {
                }
            }
        }
    }

    private void sendMail(final String messageBody, final String originatingAddress, final String toEmail) {
        new Thread(new Runnable() {
            public void run() {
                GMailSender sender = new GMailSender("quacktest01@gmail.com",
                        "awgeybqbshmsrvsm");
                try {
                    sender.sendMail("New SMS from " + originatingAddress,
                            messageBody,
                            "quacktest01@gmail.com",
                            toEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }
}

