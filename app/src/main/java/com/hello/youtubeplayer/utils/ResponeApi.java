package com.hello.youtubeplayer.utils;

import com.hello.youtubeplayer.common.ConsApp;
import com.hello.youtubeplayer.common.RetrofitClient;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;
import com.hello.youtubeplayer.interfaces.YoutubeAPI;
import com.hello.youtubeplayer.ui.adapter.YoutubeAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResponeApi {
    private List<VideoYoutube.ItemsBean> videos;

    public ResponeApi(List<VideoYoutube.ItemsBean> videos) {
        this.videos = videos;
    }


    public void getVideoHome(YoutubeAdapter youtubeAdapter) {

        Retrofit retrofit = RetrofitClient.getClient(ConsApp.BASE_URL);
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<VideoYoutube> call = youtubeAPI.getVideoYoutube(50, ConsApp.KEY_BROWSE, "VN");
        call.enqueue(new Callback<VideoYoutube>() {
            @Override
            public void onResponse(Call<VideoYoutube> call, Response<VideoYoutube> response) {
                if (response.isSuccessful() && response.body().getItems() != null) {
                    if (!videos.isEmpty()) {
                        videos.clear();
                    }
                    videos = response.body().getItems();
                    youtubeAdapter.getItems().clear();
                    youtubeAdapter.getItems().addAll(videos);
                    youtubeAdapter.notifyDataSetChanged();


                } else {
                }
            }

            @Override
            public void onFailure(Call<VideoYoutube> call, Throwable t) {

            }
        });
    }

    public void getVideoTrending(YoutubeAdapter youtubeAdapter){
        Retrofit retrofit = RetrofitClient.getClient(ConsApp.BASE_URL);
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<VideoYoutube> call = youtubeAPI.getVideoYoutube(50, ConsApp.KEY_BROWSE, "CN");
        call.enqueue(new Callback<VideoYoutube>() {
            @Override
            public void onResponse(Call<VideoYoutube> call, Response<VideoYoutube> response) {
                if (response.isSuccessful() && response.body().getItems() != null) {
                    if (!videos.isEmpty()) {
                        videos.clear();
                    }
                    videos = response.body().getItems();
                    youtubeAdapter.getItems().clear();
                    youtubeAdapter.getItems().addAll(videos);
                    youtubeAdapter.notifyDataSetChanged();

                } else {
                }
            }

            @Override
            public void onFailure(Call<VideoYoutube> call, Throwable t) {

            }
        });
    }


}
