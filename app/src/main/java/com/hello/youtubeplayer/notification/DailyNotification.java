package com.hello.youtubeplayer.notification;

import androidx.work.Worker;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.ui.main.MainActivity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import static android.app.Notification.DEFAULT_ALL;

public class DailyNotification extends Worker {
    private static String REMINDER_WORK_NAME = "reminder";
    private static String PARAM_NAME = "name";
    private Context context;
    public DailyNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    private NotificationManager mNotificationManager;
    private static void runAt() {
        WorkManager workManager = WorkManager.getInstance();
        LocalTime alarmTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            alarmTime = LocalTime.of(7, 00,0);
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            LocalTime nowTime = now.toLocalTime();
            if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) {
                now = now.plusDays(1);
            }
            now = now.withHour(alarmTime.getHour()).withMinute(alarmTime.getMinute());
            Duration duration = Duration.between(LocalDateTime.now(), now);
            OneTimeWorkRequest workRequestnew = new OneTimeWorkRequest.Builder(DailyNotification.class)
                    .setInitialDelay(duration.getSeconds(), TimeUnit.SECONDS)
                    .build();
            workManager.enqueueUniqueWork(REMINDER_WORK_NAME, ExistingWorkPolicy.REPLACE, workRequestnew);
        }
    }
    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        boolean isScheduleNext = true;
        try {
            createNotification();
            Result.success();
        } catch (Exception e) {
            if (getRunAttemptCount() > 3) {
                return Result.success();
            }
        }finally {
            if (isScheduleNext) {
                runAt();
            }
        }
        return null;
    }
    private void createNotification() {
        String title = context.getString(R.string.daily_notificatiom);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);
        bigText.setBigContentTitle("");
        bigText.setSummaryText("");
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(DEFAULT_ALL);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setContentTitle("Youtube Music");
        mBuilder.setContentText("");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText).setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "notify_001";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    title,
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(1, mBuilder.build());
    }
}
