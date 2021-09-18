package com.hello.youtubeplayer.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hello.youtubeplayer.data.network.model.Item;

import com.hello.youtubeplayer.databinding.ActivityMainBinding;
import com.hello.youtubeplayer.interfaces.Key;
import com.hello.youtubeplayer.ui.comment.CommentFragment;
import com.hello.youtubeplayer.ui.customview.CustomDialog;
import com.hello.youtubeplayer.ui.play.DetailPlayFragment;
import com.hello.youtubeplayer.ui.play.PlayVideoFragment;
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.ui.search.SearchFragment;
import com.hello.youtubeplayer.ui.home.HomeFragment;
import com.hello.youtubeplayer.ui.setting.SettingFragment;
import com.hello.youtubeplayer.ui.trending.TrendingFragment;
import com.hoanganhtuan95ptit.draggable.DraggablePanel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "channel_id";
    private static final String PRIORITY_CHANNEL_ID = "pr_channel_id";
    private Bitmap bitmap;
    private boolean checkMaxScreen = false;

    public boolean isCheckMaxScreen() {
        return checkMaxScreen;
    }

    public void setCheckMaxScreen(boolean checkMaxScreen) {
        this.checkMaxScreen = checkMaxScreen;
    }

    private NotificationManagerCompat notificationManagerCompat;

    private PlayVideoFragment playVideoFragment = new PlayVideoFragment();
    private DetailPlayFragment detailPlayFragment = new DetailPlayFragment();
    private CommentFragment commentFragment = new CommentFragment();

    private String videoId;
    private String channelID;
    private String description;
    private String titleVideo;
    private String nameChannel;
    private String like;
    private String disLike;
    private String viewCount;
    private String commentCount;
    private View shadow;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        shadow = findViewById(R.id.shadow);
        drag();
        addControl();
        setIconTablayout();

    }

    public void pauseVideo() {
        playVideoFragment.pauseVideo();
    }

    public void playVideo() {
        playVideoFragment.playVideo();
    }

    @Override
    public void onBackPressed() {

        if (binding.draggablePanel.isMaximize()) {
            binding.draggablePanel.minimize();
            detailPlayFragment.dismissShareScreen();
            removeCommentScreen();
        } else {
            super.onBackPressed();
        }


    }


    public void buttonOpenDialogClicked() {
        CustomDialog dialog = new CustomDialog(MainActivity.this);
        dialog.show();
    }


    private void getDataBundle(Fragment fragment) {
        Bundle bundle = new Bundle();

        bundle.putString(Key.VIDEO_ID, videoId);
        bundle.putString(Key.CHANNEL_ID, channelID);
        bundle.putString(Key.TITLE_VIDEO, titleVideo);
        bundle.putString(Key.DESCRIPTION, description);
        bundle.putString(Key.NAME_CHANEL, nameChannel);
        bundle.putString(Key.LIKE, like);
        bundle.putString(Key.DISLIKE, disLike);
        bundle.putString(Key.VIEWCOUNT, viewCount);
        bundle.putString(Key.COMMENTCOUNT, commentCount);
        fragment.setArguments(bundle);
    }

    public void drag() {
        binding.draggablePanel.setDraggableListener(new DraggablePanel.DraggableListener() {
            @Override
            public void onExpanded() {

            }

            @Override
            public void onChangeState(@NotNull DraggablePanel.State state) {

            }

            @Override
            public void onChangePercent(float v) {
                shadow.setAlpha(v);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.frameTop, playVideoFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameBottom, detailPlayFragment).commit();


        if (binding.draggablePanel.isMinimize()){
            checkMaxScreen = false;
        }
        //binding.draggablePanel.setVisibility(View.GONE);

    }


    public Item getItemFirst() {
        return detailPlayFragment.getItem();
    }

    public void openPlayScreen(String videoId) {
       // DetailPlayFragment detailPlayFragment = new DetailPlayFragment();
        checkMaxScreen = true;
        getDataBundle(detailPlayFragment);
        binding.draggablePanel.setVisibility(View.VISIBLE);
        binding.draggablePanel.maximize();
        detailPlayFragment.updateDetail();
        playVideoFragment.initYouTubePlayerView(videoId);
        detailPlayFragment.getVideoDetail(videoId);
        // createExpandableMediaNotification();

    }

    public void onPlayBackground() {
        // playVideoFragment.onPlayBackground();
        Toast.makeText(MainActivity.this, "ffff", Toast.LENGTH_SHORT).show();
    }

    public void offPlayBackground() {
        playVideoFragment.offPlayBackground();
    }

    public void removeCommentScreen() {
        getSupportFragmentManager().beginTransaction().remove(commentFragment).commit();
    }


    public void openCommentScreen() {
        getDataBundle(commentFragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, commentFragment).commit();

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createExpandableMediaNotification() {
        createNotificationChannels();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_logo);
        Intent intent = new Intent(getIntent());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play_background)
                .addAction(R.drawable.ic_previous_black_24, "Previous", null) // #0
                .addAction(R.drawable.ic_pause_24, "Pause", pendingIntent)  // #1
                .addAction(R.drawable.ic_next_black_24, "Next", null)     // #2
                .setStyle(new Notification.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(null))
                .setContentTitle("Expandable media notification")
                .setContentText("Artist")
                .setLargeIcon(bitmap)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    public static Bitmap getIcon(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            return null;
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel basicNotificationChanel = new NotificationChannel(CHANNEL_ID,
                    "basic notification chanel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel priorityNotificationChannel = new NotificationChannel(PRIORITY_CHANNEL_ID,
                    "priority notification channel",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(basicNotificationChanel);
            notificationManager.createNotificationChannel(priorityNotificationChannel);
        }
    }

    private void addControl() {
        binding.viewPagerMain.setOffscreenPageLimit(4);
        binding.tabLayoutMain.setupWithViewPager(binding.viewPagerMain);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        binding.viewPagerMain.setAdapter(viewPagerAdapter);
        binding.viewPagerMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayoutMain));
        binding.tabLayoutMain.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPagerMain));

        binding.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.tvMainTitle.setText(R.string.main_title_music);
                        binding.tabLayoutMain.getTabAt(0).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(1).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(2).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(3).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                        break;
                    case 1:
                        binding.tvMainTitle.setText(R.string.main_title_trending);
                        binding.tabLayoutMain.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(1).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(2).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(3).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                        break;
                    case 2:
                        binding.tvMainTitle.setText(R.string.main_title_setting);
                        binding.tabLayoutMain.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(1).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(2).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(3).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                        break;

                    case 3:
                        binding.tvMainTitle.setText(R.string.main_title_search);
                        binding.tabLayoutMain.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(1).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(2).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        binding.tabLayoutMain.getTabAt(3).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setIconTablayout() {
        binding.tabLayoutMain.getTabAt(0).setIcon(R.drawable.ic_home).setText(R.string.main_tab_home);
        binding.tabLayoutMain.getTabAt(0).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        binding.tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_trending).setText(R.string.main_tab_trending);
        binding.tabLayoutMain.getTabAt(2).setIcon(R.drawable.ic_setting).setText(R.string.main_tab_setting);
        binding.tabLayoutMain.getTabAt(3).setIcon(R.drawable.ic_search).setText(R.string.main_tab_search);

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public Fragment[] fragments = new Fragment[3];

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;

                case 1:
                    fragment = new TrendingFragment();
                    break;

                case 2:
                    fragment = new SettingFragment();
                    break;
                case 3:
                    fragment = new SearchFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setTitleVideo(String titleVideo) {
        this.titleVideo = titleVideo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNameChannel(String nameChannel) {
        this.nameChannel = nameChannel;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setDisLike(String disLike) {
        this.disLike = disLike;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getTitleVideo() {
        return titleVideo;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getNameChannel() {
        return nameChannel;
    }
}