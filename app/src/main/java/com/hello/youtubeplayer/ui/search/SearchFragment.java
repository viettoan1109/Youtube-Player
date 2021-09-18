package com.hello.youtubeplayer.ui.search;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.hello.youtubeplayer.R;
import com.hello.youtubeplayer.databinding.FragmentSearchBinding;
import com.hello.youtubeplayer.interfaces.ICallBack;
import com.hello.youtubeplayer.ui.adapter.SearchVideoAdapter;
import com.hello.youtubeplayer.interfaces.YoutubeAPI;
import com.hello.youtubeplayer.data.network.model.Item;
import com.hello.youtubeplayer.data.network.model.SOAnswersResponse;
import com.hello.youtubeplayer.common.ConsApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private List<Item> videos;
    private SearchVideoAdapter searchVideoAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        videos = new ArrayList<>();
        initView();
        search();
        return binding.getRoot();
    }

    private void initView() {

        searchVideoAdapter = new SearchVideoAdapter(videos, getActivity());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerSearch.setHasFixedSize(true);
        binding.recyclerSearch.setLayoutManager(manager);
        binding.recyclerSearch.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerSearch.setAdapter(searchVideoAdapter);


    }

    private void search() {

        binding.searchView.setOnclickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                youtubeAPICall(string);
            }

        });
    }


    private void youtubeAPICall(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConsApp.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<SOAnswersResponse> call = youtubeAPI.getAnswers(ConsApp.KEY_BROWSE, ConsApp.PART, query, ConsApp.TYPE, 50);
        call.enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {
                if (response.isSuccessful() && response.body().getItems() != null) {
                   if (response.body().getItems().size() > 0){
                       if (!videos.isEmpty()) {
                           videos.clear();
                       }
                       videos = response.body().getItems();
                       searchVideoAdapter.getItems().clear();
                       searchVideoAdapter.getItems().addAll(videos);
                       searchVideoAdapter.notifyDataSetChanged();
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

}