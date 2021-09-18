package com.hello.youtubeplayer.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.notification.DailyNotification;
import com.hello.youtubeplayer.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1500;
    private static String REMINDER_WORK_NAME = "reminder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initview();
    }

    private void initview() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WorkManager workManager = WorkManager.getInstance();
                OneTimeWorkRequest workRequestnew = new OneTimeWorkRequest.Builder(DailyNotification.class)
                        .build();
                workManager.enqueueUniqueWork(REMINDER_WORK_NAME, ExistingWorkPolicy.REPLACE, workRequestnew);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

        }, SPLASH_SCREEN);

    }
}