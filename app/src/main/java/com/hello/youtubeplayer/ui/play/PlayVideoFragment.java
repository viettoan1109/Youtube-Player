package com.hello.youtubeplayer.ui.play;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.data.network.model.Item;
import com.hello.youtubeplayer.interfaces.Key;
import com.hello.youtubeplayer.ui.main.MainActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.motion.widget.MotionLayout.*;

public class PlayVideoFragment extends Fragment {

    View view;
    private List<Item> listIdVideo = new ArrayList<>();
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private DetailPlayFragment detailPlayFragment = new DetailPlayFragment();


    public static PlayVideoFragment newInstance(String videoId) {
        PlayVideoFragment playVideoFragment = new PlayVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Key.VIDEO_ID, videoId);
        playVideoFragment.setArguments(bundle);
        return playVideoFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play, container, false);
        //initYouTubePlayerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        youTubePlayerView = view.findViewById(R.id.video);

        //initPictureInPicture(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayerQ) {
                youTubePlayer = youTubePlayerQ;
            }
        });

        youTubePlayerView.getPlayerUiController().setCustomAction1(getResources().getDrawable(R.drawable.ic_skip_previous_24), new OnClickListener() {
            @Override
            public void onClick(View v) {
                previousVideo();

            }
        });

        youTubePlayerView.getPlayerUiController().setCustomAction2(getResources().getDrawable(R.drawable.ic_skip_next_24), new OnClickListener() {
            @Override
            public void onClick(View v) {
                nextVideo();
            }
        });

    }


    private void previousVideo() {
        if (listIdVideo.size() >= 2) {
            String videoId = listIdVideo.get(listIdVideo.size() - 1).getId().getVideoId();
            ((MainActivity) getActivity()).openPlayScreen(videoId);
            listIdVideo.remove(listIdVideo.size() - 1);
        }
    }

    private void nextVideo() {
        Item item = ((MainActivity) getActivity()).getItemFirst();
        ((MainActivity) getActivity()).openPlayScreen(item.getId().getVideoId());
        listIdVideo.add(item);
    }

    public void pauseVideo() {
        youTubePlayer.pause();

    }



    @Override
    public void onPause() {
        super.onPause();
       onPlayBackground();
    }

    public void onPlayBackground(){
        youTubePlayerView.enableBackgroundPlayback(true);
    }

    public void offPlayBackground(){
        youTubePlayerView.enableBackgroundPlayback(false);
    }


    public void playVideo() {
        youTubePlayer.play();
    }

    public void initYouTubePlayerView(String videoId) {
        youTubePlayer.loadVideo(videoId, 0);
        //initPictureInPicture(youTubePlayerView);

    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureIcon = new ImageView(getContext());
        pictureInPictureIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));

        pictureInPictureIcon.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if (supportsPIP)
                    getActivity().enterPictureInPictureMode();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Can't enter picture in picture mode")
                        .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                        .show();
            }

        });

        youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
    }


}
