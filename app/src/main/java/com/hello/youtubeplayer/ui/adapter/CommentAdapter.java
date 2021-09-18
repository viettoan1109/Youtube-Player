package com.hello.youtubeplayer.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.data.network.model.ItemComment;

import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHoder> {

    private List<ItemComment> item;
    private Activity activity;

    public CommentAdapter(List<ItemComment> item, Activity activity) {
        this.item = item;
        this.activity = activity;
    }


    public List<ItemComment> getList() {
        return item;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        ViewHoder myViewHoder = new ViewHoder(view);
        return myViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final ItemComment itemComment = item.get(position);
        Glide.with(activity).load(itemComment.getSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl())
                .centerCrop()
                .into(holder.imgChannelsComment);

        holder.tvChannelsComment.setText(itemComment.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName());
        holder.tvComment.setText(itemComment.getSnippet().getTopLevelComment().getSnippet().getTextOriginal());
        getTimeComment(holder, itemComment);

    }

    private void getTimeComment(@NonNull ViewHoder holder, ItemComment itemComment) {
        Calendar today = Calendar.getInstance();
        String myDate = itemComment.getSnippet().getTopLevelComment().getSnippet().getPublishedAt();
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
            String time = null;
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
            holder.tvChannelTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView imgChannelsComment;
        private TextView tvChannelsComment;
        private TextView tvChannelTime;
        private TextView tvComment;


        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgChannelsComment = (ImageView) itemView.findViewById(R.id.image_comment_avatar);
            tvChannelsComment = (TextView) itemView.findViewById(R.id.text_comment_channel);
            tvChannelTime = (TextView) itemView.findViewById(R.id.text_comment_time);
            tvComment = (TextView) itemView.findViewById(R.id.text_comment);

        }


    }
}
