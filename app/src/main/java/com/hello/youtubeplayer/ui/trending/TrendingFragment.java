package com.hello.youtubeplayer.ui.trending;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.common.ConsApp;
import com.hello.youtubeplayer.common.RetrofitClient;
import com.hello.youtubeplayer.data.network.model.VideoYoutube;
import com.hello.youtubeplayer.databinding.FragmentTrendingBinding;
import com.hello.youtubeplayer.ui.adapter.YoutubeAdapter;
import com.hello.youtubeplayer.interfaces.YoutubeAPI;
import com.hello.youtubeplayer.utils.ResponeApi;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrendingFragment extends Fragment {
    private List<VideoYoutube.ItemsBean> videos;

    private FragmentTrendingBinding binding;
    private YoutubeAdapter youtubeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false);
        videos = new ArrayList<>();
        initView();
        youtubeAPICall();
        // new ParseVideoYoutube().execute();
        return binding.getRoot();
    }

    private void initView() {
        youtubeAdapter = new YoutubeAdapter(getActivity(), videos);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerTrending.setHasFixedSize(true);
        binding.recyclerTrending.setLayoutManager(manager);
        binding.recyclerTrending.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerTrending.setAdapter(youtubeAdapter);

    }

    private void youtubeAPICall() {
        ResponeApi responeApi = new ResponeApi(videos);
        responeApi.getVideoTrending(youtubeAdapter);

    }

}