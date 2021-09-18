package com.hello.youtubeplayer.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;
import com.hello.youtubeplayer.ui.main.MainActivity;
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.utils.ConvertCount;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHoder> {

    private List<VideoYoutube.ItemsBean> items;
    private Activity activity;
    private ConvertCount convertCount = new ConvertCount();

    public YoutubeAdapter(Activity activity, List<VideoYoutube.ItemsBean> items) {
        this.activity = activity;
        this.items = items;
    }

    public List<VideoYoutube.ItemsBean> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        ViewHoder myViewHoder = new ViewHoder(view);
        return myViewHoder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        final VideoYoutube.ItemsBean item = items.get(position);
        holder.tvTitle.setText(item.getSnippet().getTitle());

        // long view = Long.parseLong(item.getStatistics().getViewCount());
        String viewCountChannel = convertCount.convertViewCount(activity, item.getStatistics().getViewCount());
        String date = convertCount.convertTime(activity, item);
        holder.tvChannelTitle.setText(item.getSnippet().getChannelTitle() + " - " + viewCountChannel + " - " + date);


        String videoTime = item.getContentDetails().getDuration();
        if (videoTime == null) {
            holder.tvTime.setVisibility(View.INVISIBLE);
        } else {
            String replace1 = videoTime.replace("PT", "");
            String replace2 = replace1.replace('H', ':');
            String replace3 = replace2.replace('M', ':');
            String replace4 = replace3.replace('S', ' ');
            holder.tvTime.setText(replace4);
        }


        Glide.with(activity).load(item.getSnippet().getThumbnails().getHigh().getUrl())
                .centerCrop()
                .into(holder.imgVideo);

        Glide.with(activity).load(item.getSnippet().getThumbnails().getHigh().getUrl())
                .centerCrop()
                .into(holder.imgChannel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity fragmentActivity = (FragmentActivity) activity;
                if (fragmentActivity instanceof MainActivity) {

                    if (!((MainActivity) fragmentActivity).isCheckMaxScreen()){
                        ((MainActivity) fragmentActivity).setCheckMaxScreen(true);
                        String videoId = item.getId();
                        String channelId = item.getSnippet().getChannelId();
                        String title = item.getSnippet().getTitle();
                        String description = item.getSnippet().getDescription();
                        String nameChannel = item.getSnippet().getChannelTitle();
                        String like = item.getStatistics().getLikeCount();
                        String disLike = item.getStatistics().getDislikeCount();
                        String commentCount = item.getStatistics().getCommentCount();

                        ((MainActivity) fragmentActivity).setVideoId(videoId);
                        ((MainActivity) fragmentActivity).setChannelID(channelId);
                        ((MainActivity) fragmentActivity).setTitleVideo(title);
                        ((MainActivity) fragmentActivity).setDescription(description);
                        ((MainActivity) fragmentActivity).setNameChannel(nameChannel);
                        ((MainActivity) fragmentActivity).setLike(like);
                        ((MainActivity) fragmentActivity).setDisLike(disLike);
                        ((MainActivity) fragmentActivity).setViewCount(viewCountChannel);
                        ((MainActivity) fragmentActivity).setCommentCount(commentCount);

                        ((MainActivity) fragmentActivity).openPlayScreen(videoId);
                        ((MainActivity) fragmentActivity).setCheckMaxScreen(true);

                    }

                    // ((MainActivity) fragmentActivity).buttonOpenDialogClicked();
                }
            }
        });
    }

    @NotNull
    private String convertCount(long view) {
        return view < 1000 ? String.valueOf(view) + " " + activity.getString(R.string.view_count) :
                (view > 1000000 && view < 10000000) ? String.valueOf(view).substring(0, 1) + "," + String.valueOf(view).substring(1, 2) + " " + activity.getString(R.string.view_count_m) :
                        (view > 10000000 && view < 100000000) ?
                                String.valueOf(view).substring(0, 2) + " " + activity.getString(R.string.view_count_m) :
                                ((view > 100000000 && view < 1000000000) ? String.valueOf(view).substring(0, 3) + " " + activity.getString(R.string.view_count_m) : (view > 1000000000) ?
                                        String.valueOf(view).substring(0, 1) + " " + activity.getString(R.string.view_count_b) : "");
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public String convertDuration(long time) {
        String duration = "01:52:33";
        if (time > 0 && time < 60) {
            duration = "00:" + time;
        }
        if (time >= 60 && time < 3600) {
            duration = (time / 60) + ":" + (time % 60);
        }
        if (time >= 3600) {
            duration = (time / 3600) + ":" + ((time % 3600) / 60) + ":" + (((time % 3600) % 60));
        }
        return duration;
    }

    private String getTimeComment(VideoYoutube.ItemsBean item) {
        String time = null;
        Calendar today = Calendar.getInstance();
        String myDate = item.getSnippet().getPublishedAt();
        String inputModified = myDate.replace(" ", "T");
        int lengthOfAbbreviatedOffset = 3;
        if (inputModified.indexOf("+") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
            // If third character from end is a PLUS SIGN, append ':00'.
            inputModified = inputModified + ":00";
        }
        if (inputModified.indexOf("-") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
            // If third character from end is a PLUS SIGN, append ':00'.
            inputModified = inputModified + ":00";
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            OffsetDateTime odt = OffsetDateTime.parse(inputModified);
            long millis = odt
                    .toInstant().toEpochMilli();
            long diff = today.getTimeInMillis() - millis;
            long days = diff / (24 * 60 * 60 * 1000);
            long year = days / 365;
            long month = (days - year * 365) / 30;
            long day = (days - year * 365 - month * 30);
            long hour = diff / (60 * 60 * 1000) - year * 365 * 24 - month * 30 * 24 - day * 24;
            long minute = diff / (60 * 1000) - year * 365 * 24 * 60 - month * 30 * 24 * 60 - day * 24 * 60 - hour * 60;

            if (year > 0) {
                time = year + " " + activity.getString(R.string.last_year);
            } else if (month > 0 && year == 0) {
                time = month + " " + activity.getString(R.string.last_month);
            } else if (day > 0 && month == 0 && year == 0) {
                time = day + " " + activity.getString(R.string.last_day);
            } else if (hour > 0 && day == 0 && month == 0 && year == 0) {
                time = hour + " " + activity.getString(R.string.hours_ago);
            } else if (minute > 0 && hour == 0 && day == 0 && month == 0 && year == 0) {
                time = minute + " " + activity.getString(R.string.minute_ago);
            }
        }
        return time;
    }


    public class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView imgVideo;
        private TextView tvTitle;
        private TextView tvChannelTitle;
        private ImageView imgChannel;
        private CardView cardView;
        private TextView tvTime;
        private TextView tvViewCount;
        private TextView tvDate;


        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgVideo = itemView.findViewById(R.id.image_video_thumbnail);
            tvTitle = itemView.findViewById(R.id.text_video_title);
            tvChannelTitle = itemView.findViewById(R.id.text_video_channelTitle);
            imgChannel = itemView.findViewById(R.id.image_video_avatar);
            cardView = itemView.findViewById(R.id.card_video);
            tvTime = itemView.findViewById(R.id.text_video_time);
            tvViewCount = itemView.findViewById(R.id.text_video_viewCount);
            tvDate = itemView.findViewById(R.id.text_video_date);

        }

    }
}
