package com.hello.youtubeplayer.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hello.youtubeplayer.ui.main.MainActivity;
import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.data.network.model.Item;

import java.util.List;

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.ViewHolder> {

    private List<Item> items;
    private Activity activity;

    public SearchVideoAdapter(List<Item> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    public List<Item> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_video, parent, false);
        ViewHolder myViewHoder = new ViewHolder(view);
        return myViewHoder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.tvSearchTitle.setText(item.getSnippet().getTitle());
        holder.tvSearchChannelTitle.setText(item.getSnippet().getChannelTitle());


        Glide.with(activity).load(item.getSnippet().getThumbnails().getHigh().getUrl())
                .centerCrop()
                .into(holder.imgSearchVideo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity fragmentActivity = (FragmentActivity) activity;
                if (fragmentActivity instanceof MainActivity) {
                    String videoId = item.getId().getVideoId();
                    ((MainActivity) fragmentActivity).setVideoId(videoId);
                    ((MainActivity) fragmentActivity).openPlayScreen(videoId);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSearchVideo;
        private TextView tvSearchTitle;
        private TextView tvSearchChannelTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSearchVideo = itemView.findViewById(R.id.image_searchVideo_thumbnail);
            tvSearchTitle = itemView.findViewById(R.id.text_searchVideo_title);
            tvSearchChannelTitle = itemView.findViewById(R.id.text_searchVideo_channelTitle);
        }
    }
}
