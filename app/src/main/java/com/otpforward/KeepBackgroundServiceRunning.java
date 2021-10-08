package com.otpforward;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class KeepBackgroundServiceRunning extends Worker {
    Context ctx;

    public KeepBackgroundServiceRunning(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.ctx = context;
    }

    @NonNull
    public ListenableWorker.Result doWork() {
        ContextCompat.startForegroundService(this.ctx.getApplicationContext(), new Intent(this.ctx.getApplicationContext(), BackgroundService.class));
        return ListenableWorker.Result.success();
    }
}