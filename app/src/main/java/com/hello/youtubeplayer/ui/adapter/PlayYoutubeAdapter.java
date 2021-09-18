package com.hello.youtubeplayer.ui.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.data.network.model.Item;
import com.hello.youtubeplayer.ui.main.MainActivity;

import java.util.List;

public class PlayYoutubeAdapter extends RecyclerView.Adapter<PlayYoutubeAdapter.ViewHoder> {

    private List<Item> items;
    private Activity activity;

    public PlayYoutubeAdapter(Activity activity, List<Item> items) {
        this.activity = activity;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        ViewHoder myViewHoder = new ViewHoder(view);
        return myViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final Item item = items.get(position);
        holder.tvTitle.setText(item.getSnippet().getTitle());
        holder.tvChannelTitle.setText(item.getSnippet().getChannelTitle());


        Glide.with(activity).load(item.getSnippet().getThumbnails().getHigh().getUrl())
                .centerCrop()
                .into(holder.imgVideo);

        Glide.with(activity).load(item.getSnippet().getThumbnails().get_default().getUrl())
                .centerCrop()
                .into(holder.imgChannel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity fragmentActivity = (FragmentActivity) activity;
                if (fragmentActivity instanceof MainActivity){
                    String videoId = item.getId().getVideoId();
                    String title = item.getSnippet().getTitle();
                    String description = item.getSnippet().getDescription();
                    String nameChannel = item.getSnippet().getChannelTitle();

                    ((MainActivity) fragmentActivity).setVideoId(videoId);
                    ((MainActivity) fragmentActivity).setTitleVideo(title);
                    ((MainActivity) fragmentActivity).setDescription(description);
                    ((MainActivity) fragmentActivity).setNameChannel(nameChannel);

                    ((MainActivity) fragmentActivity).openPlayScreen(videoId);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView imgVideo;
        private TextView tvTitle;
        private TextView tvChannelTitle;
        private ImageView imgChannel;
        private CardView cardView;


        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgVideo = itemView.findViewById(R.id.image_video_thumbnail);
            tvTitle = itemView.findViewById(R.id.text_video_title);
            tvChannelTitle = itemView.findViewById(R.id.text_video_channelTitle);
            imgChannel = itemView.findViewById(R.id.image_video_avatar);
            cardView = itemView.findViewById(R.id.card_video);

        }


    }
}
