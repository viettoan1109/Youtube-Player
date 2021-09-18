package com.hello.youtubeplayer.notification;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.data.network.model.Item;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CreateNotification {

    public static final String CHANNEL_NAME = "channel";
    public static final String ACTIONPREVIOUS = "actionprevious";
    public static final String CHANNEL_PLAY = "actionplay";
    public static final String CHANNEL_NEXT = "actionnext";

    public static Notification notification;

    public static void CreateNotification(Context context, VideoYoutube.ItemsBean item, int playButton, int pos, int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

            notification = new NotificationCompat.Builder(context, CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_play_background)
                    .setContentTitle(item.getSnippet().getTitle())
                    .setContentText(item.getSnippet().getChannelTitle())
                    .setLargeIcon(getIcon(item.getSnippet().getThumbnails().getHigh().getUrl()))
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationManagerCompat.notify(1, notification);
        }
    }

    public static Bitmap getIcon(String src){
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }

        catch (IOException e) {
            return null;
        }
    }

}
