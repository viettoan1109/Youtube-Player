package com.hello.youtubeplayer.ui.comment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.hello.youtubeplayer.common.RetrofitClient;
import com.hello.youtubeplayer.data.network.model.CommentYoutube;
import com.hello.youtubeplayer.data.network.model.ItemComment;
import com.hello.youtubeplayer.databinding.FragmentCommentBinding;
import com.hello.youtubeplayer.interfaces.Key;
import com.hello.youtubeplayer.interfaces.YoutubeAPI;
import com.hello.youtubeplayer.ui.adapter.CommentAdapter;
import com.hello.youtubeplayer.ui.main.MainActivity;
import com.hello.youtubeplayer.common.ConsApp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentFragment extends Fragment {


    private String videoId;
    private FragmentCommentBinding binding;
    private List<ItemComment> items;
    private CommentAdapter commentAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            videoId = bundle.getString(Key.VIDEO_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false);
        items = new ArrayList<>();
        initView();
        commentList();
        return binding.getRoot();
    }

    private void initView() {
        commentAdapter = new CommentAdapter(items, getActivity());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerComment.setHasFixedSize(true);
        binding.recyclerComment.setLayoutManager(manager);
        binding.recyclerComment.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerComment.setAdapter(commentAdapter);
        binding.imgCommentClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).removeCommentScreen();
            }
        });
    }

    private void commentList() {
        Retrofit retrofit = RetrofitClient.getClient(ConsApp.BASE_URL);
        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<CommentYoutube> call = youtubeAPI.getCommentYoutube(100, videoId, ConsApp.KEY_BROWSE);
        call.enqueue(new Callback<CommentYoutube>() {
            @Override
            public void onResponse(Call<CommentYoutube> call, Response<CommentYoutube> response) {
                if (response.isSuccessful() && response.body().getItemComments() != null) {
                    if (!items.isEmpty()) {
                        items.clear();
                    }
                    items = response.body().getItemComments();
                    commentAdapter.getList().clear();
                    commentAdapter.getList().addAll(items);
                    commentAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentYoutube> call, Throwable t) {

            }
        });
    }

}