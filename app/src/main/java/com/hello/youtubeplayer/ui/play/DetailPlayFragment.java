package com.hello.youtubeplayer.ui.play;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.common.RetrofitClient;
import com.hello.youtubeplayer.data.network.model.ChannelYoutube;
import com.hello.youtubeplayer.data.network.model.CommentYoutube;
import com.hello.youtubeplayer.data.network.model.Item;
import com.hello.youtubeplayer.data.network.model.SOAnswersResponse;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;
import com.hello.youtubeplayer.databinding.FragmentDetailPlayBinding;
import com.hello.youtubeplayer.interfaces.YoutubeAPI;
import com.hello.youtubeplayer.ui.adapter.PlayYoutubeAdapter;
import com.hello.youtubeplayer.ui.main.MainActivity;
import com.hello.youtubeplayer.common.ConsApp;
import com.hello.youtubeplayer.utils.ConvertCount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailPlayFragment extends Fragment {
    private static final String CHANNEL_ID = "channel_id";
    private static final String PRIORITY_CHANNEL_ID = "pr_channel_id";
    private FragmentDetailPlayBinding binding;
    private List<Item> videos;
    private boolean checkClickItem = false;
    private PlayYoutubeAdapter playYoutubeAdapter;
    public static final int MY_PERMISSIONS_REQUEST = 1;
    private NotificationManagerCompat notificationManagerCompat;
    private ConvertCount convertCount = new ConvertCount();

    private String description;
    private String titleVideo;
    private String channelId;
    private String urlImages;
    private Bitmap bitmap;
    private String like;
    private String disLike;
    private String viewCount;
    private String nameChannel;
    private String commentCount;
    private String[] PERMISSION_GRANTED = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static boolean isPermissions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_play, container, false);
        videos = new ArrayList<>();
        notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        createNotificationChannels();
        initView();
        initPermissions();
        comment();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getVideoDetail(String videoId) {
        Retrofit retrofit = RetrofitClient.getClient(ConsApp.BASE_URL);
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);

        Call<VideoYoutube> call = youtubeAPI.getVideoDetail(videoId, ConsApp.KEY_BROWSE);
        call.enqueue(new Callback<VideoYoutube>() {
            @Override
            public void onResponse(Call<VideoYoutube> call, Response<VideoYoutube> response) {

                if (response.isSuccessful() && response.body().getItems() != null) {
                    if (response.body().getItems().size() > 0) {
                        VideoYoutube videoYoutube = response.body();
                        titleVideo = videoYoutube.getItems().get(0).getSnippet().getTitle();
                        urlImages = String.valueOf(videoYoutube.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl());
                        channelId = videoYoutube.getItems().get(0).getSnippet().getChannelId();
                        description = videoYoutube.getItems().get(0).getSnippet().getDescription();
                        nameChannel = videoYoutube.getItems().get(0).getSnippet().getChannelTitle();
                        like = videoYoutube.getItems().get(0).getStatistics().getLikeCount();
                        disLike = videoYoutube.getItems().get(0).getStatistics().getDislikeCount();
                        viewCount = videoYoutube.getItems().get(0).getStatistics().getViewCount();
                        commentCount = videoYoutube.getItems().get(0).getStatistics().getCommentCount();


                        binding.textPlayVideoLike.setText(convertCount.convertLikeCount(like));
                        binding.textPlayVieoTitle.setText(titleVideo);
                        binding.textPlayVideoNameChannel.setText(nameChannel);
                        binding.textPlayVideoDisLike.setText(convertCount.convertLikeCount(disLike));
                        binding.textPlayVideoNumberComment.setText(convertCount.convertLikeCount(commentCount));
                        binding.textPlayVieoViewCount.setText(convertCount.convertViewCount(getContext(), viewCount));

                        createExpandableMediaNotification();
                        getChannelYoutube();
                        youtubeAPICall(titleVideo);

                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<VideoYoutube> call, Throwable t) {

            }
        });

        getFirstComment(videoId);
        share(videoId);
    }

    private void initView() {
        playYoutubeAdapter = new PlayYoutubeAdapter(getActivity(), videos);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerPlayVideo.setHasFixedSize(true);
        binding.recyclerPlayVideo.setLayoutManager(manager);
        binding.recyclerPlayVideo.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerPlayVideo.setAdapter(playYoutubeAdapter);


    }

    private void getFirstComment(String videoId) {
        Retrofit retrofit = RetrofitClient.getClient(ConsApp.BASE_URL);
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<CommentYoutube> call = youtubeAPI.getCommentYoutube(100, videoId, ConsApp.KEY_BROWSE);
        call.enqueue(new Callback<CommentYoutube>() {
            @Override
            public void onResponse(Call<CommentYoutube> call, Response<CommentYoutube> response) {
                if (response.isSuccessful() && response.body().getItemComments() != null) {
                    if (response.body().getItemComments().size() > 0) {
                        CommentYoutube commentYoutube = response.body();

                        Glide.with(getActivity())
                                .load(commentYoutube.getItemComments().get(0).getSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl())
                                .centerCrop()
                                .into(binding.imagePlayVideoCommentId);

                        binding.textPlayVideoFirstComment.setText(commentYoutube.getItemComments()
                                .get(0).getSnippet().getTopLevelComment().getSnippet().getTextOriginal());


                    }
                } else {
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentYoutube> call, Throwable t) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createExpandableMediaNotification() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        Notification notification = new Notification.Builder(getActivity(), CHANNEL_ID)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play_background)
                .addAction(R.drawable.ic_previous_black_24, "Previous", null) // #0
                .addAction(R.drawable.ic_pause_24, "Pause", pendingIntent)  // #1
                .addAction(R.drawable.ic_next_black_24, "Next", null)     // #2
                .setStyle(new Notification.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(null))
                .setContentTitle(titleVideo)
                .setContentText(nameChannel)
                .setLargeIcon(getIcon(urlImages))
                .build();

        notificationManagerCompat.notify(1, notification);

    }

    public Bitmap getIcon(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel basicNotificationChanel = new NotificationChannel(CHANNEL_ID,
                    "basic notification chanel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel priorityNotificationChannel = new NotificationChannel(PRIORITY_CHANNEL_ID,
                    "priority notification channel",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(basicNotificationChanel);
            notificationManager.createNotificationChannel(priorityNotificationChannel);
        }
    }

    private void comment() {
        binding.relativePlayVideoComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity fragmentActivity = getActivity();
                if (fragmentActivity instanceof MainActivity) {
                    ((MainActivity) fragmentActivity).openCommentScreen();
                }
            }
        });
    }

    public void dismissShareScreen() {
        binding.shareBottomSheet.dismissSheet();
    }

    private void share(String videoId) {
        binding.constraintPlayVideoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + videoId);
                shareIntent.setType("text/plain");
                IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(getActivity(), shareIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
                    @Override
                    public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                        binding.shareBottomSheet.dismissSheet();
                        startActivity(activityInfo.getConcreteIntent(shareIntent));
                        Log.d("TAG", "onIntentPicked: " + activityInfo.getConcreteIntent(shareIntent));
                    }

                });

                intentPickerSheet.setFilter(new IntentPickerSheetView.Filter() {
                    @Override
                    public boolean include(IntentPickerSheetView.ActivityInfo info) {
                        return !info.componentName.getPackageName().startsWith("com.android");
                    }
                });

                intentPickerSheet.setSortMethod(new Comparator<IntentPickerSheetView.ActivityInfo>() {
                    @Override
                    public int compare(IntentPickerSheetView.ActivityInfo lhs, IntentPickerSheetView.ActivityInfo rhs) {
                        return rhs.label.compareTo(lhs.label);
                    }
                });
                binding.shareBottomSheet.showWithSheetView(intentPickerSheet);
            }
        });
    }

    private void youtubeAPICall(String query) {

        Retrofit retrofit = RetrofitClient.getClient(ConsApp.BASE_URL);
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<SOAnswersResponse> call = youtubeAPI.getAnswers(ConsApp.KEY_BROWSE, ConsApp.PART, query, ConsApp.TYPE, 10);
        call.enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {
                if (response.isSuccessful() && response.body().getItems() != null) {
                    if (response.body().getItems().size() > 0) {
                        if (!videos.isEmpty()) {
                            videos.clear();
                        }
                        videos = response.body().getItems();
                        videos.remove(0);
                        //videos.removeAll(videos);
                        playYoutubeAdapter.getItems().clear();
                        playYoutubeAdapter.getItems().addAll(videos);

                        playYoutubeAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(getContext(), "No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SOAnswersResponse> call, Throwable t) {

            }
        });

    }

    public void updateDetail() {
        videos.removeAll(videos);
        playYoutubeAdapter.notifyDataSetChanged();
    }

    public Item getItem() {
        return videos.get(2);
    }

    public void getOutputMediaFile(Uri uri) {
        File mediaFile;
        String sourceVideoName = uri.getPath();
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString();
        File myDir = new File(root + "/Text chat story");
        if (!myDir.exists()) {
            if (myDir.mkdirs()) {
                Log.i("Folder ", "created");
            }
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String videoName = "Video_" + n + ".mp4";
        mediaFile = new File(myDir.getAbsolutePath() + "/" + videoName);
        Log.d("TAG", "getOutputMediaFile: ");

        try {
            mediaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mediaFile.exists()) {
            mediaFile.delete();
        }
        try {
            FileInputStream in = new FileInputStream(sourceVideoName);
            FileOutputStream out = new FileOutputStream(mediaFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requireContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mediaFile)));
        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
    }


    private void getChannelYoutube() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConsApp.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<ChannelYoutube> call = youtubeAPI.getChannelYoutube(channelId, ConsApp.KEY_BROWSE);
        call.enqueue(new Callback<ChannelYoutube>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ChannelYoutube> call, Response<ChannelYoutube> response) {
                if (response.isSuccessful() && response.body().getItems() != null) {
                    if (response.body().getItems().size() > 0) {
                        ChannelYoutube channelYoutube = response.body();

                        Glide.with(getActivity())
                                .load(channelYoutube.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl())
                                .centerCrop()
                                .into(binding.imagePlayVideoChannel);

                        String count = channelYoutube.getItems().get(0).getStatistics().getSubscriberCount();
                        binding.textPlayVideoSubscriber.setText(convertCount.convertLikeCount(count) + " " + getString(R.string.subscribe));

                    }
                }
            }

            @Override
            public void onFailure(Call<ChannelYoutube> call, Throwable t) {

            }
        });
    }


    private void initPermissions() {
        if (permissions()) {
            isPermissions = true;
        } else {
            myRequestPermissions();
        }
    }

    private boolean permissions() {
        for (String permission : PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void myRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSION_GRANTED, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length == PERMISSION_GRANTED.length) {
                boolean isAll = true;
                for (int p : grantResults) {
                    if (p == PackageManager.PERMISSION_DENIED) {
                        isAll = false;
                    }
                }
                if (isAll) {
                    isPermissions = true;
                }
            }
        }
    }

    private ProgressDialog mDialog;

    private class RequestDownloadVideoStream extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Downloading...");
            mDialog.setIndeterminate(false);
            mDialog.setMax(100);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream mInputStream = null;
            URL mUrl = null;
            int mLen1 = 0;
            int mTemp_progress = 0;
            int progress = 0;
            try {
                mUrl = new URL(params[0]);
                mInputStream = mUrl.openStream();
                URLConnection mUrlConnection = mUrl.openConnection();
                mUrlConnection.connect();
                int size = mUrlConnection.getContentLength();

                if (mUrlConnection != null) {
                    String mFileName = params[1] + ".mp4";
                    String mtoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/YoutubeVideos";
                    Log.d("TAG", "doInBackground: " + mtoragePath);
                    File mfile = new File(mtoragePath);
                    if (!mfile.exists()) {
                        mfile.mkdir();
                    }

                    FileOutputStream mFileOutputStream = new FileOutputStream(mfile + "/" + mFileName);
                    byte[] buffer = new byte[1024];
                    int total = 0;
                    if (mInputStream != null) {
                        while ((mLen1 = mInputStream.read(buffer)) != -1) {
                            total += mLen1;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            progress = (int) ((total * 100) / size);
                            if (progress >= 0) {
                                mTemp_progress = progress;
                                publishProgress("" + progress);
                            } else
                                publishProgress("" + mTemp_progress + 1);

                            mFileOutputStream.write(buffer, 0, mLen1);
                        }
                    }

                    if (mFileOutputStream != null) {
                        publishProgress("" + 100);
                        mFileOutputStream.close();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (mInputStream != null) {
                    try {
                        mInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mDialog.isShowing())
                mDialog.dismiss();
        }
    }

}