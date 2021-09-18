package com.hello.youtubeplayer.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.common.RetrofitClient;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;
import com.hello.youtubeplayer.databinding.FragmentHomeBinding;
import com.hello.youtubeplayer.ui.adapter.YoutubeAdapter;
import com.hello.youtubeplayer.interfaces.YoutubeAPI;
import com.hello.youtubeplayer.common.ConsApp;
import com.hello.youtubeplayer.utils.ResponeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<VideoYoutube.ItemsBean> videos;

    private YoutubeAdapter youtubeAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        videos = new ArrayList<>();
        initView();
        youtubeAPICall();
        return binding.getRoot();
    }

    private void initView() {

        youtubeAdapter = new YoutubeAdapter(getActivity(), videos);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerHome.setHasFixedSize(true);
        binding.recyclerHome.setLayoutManager(manager);
        binding.recyclerHome.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerHome.setAdapter(youtubeAdapter);

    }

    private void youtubeAPICall() {
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

                    dismissProgress();

                } else {
                }
            }

            @Override
            public void onFailure(Call<VideoYoutube> call, Throwable t) {

            }
        });
    }

    private void dismissProgress() {
        binding.progress.setVisibility(View.GONE);
        binding.recyclerHome.setClickable(true);
        binding.recyclerHome.setVisibility(View.VISIBLE);
    }

}