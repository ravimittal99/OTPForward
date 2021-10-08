package com.otpforward;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class BackgroundService extends Service {
    public static final String CHANNEL_ID = "SMSForwardChannel";
    private IntentFilter mIntentFilter;
    private SmsReceiver mSMSreceiver;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.i(NotificationCompat.CATEGORY_SERVICE, "started");
        this.mSMSreceiver = new SmsReceiver();
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(this.mSMSreceiver, this.mIntentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mSMSreceiver);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        String stringExtra = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        startForeground(1, new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Forward OTPs").setContentText(stringExtra).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0)).build());
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }
}