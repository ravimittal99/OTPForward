package com.otpforward;

import android.app.Application;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        startPeriodicWork();
    }

    public void startPeriodicWork() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("KEEP_APP_RUNNING", ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) new PeriodicWorkRequest.Builder((Class<? extends ListenableWorker>) KeepBackgroundServiceRunning.class, 15, TimeUnit.MINUTES).build());

    }
}